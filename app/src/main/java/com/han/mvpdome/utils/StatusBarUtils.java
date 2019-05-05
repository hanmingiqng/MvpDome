package com.han.mvpdome.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


import com.han.mvpdome.R;

import java.lang.reflect.Field;


/**
 * @author liyuanli
 * @data 2018/7/30
 */

public class StatusBarUtils {
    /**
     * 布局中必须有一个id为status_bar的view来设置状态栏背景
     * 必须要在 setContentView之后调用
     *
     * @param activity 当前页面
     */
    public static void setTranslateStatusBar(Activity activity) {
        // 4.4以上处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // android
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 状态栏透明
            View status_bar = activity.findViewById(R.id.status_bar);// 标题栏id
            if (status_bar != null) {
                ViewGroup.LayoutParams params = status_bar.getLayoutParams();
                params.height = getStatusBarHeight(activity);
                status_bar.setLayoutParams(params);
            }
        }
        //5.0 以上处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    /**
     * Flag只有在使用了FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
     * 并且没有使用 FLAG_TRANSLUCENT_STATUS的时候才有效，也就是只有在状态栏全透明的时候才有效。
     *
     * @param activity
     * @param bDark
     */
    public static void setStatusBarMode(Activity activity, boolean bDark) {
        //6.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (bDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }

        }
    }


    public static void statusBarSetting(Activity activity) {
        View view = getStatusBarView(activity);
        if (view == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4 状态栏
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.GRAY);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0 全透明实现
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        DeviceUtil.setStatusBarLightMode(activity.getWindow(), getDarkOrLight(), view);
    }

    public static boolean getDarkOrLight() {
        return true;
    }

    public static View getStatusBarView(Activity activity) {
        View view = activity.findViewById(R.id.status_bar);
        if (view == null) {
            return null;
        }
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        view.setLayoutParams(llp);
        return view;
    }

    /**
     * 获取状态栏高度——方法1
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //获取status_bar_height资源的ID
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return statusBarHeight;
    }

    public static int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        if (null == context) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;//默认为38，貌似大部分是这样的

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

}
