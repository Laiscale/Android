package com.example.common.util;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.common.R;

import java.util.Stack;

public class FragmentStackUtil {
    private static Fragment currentFragment = null;
    private static Stack<Fragment> fragmentStack = new Stack<>();

    public static void popBackStack(Fragment fragment){
        popBackStack(fragment.getParentFragmentManager());
    }

    public static void popBackStack(FragmentManager fragmentManager){
        if (isFragmentStackEmpty(fragmentManager)) return ;
        fragmentManager.popBackStack();
    }

    public static void popBackStack(FragmentActivity fragmentActivity){
        popBackStack(fragmentActivity.getSupportFragmentManager());
    }

    private static boolean isFragmentStackEmpty(FragmentManager fragmentManager){
        return fragmentManager.getBackStackEntryCount() == 0;
    }


    public static void addToMainFragment(FragmentManager fragmentManager, Fragment fragment, String tag, @NonNull boolean addToStack, String stackName){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 如果当前有fragment，就先将其隐藏
        if (!fragmentStack.empty()){
            fragmentTransaction.hide(fragmentStack.peek());
        }
        fragmentTransaction.add(R.id.main_fragment_container, fragment, tag);
        if (addToStack){
            fragmentTransaction.addToBackStack(stackName);
        }
        fragmentStack.push(fragment);
        fragmentTransaction.commit();
    }

    public static void replaceMainFragment(FragmentManager fragmentManager, Fragment fragment, String tag, @NonNull boolean addToStack, String stackName){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment, tag);
        fragmentStack.pop();
        fragmentStack.push(fragment);
        if (addToStack){
            fragmentTransaction.addToBackStack(stackName);
        }
        fragmentTransaction.commit();
    }

    public static void navBack(){
        fragmentStack.pop();
    }
}
