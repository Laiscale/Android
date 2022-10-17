package com.example.profile.edit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.common.MyApp;
import com.example.common.bean.ImageUploadData;
import com.example.common.bean.RetrofitResponse;
import com.example.common.bean.UserBean;
import com.example.common.bean.UserUpdateData;
import com.example.common.constants.HttpConstants;
import com.example.common.util.FragmentStackUtil;
import com.example.common.util.MyToast;
import com.example.common.util.UriTofilePath;
import com.example.profile.R;
import com.example.profile.databinding.FragmentEditDataBinding;

import java.io.IOException;

public class EditDataFragment extends Fragment {
    private FragmentEditDataBinding viewBinding;
    private EditDataViewModel viewModel;
    private UserBean userBean;
    private String username;
    private String introduce;
    private int sexNum;

    public static EditDataFragment newInstance(UserBean userBean) {
        EditDataFragment fragment = new EditDataFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserBean", userBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) userBean = (UserBean) getArguments().getSerializable("UserBean");
        username = MyApp.getUserBean().username;
        introduce = MyApp.getUserBean().introduce;
        sexNum = MyApp.getUserBean().sex;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentEditDataBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(EditDataViewModel.class);

        // 显示原有的用户信息
        Glide.with(requireContext()).load(MyApp.getUserBean().avatar).into(viewBinding.imageAvatar);
        viewBinding.username.setText(userBean.username);
        if (userBean.sex == 0) {
            viewBinding.sex.setText("男");
        } else {
            viewBinding.sex.setText("女");
        }
        viewBinding.introduce.setText(userBean.introduce);
        // 监听按钮，发起改变请求
        viewBinding.updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewBinding.introduce.getText().toString().isEmpty()) introduce = viewBinding.introduce.getText().toString();
                if (!viewBinding.username.getText().toString().isEmpty()) username = viewBinding.username.getText().toString();
                String sex = viewBinding.sex.getText().toString();
                if (sex.equals("男")) sexNum = 0;
                else if (sex.equals("女")) sexNum = 1;
                viewModel.saveUserInfo(MyApp.getUserBean().avatar, MyApp.getUserBean().id, introduce, sexNum, username);
            }
        });

        // 监听信息改变的结果
        viewModel.userUpdateRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                if (stringRetrofitResponse.code == HttpConstants.SUCCESS_STATUS){
                    UserBean userBean = new UserBean(
                            MyApp.getUserBean().appKey,
                            MyApp.getUserBean().avatar,
                            MyApp.getUserBean().createTime,
                            MyApp.getUserBean().id,
                            introduce,
                            MyApp.getUserBean().lastUpdateTime,
                            MyApp.getUserBean().password,
                            sexNum,
                            username
                    );
                    MyApp.setUserBean(userBean);
                    FragmentStackUtil.popBackStack(requireActivity().getSupportFragmentManager());
                }else {
                    MyToast.ShowToast(requireContext(), stringRetrofitResponse.msg);
                }
            }
        });

        // 观测头像上传结果
        viewModel.uploadData.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<ImageUploadData>>() {
            @Override
            public void onChanged(RetrofitResponse<ImageUploadData> imageUploadDataRetrofitResponse) {
                if (imageUploadDataRetrofitResponse.code == HttpConstants.SUCCESS_STATUS && !imageUploadDataRetrofitResponse.data.imageUrlList.isEmpty()){
                    MyApp.getUserBean().avatar = imageUploadDataRetrofitResponse.data.imageUrlList.get(0);
                }else {
                    MyToast.ShowToast(requireContext(), imageUploadDataRetrofitResponse.msg);
                }
            }
        });

        // 相册
        ActivityResultLauncher<String> stringActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Bitmap bitmap;
                // 防止未选图片返回时闪退
                if (result == null) return;
                // 本地显示
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    try {
                        bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().getContentResolver(), result));
                        viewBinding.imageAvatar.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // 上传到网络
                String imagePath = UriTofilePath.getFileAbsolutePath(requireContext(), result);
                viewModel.uploadAvatar(imagePath);
            }
        });

        // 监听头像上传按钮
        viewBinding.imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringActivityResultLauncher.launch("image/*");
            }
        });

        viewBinding.backNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentStackUtil.popBackStack(requireActivity().getSupportFragmentManager());
            }
        });

        // TODO 添加上传图片修改的功能
        return viewBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentStackUtil.navBack();
    }
}