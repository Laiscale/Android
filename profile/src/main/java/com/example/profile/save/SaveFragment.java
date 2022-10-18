package com.example.profile.save;

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
import android.widget.Toast;

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
import com.example.profile.databinding.FragmentSaveBinding;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class SaveFragment extends Fragment {
    private FragmentSaveBinding viewBinding;
    private SaveViewModel viewModel;
    private List<PictureData> pictureDataList = new ArrayList<>();
    private PicRecyclerViewAdapter recyclerViewAdapter = new PicRecyclerViewAdapter(pictureDataList);
    private int changePosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentSaveBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SaveViewModel.class);

        // 配置 Adapter
        viewBinding.saveRecyclerView.setAdapter(recyclerViewAdapter);
        // 定义瀑布布局
        viewBinding.saveRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        viewModel.getSaveList(1, 10, MyApp.getUserBean().id);

        viewBinding.smartRefresh.setEnableAutoLoadMore(true);
        viewBinding.smartRefresh.setRefreshHeader(new ClassicsHeader(requireContext()));
        viewBinding.smartRefresh.setRefreshFooter(new ClassicsFooter(requireContext()));

        viewBinding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.getSaveList(1, 10, MyApp.getUserBean().id);
                viewBinding.smartRefresh.finishRefresh();
            }
        });

        viewBinding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (viewModel.isHasMore()){
                    viewModel.getSaveList(viewModel.getCurrentPage() + 1, 10, MyApp.getUserBean().id);
                }else {
                    // 如果没有更多，停止加载动画
                    viewBinding.smartRefresh.finishLoadMore();
                }
            }
        });

        // 检测获取的列表数据结果
        viewModel.savePicList.observe(getViewLifecycleOwner(), new Observer<ResponseData<PictureData>>() {
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

        // 检测改变状态的结果
        viewModel.changeRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                if (stringRetrofitResponse.code == HttpConstants.SUCCESS_STATUS){
                    // 将改变的图片，在本地的列表中删除，并刷新
                    pictureDataList.remove(changePosition);
                    viewBinding.saveRecyclerView.setAdapter(recyclerViewAdapter);
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
        builder.setPositiveButton("分享", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PictureData pictureData = pictureDataList.get(changePosition);
                // 删除操作
                viewModel.changToShare(pictureData.content, pictureData.id, pictureData.imageCode, pictureData.pUserId, pictureData.title);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });



        viewBinding.saveRecyclerView.addOnItemTouchListener(new RecyclerViewItemListener(requireContext(), viewBinding.saveRecyclerView, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 跳转到详情页
                FragmentStackUtil.addToMainFragment(requireActivity().getSupportFragmentManager(), PicDetailsFragment.newInstance(pictureDataList.get(position)), "PicDetail_Fragment_Tag", true, "PicDetail_Fragment");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // 保存选择的item位置
                changePosition = position;
                builder.setMessage("是否分享: " + pictureDataList.get(position).title);
                AlertDialog dialog = builder.create();
                // 显示对话框
                dialog.show();
            }
        }));

        return viewBinding.getRoot();
    }
}