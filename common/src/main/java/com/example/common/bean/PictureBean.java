package com.example.common.bean;

public class PictureBean {
    public String content;
    public long id;
    public long imageCode;
    public long pUserId;
    public String title;

    public PictureBean(String content, long id, long imageCode, long pUserId, String title) {
        this.content = content;
        this.id = id;
        this.imageCode = imageCode;
        this.pUserId = pUserId;
        this.title = title;
    }

    public PictureBean(String content, long imageCode, long pUserId, String title) {
        this.content = content;
        this.imageCode = imageCode;
        this.pUserId = pUserId;
        this.title = title;
    }
}
