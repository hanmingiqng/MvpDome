/**
 * DeviceUtil.java
 * ImageChooser
 * <p/>
 * Created by likebamboo on 2014-4-24
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

package com.han.mvpdome.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.han.mvpdome.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 与设备相关的工具类
 *
 * @author likebamboo
 */
public class DeviceUtil {

    private DeviceUtil() {

    }

    /**
     * 获取屏幕的宽高
     *
     * @return
     */
    public static Point getDeviceSize(Context ctx) {
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        Point size = new Point();
        size.x = dm.widthPixels;
        size.y = dm.heightPixels;
        return size;
    }

    /**
     * 获取手机的SDK版本
     *
     * @return SDK版本
     */
    public static int getVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机的系统版本
     *
     * @return 系统版本
     */
    public static String getVersionCode() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机的品牌、型号
     *
     * @return 手机品牌型号
     */
    public static String getTelBrandAndModel() {
        return Build.BRAND + " " + Build.MODEL;
    }

    public static String getVersionName(Context mContext) {
        // 获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVersionCode(Context mContext) {
        // 获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            int version = packInfo.versionCode;
            return version + "";
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0";
    }


    /**
     * 获取配置文件中的META_DATA数据
     *
     * @param mContext
     * @param key
     * @return
     */
    public static String getMetaData(Context mContext, String key) {
        String CHANNELID = "fzbx";
        if (TextUtils.isEmpty(key)) {
            key = "UMENG_CHANNEL";
        }
        try {
            ApplicationInfo ai;
            ai = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                CHANNELID = value.toString();
            }
        } catch (Exception e) {
//            LogUtil.e("getMetaData()", e.getMessage());
        }
        return CHANNELID;
    }

    /**
     * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
     * <p/>
     * 渠道标志为： 1，andriod（a）
     * <p/>
     * 识别符来源标志： 1， wifi mac地址（wifi）； 2， IMEI（imei）； 3， 序列号（sn）； 4，
     * id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
     *
     * @param context 上下文
     * @return 唯一标识
     */
    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");

        try {
            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            assert wifi != null;
            WifiInfo info = wifi.getConnectionInfo();
            @SuppressLint("HardwareIds") String wifiMac = info.getMacAddress();
            if (!TextUtils.isEmpty(wifiMac)) {
                deviceId.append("wifi");
                deviceId.append(wifiMac);
            }

            // IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                return deviceId.toString();
            }

            // 序列号（sn）
            @SuppressLint("MissingPermission") String sn = tm.getSimSerialNumber();
            if (!TextUtils.isEmpty(sn)) {
                deviceId.append("sn");
                deviceId.append(sn);
                return deviceId.toString();
            }

            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (!TextUtils.isEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }

        return deviceId.toString();
    }

    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context) {
        SharedPreferences mShare = context.getSharedPreferences("sysCacheMap", Context.MODE_PRIVATE);
        SharedPreferences.Editor saveEditor = mShare.edit();

        String uuid = mShare.getString("uuid", "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            saveEditor.putString("uuid", uuid);
            saveEditor.commit();
        }
        return uuid;
    }

    /**
     * 隐藏输入法
     */
    public static void hideSoftKeyboard(Context paramContext, View paramEditText) {
        ((InputMethodManager) paramContext.getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    /**
     * 显示输入法
     */
    public static void showSoftKeyborad(Context paramContext, EditText paramEditText) {
        ((InputMethodManager) paramContext.getSystemService(Context.INPUT_METHOD_SERVICE)).
                showSoftInput(paramEditText, 0);
    }

    /**
     * 跳转浏览器
     *
     * @param mContext 上下文对象
     * @param url      url地址
     */
    public static void jump2Browser(Context mContext, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        mContext.startActivity(intent);
    }


    public static boolean setStatusBarLightMode(Window window, boolean dark, View statusBar) {
        boolean result = false;

        if (FlymeSetStatusBarLightMode(window, dark)) {
            result = true;
        } else if (MIUISetStatusBarLightMode(window, dark)) {
            result = true;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                result = true;
            } else {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && dark) {
            if (statusBar != null) {
                statusBar.setBackgroundColor(statusBar.getContext().getResources().getColor(R.color.color_646368));
            }
        }
        return result;
    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }


    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    }
                }

            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 判断SDCard是否可用
     */
    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断手机是否开启相机权限（针对锤子、魅族部分手机在没开启相机权限的时候也能正常调用相机）
     */

    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters(); //针对魅族手机
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

}
