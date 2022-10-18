package com.example.common.bean;

public class FirstCommentBean {
    public String content;
    public long shareId;
    public long userId;
    public String userName;

    public FirstCommentBean(String content, long shareId, long userId, String username) {
        this.content = content;
        this.shareId = shareId;
        this.userId = userId;
        this.userName = username;
    }
}
