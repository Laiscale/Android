package com.example.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.common.adapter.ViewPagerAdapter;
import com.example.common.util.FragmentStackUtil;
import com.example.profile.databinding.FragmentProfileBinding;
import com.example.profile.edit.EditDataFragment;
import com.example.profile.like.LikeFragment;
import com.example.profile.save.SaveFragment;
import com.example.profile.share.ShareFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding viewBinding;
    private String[] titles = {"分享", "保存", "喜欢"};
    private Fragment[] fragments;

    public ProfileFragment(){
        fragments = new Fragment[] {
                new ShareFragment(),
                new SaveFragment(),
                new LikeFragment()
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false);
        // 实在 ViewPager与TabLayout
        viewBinding.viewPager.setAdapter(new ViewPagerAdapter(fragments, requireActivity().getSupportFragmentManager(), getLifecycle()));
        // 将 viewpager 与 tabLayout 联系起来
        new TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();

        // 显示个人信息
        Glide.with(requireContext()).load(MyApp.getUserBean().avatar).into(viewBinding.imageAvater);
        viewBinding.userName.setText(MyApp.getUserBean().username);
        viewBinding.userId.setText(String.valueOf(MyApp.getUserBean().id));
        viewBinding.individualitySignature.setText(MyApp.getUserBean().introduce);
        if (MyApp.getUserBean().sex == 0){
            viewBinding.sexImage.setImageResource(R.drawable.boy);
        }else {
            viewBinding.sexImage.setImageResource(R.drawable.girl);
        }

        // 监听编辑资料的按钮
        viewBinding.editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDataFragment editDataFragment = EditDataFragment.newInstance(MyApp.getUserBean());
                FragmentStackUtil.addToMainFragment(requireActivity().getSupportFragmentManager(), editDataFragment, "Edit_Data_Fragment_Tag", true, "Edit_Data_Fragment");
            }
        });
        return viewBinding.getRoot();
    }
}