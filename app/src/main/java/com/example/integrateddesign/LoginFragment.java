package com.example.integrateddesign;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.common.MyApp;
import com.example.common.bean.RetrofitResponse;
import com.example.common.bean.UserBean;
import com.example.common.constants.HttpConstants;
import com.example.common.util.FragmentStackUtil;
import com.example.common.util.MyToast;
import com.example.picshare.MainFragment;
import com.example.picshare.R;
import com.example.picshare.databinding.FragmentLoginBinding;
import com.example.picshare.register.RegisterFragment;

import java.util.Objects;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding viewBinding;
    private LoginViewModel viewModel;

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // 点击事件
        viewBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = viewBinding.usernameEditText.getText().toString();
                String password = viewBinding.passwordEditText.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    MyToast.ShowToast(requireActivity(), "username or password is empty");
                    return;
                }
                viewModel.login(username, password);
            }
        });

        viewBinding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 跳转到注册页面
                FragmentStackUtil.addToMainFragment(requireActivity().getSupportFragmentManager(), RegisterFragment.newInstance(), "Register_Fragment_Tag", true, "Register_Fragment");
            }
        });

        viewModel.loginRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<UserBean>>() {
            @Override
            public void onChanged(RetrofitResponse<UserBean> userBeanRetrofitResponse) {
                if (userBeanRetrofitResponse.code == HttpConstants.SUCCESS_STATUS){
                    // 如果成功
                    MyApp.setUserBean(userBeanRetrofitResponse.data);
                    MyToast.ShowToast(requireActivity(), "Login success!");
                    FragmentStackUtil.addToMainFragment(requireActivity().getSupportFragmentManager(), MainFragment.newInstance(), "Main_Fragment_Tag", true, "Main_Fragment");
                }else {
                    MyToast.ShowToast(requireActivity(), userBeanRetrofitResponse.msg);
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