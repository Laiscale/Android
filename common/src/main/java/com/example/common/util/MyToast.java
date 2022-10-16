package com.example.common.util;

import android.content.Context;
import android.widget.Toast;

public class MyToast {
    public static void ShowToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
