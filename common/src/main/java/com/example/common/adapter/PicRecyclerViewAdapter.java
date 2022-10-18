package com.example.common.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.common.R;
import com.example.common.bean.PictureData;

import java.util.List;

public class PicRecyclerViewAdapter extends RecyclerView.Adapter<PicRecyclerViewAdapter.PicRecyclerViewHolder> {
    private List<PictureData> pictureDataList;

    public PicRecyclerViewAdapter(List<PictureData> pictureDataList){
        this.pictureDataList = pictureDataList;
    }

    @NonNull
    @Override
    public PicRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_list_item, parent, false);
        return new PicRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicRecyclerViewHolder holder, int position) {
        PictureData pictureData = pictureDataList.get(position);
        holder.tv_title.setText(pictureData.title);
        holder.tv_favorite_number.setText(String.valueOf(pictureData.collectNum));
        holder.tv_username.setText(pictureData.username);
        // 根据是否已经收藏，来加载不同的图标
        if (pictureData.hasCollect){
            holder.iv_favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
        }else {
            holder.iv_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        // 根据是否有图片，来判断是否加载图片
        if (pictureData.imageUrlList.isEmpty()){
            holder.iv_image.setVisibility(View.GONE);
        }else {
            Glide.with(holder.iv_image.getContext()).load(pictureData.imageUrlList.get(0)).into(holder.iv_image);
        }
    }

    @Override
    public int getItemCount() {
        return pictureDataList.size();
    }

    public class PicRecyclerViewHolder extends RecyclerView.ViewHolder{
        public final ImageView iv_image;
        public final TextView tv_title;
        public final TextView tv_username;
        public final ImageView iv_favorite;
        public final TextView tv_favorite_number;

        public PicRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_username = itemView.findViewById(R.id.tv_username);
            iv_favorite = itemView.findViewById(R.id.iv_favorite);
            tv_favorite_number = itemView.findViewById(R.id.tv_favorite_number);
        }
    }
}

