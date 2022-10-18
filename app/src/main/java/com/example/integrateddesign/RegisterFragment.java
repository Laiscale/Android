package com.example.integrateddesign;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.common.MyApp;
import com.example.common.bean.RetrofitResponse;
import com.example.common.constants.HttpConstants;
import com.example.common.util.FragmentStackUtil;
import com.example.common.util.MyToast;
import com.example.picshare.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding viewBinding;
    private RegisterViewModel viewModel;

    public static RegisterFragment newInstance(){
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // 设置注册按钮事件监听
        viewBinding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = viewBinding.usernameEditText.getText().toString();
                String password = viewBinding.passwordEditText.getText().toString();
                String confirmPassword = viewBinding.passwordConfirmEditText.getText().toString();
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(requireContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
                }else{
                    viewModel.register(username, password);
                }
            }
        });

        // 观察注册结果
        viewModel.registerRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                if (stringRetrofitResponse.code == HttpConstants.SUCCESS_STATUS){
                    // 如果成功
                    MyToast.ShowToast(requireActivity(), "Register success!");
                    FragmentStackUtil.popBackStack(requireActivity().getSupportFragmentManager());
                }else {
                    MyToast.ShowToast(requireActivity(), stringRetrofitResponse.msg);
                }
            }
        });
        return viewBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentStackUtil.navBack();
    }
}