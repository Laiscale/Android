package com.example.common.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.WindowInsets;

public final class ActivityUtil {
    public void hideSystemStatusBar(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            activity.getWindow().getDecorView().getWindowInsetsController().hide(WindowInsets.Type.statusBars());
        }else {
            activity.getWindow().getDecorView().setSystemUiVisibility(activity.getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
}
