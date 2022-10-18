package com.example.picshare;

import android.os.Bundle;

import com.example.common.util.ActivityUtil;
import com.example.common.util.FragmentStackUtil;
import com.example.integrateddesign.LoginFragment;
import androidx.appcompat.app.AppCompatActivity;

import com.example.picshare.databinding.ActivityContainerBinding;

public class ContainerActivity extends AppCompatActivity {
    private ActivityContainerBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ActivityUtil().hideSystemStatusBar(this);
        super.onCreate(savedInstanceState);
        // 设置 ViewBinding
        viewBinding = ActivityContainerBinding.inflate(getLayoutInflater());
        FragmentStackUtil.addToMainFragment(getSupportFragmentManager(), LoginFragment.newInstance(), "Login_Fragment_Tag", false, null);
        setContentView(viewBinding.getRoot());
    }
}