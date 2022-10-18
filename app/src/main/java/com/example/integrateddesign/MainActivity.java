package com.example.picshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.common.util.ActivityUtil;
import com.example.common.util.FragmentStackUtil;
import com.example.discover.DiscoverFragment;
import com.example.home.HomeFragment;
import com.example.picshare.databinding.ActivityMainBinding;
import com.example.picshare.login.LoginFragment;
import com.example.profile.ProfileFragment;
import com.example.release.ReleaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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