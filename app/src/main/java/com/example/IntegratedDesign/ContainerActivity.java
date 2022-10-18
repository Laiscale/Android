package com.example.IntegratedDesign;

import android.os.Bundle;

import com.example.IntegratedDesign.databinding.ActivityContainerBinding;
import com.example.common.util.ActivityUtil;
import com.example.common.util.FragmentStackUtil;
import androidx.appcompat.app.AppCompatActivity;


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