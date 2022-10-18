package com.example.common.bean;

import java.io.Serializable;
import java.util.List;

public class PictureData implements Serializable {
    public long collectId;
    public int collectNum;
    public String content;
    public long createTime;
    public Boolean hasCollect;
    public Boolean hasFocus;
    public Boolean hasLike;
    public long id;
    public long imageCode;
    public int likeId;
    public int likeNum;
    public long pUserId;
    public String title;
    public String username;
    public List<String> imageUrlList;
}
