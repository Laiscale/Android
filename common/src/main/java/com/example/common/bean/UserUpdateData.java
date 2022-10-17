package com.example.common.bean;

public class UserUpdateData {
   public String avatar;
   public long id;
   public String introduce;
   public int sex;
   public String username;

    public UserUpdateData(String avatar, long id, String introduce, int sex, String username) {
        this.avatar = avatar;
        this.id = id;
        this.introduce = introduce;
        this.sex = sex;
        this.username = username;
    }
}
