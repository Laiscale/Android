package com.example.release;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageListAdapter extends BaseAdapter {
    private final List<String> imageList;

    public ImageListAdapter(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(convertView.getContext()).inflate(R.layout.layout_image_item, parent, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.image_added);
        Glide.with(convertView.getContext()).load(imageList.get(position)).into(imageView);

//        if (!imageList.get(position).equals("emptyUrl")){
//            Glide.with(convertView.getContext()).load(imageList.get(position)).into(imageView);
//        }else {
//            imageView.setImageResource(R.drawable.add);
//            imageView.setBackgroundResource(R.drawable.add_picture_bg);
//        }
        return view;
    }
}
