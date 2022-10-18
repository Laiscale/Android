package com.example.common.ui.details;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.common.MyApp;
import com.example.common.R;
import com.example.common.adapter.PicCommentRecyclerViewAdapter;
import com.example.common.bean.CommentData;
import com.example.common.bean.PictureData;
import com.example.common.bean.ResponseData;
import com.example.common.bean.RetrofitResponse;
import com.example.common.constants.HttpConstants;
import com.example.common.databinding.FragmentPicDetailsBinding;
import com.example.common.util.FragmentStackUtil;
import com.example.common.util.MyToast;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PicDetailsFragment extends Fragment {
    private FragmentPicDetailsBinding viewBinding;
    private PicDetailsViewModel viewModel;
    private PictureData pictureData;

    private List<ImageView> imageViewList = new LinkedList<>();
    ;
    private List<String> imageUrlList;
    private ImageViewPagerAdapter imageViewPagerAdapter;

    private List<CommentData> commentDataList = new ArrayList<>();
    private PicCommentRecyclerViewAdapter commentRecyclerViewAdapter = new PicCommentRecyclerViewAdapter(commentDataList);

    public static PicDetailsFragment newInstance(PictureData pictureData) {
        PicDetailsFragment picDetailsFragment = new PicDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("PictureData", pictureData);
        picDetailsFragment.setArguments(bundle);
        return picDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pictureData = (PictureData) getArguments().getSerializable("PictureData");
            imageUrlList = pictureData.imageUrlList;
            // 根据图片个数，创建 ImageView
            imageViewPagerAdapter = new ImageViewPagerAdapter(imageUrlList);
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewBinding = FragmentPicDetailsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(PicDetailsViewModel.class);

        // 获取详情
        viewModel.getPicDetails(pictureData.id, MyApp.getUserBean().id);

        // 获取一级评论列表
        viewModel.getFirstCommentList(1, pictureData.id, 10);

        // 适配一级评论列表
        viewBinding.commentRecyclerView.setAdapter(commentRecyclerViewAdapter);
        viewBinding.commentRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        // 显示页面信息
        updateViewDisplay();

        // 设置按钮监听
        setClickEvent();

        // 设置刷新头
        viewBinding.smartRefresh.setEnableAutoLoadMore(true);
        viewBinding.smartRefresh.setRefreshHeader(new ClassicsHeader(requireContext()));
        viewBinding.smartRefresh.setRefreshFooter(new ClassicsFooter(requireContext()));

        viewBinding.smartRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (viewModel.getHasMore()) {
                    viewModel.getFirstCommentList(viewModel.getCurrentPage(), pictureData.id, viewModel.getSize());
                } else {
                    // 如果没有更多，停止加载动画
                    viewBinding.smartRefresh.finishRefresh();
                }
            }
        });

        viewBinding.smartRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (viewModel.getHasMore()) {
                    viewModel.getFirstCommentList(viewModel.getCurrentPage() + 1, pictureData.id, viewModel.getSize());
                } else {
                    // 如果没有更多，停止加载动画
                    viewBinding.smartRefresh.finishLoadMore();
                }
            }
        });

        // 检测图片详情结果
        viewModel.picData.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<PictureData>>() {
            @Override
            public void onChanged(RetrofitResponse<PictureData> pictureDataRetrofitResponse) {
                // 更新数据源
                pictureData = pictureDataRetrofitResponse.data;
                updateViewDisplay();
            }
        });

        // 监听关注结果
        viewModel.focusRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                if (stringRetrofitResponse.code == HttpConstants.SUCCESS_STATUS) {
                    // 如果成功，就获取详情，然后获取likeId
                    viewModel.getPicDetails(pictureData.id, MyApp.getUserBean().id);
                } else {
                    MyToast.ShowToast(requireContext(), stringRetrofitResponse.msg);
                }
            }
        });

        // 监听取消关注结果
        viewModel.unFocusRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                if (stringRetrofitResponse.code == HttpConstants.SUCCESS_STATUS) {
                    // 如果成功，就获取详情，然后获取likeId
                    viewModel.getPicDetails(pictureData.id, MyApp.getUserBean().id);
                } else {
                    MyToast.ShowToast(requireContext(), stringRetrofitResponse.msg);
                }
            }
        });

        // 监听评论获取结果
        viewModel.firstCommentList.observe(getViewLifecycleOwner(), new Observer<List<CommentData>>() {
            @Override
            public void onChanged(List<CommentData> commentList) {
                if (commentDataList != null) {
                    commentDataList.addAll(0, commentList);
                    commentRecyclerViewAdapter.notifyItemRangeChanged(0, commentDataList.size());
                }
                viewBinding.smartRefresh.finishLoadMore();
                viewBinding.smartRefresh.finishRefresh();
            }
        });

        // 监听添加评论的结果
        viewModel.commentRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                if (stringRetrofitResponse.code == HttpConstants.SUCCESS_STATUS) {
                    // 如果成功，就往列表最后添加
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                    String createTime = simpleDateFormat.format(new Date());
                    CommentData commentData = new CommentData(viewBinding.releaseComment.getText().toString(), createTime, MyApp.getUserBean().id, MyApp.getUserBean().username);
                    commentDataList.add(commentData);
                    commentRecyclerViewAdapter.notifyItemRangeChanged(commentDataList.size() - 1, 1);
                    viewBinding.releaseComment.setText("");
                } else {
                    MyToast.ShowToast(requireContext(), stringRetrofitResponse.msg);
                }
            }
        });

        // 监听点赞的结果
        viewModel.likeRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                if (stringRetrofitResponse.code == HttpConstants.SUCCESS_STATUS) {
                    // 如果成功，就获取详情，然后获取likeId
                    viewModel.getPicDetails(pictureData.id, MyApp.getUserBean().id);
                } else {
                    MyToast.ShowToast(requireContext(), stringRetrofitResponse.msg);
                }
            }
        });

        // 监听取消点赞的结果
        viewModel.disLikeRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                if (stringRetrofitResponse.code == HttpConstants.SUCCESS_STATUS) {
                    // 如果成功，就获取详情，然后刷新picDetail
                    viewModel.getPicDetails(pictureData.id, MyApp.getUserBean().id);
                } else {
                    MyToast.ShowToast(requireContext(), stringRetrofitResponse.msg);
                }
            }
        });

        // 监听收藏的结果
        viewModel.favoriteRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                if (stringRetrofitResponse.code == HttpConstants.SUCCESS_STATUS) {
                    // 如果成功，就获取详情，然后刷新picDetail
                    viewModel.getPicDetails(pictureData.id, MyApp.getUserBean().id);
                } else {
                    MyToast.ShowToast(requireContext(), stringRetrofitResponse.msg);
                }
            }
        });

        // 监听取消收藏的结果
        viewModel.unFavoriteRes.observe(getViewLifecycleOwner(), new Observer<RetrofitResponse<String>>() {
            @Override
            public void onChanged(RetrofitResponse<String> stringRetrofitResponse) {
                if (stringRetrofitResponse.code == HttpConstants.SUCCESS_STATUS) {
                    // 如果成功，就获取详情，然后刷新picDetail
                    viewModel.getPicDetails(pictureData.id, MyApp.getUserBean().id);
                } else {
                    MyToast.ShowToast(requireContext(), stringRetrofitResponse.msg);
                }
            }
        });

        viewBinding.imageViewPager.setAdapter(imageViewPagerAdapter);

        viewBinding.imageViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        return viewBinding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentStackUtil.navBack();
    }

    private void setClickEvent() {
        // 监听关注按钮事件
        viewBinding.focusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewBinding.focusButton.getText().toString().equals("关注")) {
                    viewModel.focusUser(pictureData.pUserId, MyApp.getUserBean().id);
                } else {
                    viewModel.unFocusUser(pictureData.pUserId, MyApp.getUserBean().id);
                }
            }
        });
        // 监听发表评论按钮
        viewBinding.releaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = viewBinding.releaseComment.getText().toString();
                viewModel.releaseComment(comment, pictureData.id, MyApp.getUserBean().id, MyApp.getUserBean().username);
            }
        });
        // 监听返回按钮
        viewBinding.navBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentStackUtil.popBackStack(requireActivity().getSupportFragmentManager());
            }
        });
        // 监听点赞图标
        viewBinding.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pictureData.hasLike) {
                    viewModel.dislikePic(pictureData.likeId);
                } else {
                    viewModel.likePic(pictureData.id, MyApp.getUserBean().id);
                }
            }
        });
        // 监听收藏图标
        viewBinding.collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pictureData.hasCollect) {
                    viewModel.unFavoritePic(pictureData.collectId);
                } else {
                    viewModel.favoritePic(pictureData.id, MyApp.getUserBean().id);
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void updateViewDisplay() {
        viewBinding.detailUserName.setText(pictureData.username);
        viewBinding.describeTitle.setText(pictureData.title);
        viewBinding.describeContent.setText(pictureData.content);
        viewBinding.favoriteNumber.setText(String.valueOf(pictureData.likeNum));
        viewBinding.collectionNumber.setText(String.valueOf(pictureData.collectNum));
        viewBinding.createTime.setText((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(pictureData.collectNum)));
        if (pictureData.hasFocus) {
            viewBinding.focusButton.setText("取消关注");
        } else {
            viewBinding.focusButton.setText("关注");
        }

        if (pictureData.hasCollect){
            viewBinding.collection.setImageResource(R.drawable.ic_baseline_star_24);
        }else {
            viewBinding.collection.setImageResource(R.drawable.ic_baseline_star_outline_24);
        }

        if (pictureData.hasLike){
            viewBinding.favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
        }else {
            viewBinding.favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
    }

}