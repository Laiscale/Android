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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.bean.ImageUploadData;
import com.example.common.bean.RetrofitResponse;
import com.example.common.constants.HttpConstants;
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
    private ImageListAdapter imageListAdapter = new ImageListAdapter(imageList);
    private Boolean uploadFinish = false;
    private Boolean isShare = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentReleaseBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ReleaseViewModel.class);

        ActivityResultLauncher<String> stringActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            Bitmap bitmap;
            @Override
            public void onActivityResult(Uri result) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().getContentResolver(), result));
                    }
                    String imagePath = UriTofilePath.getFilePathByUri(requireContext(), result);
                    // 添加到图片列表
                    imageList.add(imagePath);
                    imageListAdapter = new ImageListAdapter(imageList);
                    viewBinding.imageList.setAdapter(imageListAdapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        viewBinding.imageList.setAdapter(imageListAdapter);

        viewBinding.imageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开相册
                stringActivityResultLauncher.launch("image/*");
            }
        });

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

        return viewBinding.getRoot();
    }
}