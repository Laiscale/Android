package com.example.profile.like;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.bean.PictureData;
import com.example.common.bean.ResponseData;
import com.example.profile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LikeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikeFragment extends Fragment import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProvider;
        import androidx.recyclerview.widget.StaggeredGridLayoutManager;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import com.example.common.MyApp;
        import com.example.common.adapter.PicRecyclerViewAdapter;
        import com.example.common.bean.PictureData;
        import com.example.common.bean.ResponseData;
        import com.example.profile.R;
        import com.example.profile.databinding.FragmentLikeBinding;
        import com.example.profile.databinding.FragmentShareBinding;
        import com.example.profile.share.ShareViewModel;
        import com.scwang.smart.refresh.footer.ClassicsFooter;
        import com.scwang.smart.refresh.header.ClassicsHeader;
        import com.scwang.smart.refresh.layout.api.RefreshLayout;
        import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
        import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

        import java.util.ArrayList;
        import java.util.List;

public class LikeFragment extends Fragment {
    private FragmentLikeBinding viewBinding;
    private LikeViewModel viewModel;
    private List<PictureData> pictureDataList = new ArrayList<>();
    private PicRecyclerViewAdapter recyclerViewAdapter = new PicRecyclerViewAdapter(pictureDataList);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentLikeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(LikeViewModel.class);

        // 配置 Adapter
        viewBinding.likeRecyclerView.setAdapter(recyclerViewAdapter);
        // 定义瀑布布局
        viewBinding.likeRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        viewModel.getLikeList(1, 10, MyApp.getUserBean().id);

        viewBinding.smartRefresh.setEnableAutoLoadMore(true);
        viewBinding.smartRefresh.setRefreshHeader(new ClassicsHeader(requireContext()));
        viewBinding.smartRefresh.setRefreshFooter(new ClassicsFooter(requireContext()));

        viewBinding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (viewModel.isHasMore()){
                    viewModel.getLikeList(viewModel.getCurrentPage() + 1, 10, MyApp.getUserBean().id);
                }
            }
        });

        viewBinding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (viewModel.isHasMore()){
                    viewModel.getLikeList(viewModel.getCurrentPage() + 1, 10, MyApp.getUserBean().id);
                }
            }
        });

        viewModel.likePicList.observe(getViewLifecycleOwner(), new Observer<ResponseData<PictureData>>() {
            @Override
            public void onChanged(ResponseData<PictureData> pictureDataResponseData) {
                // 结束刷新
                viewBinding.smartRefresh.finishLoadMore();
                viewBinding.smartRefresh.finishRefresh();
                // 如果data为null，就直接返回
                if (pictureDataResponseData == null) return;

                if (!pictureDataResponseData.records.isEmpty()){
                    if (viewModel.getCurrentPage() == 1){
                        pictureDataList.addAll(0, pictureDataResponseData.records);
                        // 通知刷新
                        recyclerViewAdapter.notifyItemRangeChanged(0, pictureDataResponseData.size);
                    }else {
                        pictureDataList.addAll(pictureDataResponseData.records);
                        // 通知刷新
                        recyclerViewAdapter.notifyItemRangeChanged((viewModel.getCurrentPage() - 1) * 10, pictureDataResponseData.size);
                    }
                }
            }
        });
        return viewBinding.getRoot();
    }
}