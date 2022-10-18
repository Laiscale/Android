package com.example.common.ui.details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

public class ImageViewPagerAdapter extends RecyclerView.Adapter<ImageViewPagerAdapter.ViewPagerViewHolder> {
    private List<String> imageViews;

    public ImageViewPagerAdapter(List<String> imageViews){
        this.imageViews = imageViews;
    }

    @NonNull
    @Override
    public ImageViewPagerAdapter.ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.example.common.R.layout.layout_image_view_pager_item, parent, false);
        return new ViewPagerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewPagerAdapter.ViewPagerViewHolder holder, int position) {
        // 加载图片
        Glide.with(holder.imageView.getContext()).load(imageViews.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageViews.size();
    }

    public class ViewPagerViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public ViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(com.example.common.R.id.view_pager_image);
        }
    }
}
