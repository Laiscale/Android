package com.example.release;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.common.adapter.PicRecyclerViewAdapter;
import com.example.common.bean.PictureData;

import java.util.List;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ImageRecyclerViewHolder>{
    private List<String> imageList;

    public ImageRecyclerViewAdapter(List<String> imageList){
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageRecyclerViewAdapter.ImageRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_item, parent, false);
        return new ImageRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageRecyclerViewAdapter.ImageRecyclerViewHolder holder, int position) {
        if (position == imageList.size() - 1){
            holder.imageView.setImageResource(R.drawable.add);
            holder.imageView.setBackgroundResource(R.drawable.add_picture_bg);
        }else {
            Glide.with(holder.imageView.getContext()).load(imageList.get(position)).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageRecyclerViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ImageRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_added);
        }
    }
}
