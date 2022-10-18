package com.example.release;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.common.MyApp;
import com.example.common.adapter.PicRecyclerViewAdapter;
import com.example.common.bean.ImageUploadData;
import com.example.common.bean.RetrofitResponse;
import com.example.common.constants.HttpConstants;
import com.example.common.listener.OnItemClickListener;
import com.example.common.listener.RecyclerViewItemListener;
import com.example.common.util.MyToast;
import com.example.common.util.UriTofilePath;
import com.example.release.databinding.FragmentReleaseBinding;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class ReleaseFragment extends Fragment {
    private FragmentReleaseBinding viewBinding;
    private ReleaseViewModel viewModel;
    private List<String> imageList = new LinkedList<>();
    private ImageRecyclerViewAdapter imageListAdapter ;
    private Boolean uploadFinish = false;
    private Boolean isShare = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在列表后面加一个无效的URL
        imageList.add("EmptyUrl");
        imageListAdapter = new ImageRecyclerViewAdapter(imageList);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentReleaseBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ReleaseViewModel.class);

        viewBinding.imageRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        viewBinding.imageRecyclerView.setAdapter(imageListAdapter);

        ActivityResultLauncher<String> stringActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                // 防止未选图片返回时闪退
                if (result == null) return;
                String imagePath = UriTofilePath.getFileAbsolutePath(requireContext(), result);
                // 添加到图片列表, 每次都在前面添加，保证最后一个一定是无效URL
                imageList.add(0, imagePath);
                imageListAdapter = new ImageRecyclerViewAdapter(imageList);
                viewBinding.imageRecyclerView.setAdapter(imageListAdapter);
                imageListAdapter.notifyItemRangeChanged(0, imageList.size());
            }
        });


        // 监测图片上传结果，再进行相应的网络请求
        viewModel.uploadData.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<ImageUploadData>>() {
            @Override
            public void onChanged(RetrofitResponse<ImageUploadData> imageUploadDataRetrofitResponse) {
                if (imageUploadDataRetrofitResponse.code == HttpConstants.SUCCESS_STATUS){
                    uploadFinish = true;
                    if (isShare){
                        viewModel.sharePic(viewBinding.shareText.getText().toString(), viewBinding.shareTitle.getText().toString(), imageUploadDataRetrofitResponse.data.imageCode, MyApp.getUserBean().id);
                    }else {
                        viewModel.savePic(viewBinding.shareText.getText().toString(), viewBinding.shareTitle.getText().toString(), imageUploadDataRetrofitResponse.data.imageCode, MyApp.getUserBean().id);
                    }
                }else {
                    MyToast.ShowToast(requireContext(), imageUploadDataRetrofitResponse.msg);
                }
            }
        });

        // 检测分享结果
        viewModel.shareRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                viewBinding.shareTitle.setText("");
                viewBinding.shareText.setText("");
                imageList.clear();
                // 在列表后面加一个无效的URL
                imageList.add("EmptyUrl");
                // 刷新界面
                viewBinding.imageRecyclerView.setAdapter(imageListAdapter);
                imageListAdapter.notifyItemRangeChanged(0, imageList.size());
            }
        });

        // 检测保存结果
        viewModel.saveRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                viewBinding.shareTitle.setText("");
                viewBinding.shareText.setText("");
                imageList.clear();
                // 在列表后面加一个无效的URL
                imageList.add("EmptyUrl");
                // 刷新界面
                viewBinding.imageRecyclerView.setAdapter(imageListAdapter);
                imageListAdapter.notifyItemRangeChanged(0, imageList.size());
            }
        });


        viewBinding.imageRecyclerView.addOnItemTouchListener(new RecyclerViewItemListener(requireContext(), viewBinding.imageRecyclerView, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 如果是最后一个被点击
                if (position == imageList.size() - 1){
                    // 打开相册
                    stringActivityResultLauncher.launch("image/*");
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        viewBinding.saveDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.uploadImage(imageList);
                uploadFinish = false;
                isShare = false;
            }
        });

        viewBinding.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.uploadImage(imageList);
                uploadFinish = false;
                isShare = true;
            }
        });

        return viewBinding.getRoot();
    }
}