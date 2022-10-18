package com.example.discover;

import android.os.Bundle;

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
import com.example.common.ui.details.PicDetailsFragment;
import com.example.common.listener.OnItemClickListener;
import com.example.common.listener.RecyclerViewItemListener;
import com.example.common.util.FragmentStackUtil;
import com.example.discover.databinding.FragmentDiscoverBinding;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {
    private FragmentDiscoverBinding viewBinding;
    private DiscoverViewModel viewModel;
    private List<PictureData> pictureDataList = new ArrayList<>();
    private PicRecyclerViewAdapter recyclerViewAdapter = new PicRecyclerViewAdapter(pictureDataList);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentDiscoverBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
        // 配置 Adapter
        viewBinding.discoverRecyclerView.setAdapter(recyclerViewAdapter);
        // 定义瀑布布局
        viewBinding.discoverRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        viewModel.getDiscoverPicList(1, 10, MyApp.getUserBean().id);
        //刷新界面
        viewBinding.smartRefresh.setEnableAutoLoadMore(true);
        viewBinding.smartRefresh.setRefreshHeader(new ClassicsHeader(requireContext()));
        viewBinding.smartRefresh.setRefreshFooter(new ClassicsFooter(requireContext()));

        viewBinding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.getDiscoverPicList(1, 10, MyApp.getUserBean().id);
                viewBinding.smartRefresh.finishRefresh();
            }
        });

        viewBinding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (viewModel.getHasMore()){
                    //如果还有数据就取数据
                    viewModel.getDiscoverPicList(viewModel.getCurrentPage() + 1, 10, MyApp.getUserBean().id);
                }else {
                    // 如果没有更多，停止加载动画
                    viewBinding.smartRefresh.finishLoadMore();
                }
            }
        });

        viewModel.discoverPicList.observe(getViewLifecycleOwner(), new Observer<ResponseData<PictureData>>() {
            @Override
            public void onChanged(ResponseData<PictureData> pictureDataResponseData) {
                // 结束刷新
                viewBinding.smartRefresh.finishLoadMore();
                viewBinding.smartRefresh.finishRefresh();
                // 如果data为null，就直接返回
                if (pictureDataResponseData == null) return;
                if (!pictureDataResponseData.records.isEmpty()){
                    if (viewModel.getCurrentPage() == 1){
                        //顶部下拉刷新内容列表
                        int size = pictureDataList.size();
                        pictureDataList.clear();
                        pictureDataList.addAll(0, pictureDataResponseData.records);
                        // 通知刷新
                        recyclerViewAdapter.notifyItemRangeChanged(0, size);
                    }else {
                        //底部上拉继续渲染列表内内容
                        pictureDataList.addAll(pictureDataResponseData.records);
                        // 通知刷新
                        recyclerViewAdapter.notifyItemRangeChanged((viewModel.getCurrentPage() - 1) * 10, pictureDataResponseData.size);
                    }
                }
            }
        });

        viewBinding.discoverRecyclerView.addOnItemTouchListener(new RecyclerViewItemListener(requireContext(), viewBinding.discoverRecyclerView, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 跳转到详情页
                FragmentStackUtil.addToMainFragment(requireActivity().getSupportFragmentManager(), PicDetailsFragment.newInstance(pictureDataList.get(position)), "PicDetail_Fragment_Tag", true, "PicDetail_Fragment");
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        return viewBinding.getRoot();
    }
}