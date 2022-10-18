package com.example.IntegratedDesign;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.IntegratedDesign.databinding.FragmentMainBinding;
import com.example.common.base.BaseFragment;
import com.example.common.util.FragmentStackUtil;
import com.example.discover.DiscoverFragment;
import com.example.home.HomeFragment;
import com.example.profile.ProfileFragment;
import com.example.release.ReleaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class MainFragment extends BaseFragment {
    private FragmentMainBinding viewBinding;

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

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentMainBinding.inflate(inflater, container, false);
        // 配置 viewPager
        viewBinding.viewPager.setUserInputEnabled(false);
        viewBinding.viewPager.setOffscreenPageLimit(fragments.length);
        viewBinding.viewPager.setAdapter(new TabLayoutAdapter(fragments, requireActivity().getSupportFragmentManager(), getLifecycle()));

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
        return viewBinding.getRoot();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentStackUtil.navBack();
    }
}