package com.example.common;

import android.app.Application;

import com.example.common.bean.UserBean;

public class MyApp extends Application {
    private static UserBean userBean;

    public static UserBean getUserBean() {
        return userBean;
    }

    public static void setUserBean(UserBean userBean) {
        MyApp.userBean = userBean;
    }
}

