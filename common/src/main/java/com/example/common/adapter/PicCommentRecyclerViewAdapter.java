package com.example.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.R;
import com.example.common.bean.CommentData;

import java.util.List;

public class PicCommentRecyclerViewAdapter extends RecyclerView.Adapter<PicCommentRecyclerViewAdapter.PicCommentRecyclerViewHolder>{
    private List<CommentData> commentDataList;

    public PicCommentRecyclerViewAdapter(List<CommentData> commentDataList){
        this.commentDataList = commentDataList;
    }

    @NonNull
    @Override
    public PicCommentRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false);
        return new PicCommentRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicCommentRecyclerViewHolder holder, int position) {
        holder.tv_comment_context.setText(commentDataList.get(position).content);
        holder.tv_username.setText(commentDataList.get(position).userName);
        holder.tv_time.setText(commentDataList.get(position).createTime);
    }

    @Override
    public int getItemCount() {
        return commentDataList.size();
    }

    public class PicCommentRecyclerViewHolder extends RecyclerView.ViewHolder{
        public final TextView tv_time;
        public final TextView tv_username;
        public final TextView tv_comment_context;

        public PicCommentRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.commentTime);
            tv_username = itemView.findViewById(R.id.commentUserName);
            tv_comment_context = itemView.findViewById(R.id.commentContext);
        }
    }
}
