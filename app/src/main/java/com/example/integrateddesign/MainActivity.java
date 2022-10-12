package com.example.integrateddesign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.common.util.ActivityUtil;
import com.example.discover.DiscoverFragment;
import com.example.home.HomeFragment;
import com.example.integrateddesign.databinding.ActivityMainBinding;
import com.example.profile.ProfileFragment;
import com.example.release.ReleaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding viewBinding;
    private final String[] tabList = {"首页", "发现", "发布", "我的"};
    private final int[] iconActiveList = {
            R.drawable.home_active,
            R.drawable.discover_active,
            R.drawable.edit_active,
            R.drawable.profile_active
    };
    private final int[] iconInactiveList = {
            R.drawable.home_inactive,
            R.drawable.discover_inactive,
            R.drawable.edit_inactive,
            R.drawable.profile_inactive
    };
    private final Fragment[] fragments = {
            new HomeFragment(),
            new DiscoverFragment(),
            new ReleaseFragment(),
            new ProfileFragment()
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ActivityUtil().hideSystemStatusBar(this);
        super.onCreate(savedInstanceState);
        // 设置 ViewBinding
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        // 初始化底部导航栏
        bottomNavigationInit();
    }

    private void bottomNavigationInit(){
        // 配置 viewPager
        viewBinding.viewPager.setUserInputEnabled(false);
        viewBinding.viewPager.setOffscreenPageLimit(fragments.length);
        viewBinding.viewPager.setAdapter(new TabLayoutAdapter(fragments, getSupportFragmentManager(), getLifecycle()));

        // 配置 TabLayout
        new TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager, (tab, position) -> {
            tab.setText(tabList[position]);
            tab.setIcon(iconInactiveList[position]);
        }).attach();

        // 设置默认点击的图标
        Objects.requireNonNull(viewBinding.tabLayout.getTabAt(0)).setIcon(iconActiveList[0]);

        changeIconImgBottomMargin(viewBinding.tabLayout, 0);

        viewBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 通过设置缩放，来达到动画的效果
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(tab.view, "scaleX", 1f, 1.2f, 1f);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(tab.view, "scaleY", 1f, 1.2f, 1f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.setDuration(300);
                animatorSet.play(animator1).with(animator2);
                animatorSet.start();
                // 切换为选中图标
                tab.setIcon(iconActiveList[tab.getPosition()]);
                changeIconImgBottomMargin(viewBinding.tabLayout, 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 切换为未选中图标
                tab.setIcon(iconInactiveList[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                changeIconImgBottomMargin(viewBinding.tabLayout, 0);
            }
        });
    }

    // 改变底部导航栏图标的上下边距
    private void changeIconImgBottomMargin(ViewGroup parent, int px){
        for (int i = 0; i < parent.getChildCount(); i++){
            View view = parent.getChildAt(i);
            if (view instanceof ImageView){
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                layoutParams.bottomMargin = px;
                layoutParams.topMargin = px;
            }else if (view instanceof ViewGroup){
                changeIconImgBottomMargin((ViewGroup) view, px);
            }
        }
    }
}