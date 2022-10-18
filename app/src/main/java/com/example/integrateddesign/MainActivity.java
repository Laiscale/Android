package com.example.integrateddesign;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.common.util.ActivityUtil;
import com.example.common.util.FragmentStackUtil;
import com.example.picshare.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ActivityUtil().hideSystemStatusBar(this);
        super.onCreate(savedInstanceState);
        // 设置 ViewBinding
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        FragmentStackUtil.addToMainFragment(getSupportFragmentManager(), LoginFragment.newInstance(), "Login_Fragment_Tag", false, null);
        setContentView(viewBinding.getRoot());
    }
}