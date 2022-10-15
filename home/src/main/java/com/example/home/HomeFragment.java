package com.example.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.adapter.ViewPagerAdapter;
import com.example.common.util.FragmentStackUtil;
import com.example.home.databinding.FragmentHomeBinding;
import com.example.home.favorite.FavoriteFragment;
import com.example.home.follow.FollowFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding viewBinding;
    private String[] titles = {"关注", "收藏"};
    private Fragment[] fragments;

    public HomeFragment(){
        fragments = new Fragment[]{
                new FollowFragment(),
                new FavoriteFragment()
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false);
        viewBinding.viewPager.setAdapter(new ViewPagerAdapter(fragments, requireActivity().getSupportFragmentManager(), getLifecycle()));
        // 将 viewpager 与 tabLayout 联系起来
        new TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();

        return viewBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentStackUtil.navBack();
    }
}