package com.example.common.bean;

public class CommentData {
    public String appKey;
    public int commentLevel;
    public String content;
    public String createTime;
    public long id;
    public long pUserId;
    public long parentCommentId;
    public long parentCommentUserId;
    public long replyCommentId;
    public long replyCommentUserId;
    public long shareId;
    public String userName;

    public CommentData(String content, String createTime, long id, String username) {
        this.content = content;
        this.createTime = createTime;
        this.id = id;
        this.userName = username;
    }
}
