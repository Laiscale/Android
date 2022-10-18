package com.example.profile.share;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.common.bean.RetrofitResponse;
import com.example.common.constants.HttpConstants;
import com.example.common.ui.details.PicDetailsFragment;
import com.example.common.listener.OnItemClickListener;
import com.example.common.listener.RecyclerViewItemListener;
import com.example.common.util.FragmentStackUtil;
import com.example.common.util.MyToast;
import com.example.profile.databinding.FragmentShareBinding;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class ShareFragment extends Fragment {
    private FragmentShareBinding viewBinding;
    private ShareViewModel viewModel;
    private List<PictureData> pictureDataList = new ArrayList<>();
    private PicRecyclerViewAdapter recyclerViewAdapter = new PicRecyclerViewAdapter(pictureDataList);
    private int deletePosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentShareBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ShareViewModel.class);

        // 配置 Adapter
        viewBinding.shareRecyclerView.setAdapter(recyclerViewAdapter);
        // 定义瀑布布局
        viewBinding.shareRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        viewModel.getShareList(1, 10, MyApp.getUserBean().id);

        viewBinding.smartRefresh.setEnableAutoLoadMore(true);
        viewBinding.smartRefresh.setRefreshHeader(new ClassicsHeader(requireContext()));
        viewBinding.smartRefresh.setRefreshFooter(new ClassicsFooter(requireContext()));

        viewBinding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.getShareList(1, 10, MyApp.getUserBean().id);
                viewBinding.smartRefresh.finishRefresh();
            }
        });

        viewBinding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (viewModel.isHasMore()){
                    viewModel.getShareList(viewModel.getCurrentPage() + 1, 10, MyApp.getUserBean().id);
                }else {
                    // 如果没有更多，停止加载动画
                    viewBinding.smartRefresh.finishLoadMore();
                }
            }
        });

        viewModel.sharePicList.observe(getViewLifecycleOwner(), new Observer<ResponseData<PictureData>>() {
            @Override
            public void onChanged(ResponseData<PictureData> pictureDataResponseData) {
                // 结束刷新
                viewBinding.smartRefresh.finishLoadMore();
                viewBinding.smartRefresh.finishRefresh();
                // 如果data为null，就直接返回
                if (pictureDataResponseData == null) return;

                if (!pictureDataResponseData.records.isEmpty()){
                    if (viewModel.getCurrentPage() == 1){
                        int size = pictureDataList.size();
                        pictureDataList.clear();
                        pictureDataList.addAll(0, pictureDataResponseData.records);
                        // 通知刷新
                        recyclerViewAdapter.notifyItemRangeChanged(0, size);
                    }else {
                        pictureDataList.addAll(pictureDataResponseData.records);
                        // 通知刷新
                        recyclerViewAdapter.notifyItemRangeChanged((viewModel.getCurrentPage() - 1) * 10, pictureDataResponseData.size);
                    }
                }
            }
        });

        // 检测删除的结果
        viewModel.deleteRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                if (stringRetrofitResponse.code == HttpConstants.SUCCESS_STATUS){
                    // 将删除的图片，在本地的列表中删除，并刷新
                    pictureDataList.remove(deletePosition);
                    viewBinding.shareRecyclerView.setAdapter(recyclerViewAdapter);
                    recyclerViewAdapter.notifyItemRangeChanged(0, pictureDataList.size());
                }else {
                    MyToast.ShowToast(requireContext(), stringRetrofitResponse.msg);
                }
            }
        });

        // 设置对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("提示:");
        // 设置按钮的点击事件
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 删除操作
                viewModel.deleteShare(pictureDataList.get(deletePosition).id, MyApp.getUserBean().id);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        viewBinding.shareRecyclerView.addOnItemTouchListener(new RecyclerViewItemListener(requireContext(), viewBinding.shareRecyclerView, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 跳转到详情页
                FragmentStackUtil.addToMainFragment(requireActivity().getSupportFragmentManager(), PicDetailsFragment.newInstance(pictureDataList.get(position)), "PicDetail_Fragment_Tag", true, "PicDetail_Fragment");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // 保存选择的item位置
                deletePosition = position;
                builder.setMessage("是否删除分享: " + pictureDataList.get(position).title);
                AlertDialog dialog = builder.create();
                // 显示删除对话框
                dialog.show();
            }
        }));
        return viewBinding.getRoot();
    }
}