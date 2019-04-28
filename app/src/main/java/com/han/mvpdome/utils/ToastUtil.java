package com.han.mvpdome.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 */
//
public class ToastUtil {
    private static Toast mToast;

    public static void showShortToast(final Context context, final String message){
        if (mToast == null){
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }else{
            mToast.setText(message);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void showLongToast(final Context context, final int messageResId){
        if (mToast == null){
            mToast = Toast.makeText(context, messageResId, Toast.LENGTH_LONG);
        }else{
            mToast.setText(messageResId);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public static void cancleToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}