package com.example.profile.edit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.common.MyApp;
import com.example.common.bean.RetrofitResponse;
import com.example.common.bean.UserBean;
import com.example.common.constants.HttpConstants;
import com.example.common.util.FragmentStackUtil;
import com.example.common.util.MyToast;
import com.example.profile.databinding.FragmentEditDataBinding;

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
                int sexNum = MyApp.getUserBean().sex;
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