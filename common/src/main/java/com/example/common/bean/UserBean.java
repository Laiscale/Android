package com.example.common.bean;

import java.io.Serializable;

public class UserBean implements Serializable {
    public String appKey;
    public String avatar;
    public long createTime;
    public long id;
    public String introduce;
    public long lastUpdateTime;
    public String password;
    public int sex;
    public String username;

    public UserBean(String appKey, String avatar, long createTime, long id, String introduce, long lastUpdateTime, String password, int sex, String username) {
        this.appKey = appKey;
        this.avatar = avatar;
        this.createTime = createTime;
        this.id = id;
        this.introduce = introduce;
        this.lastUpdateTime = lastUpdateTime;
        this.password = password;
        this.sex = sex;
        this.username = username;
    }
}
