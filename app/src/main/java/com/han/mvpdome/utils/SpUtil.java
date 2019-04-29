package com.han.mvpdome.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.han.mvpdome.AppConstant;
import com.han.mvpdome.beans.LoginBean;

import java.util.HashSet;

import static android.content.Context.MODE_APPEND;

/**
 * 轻量级数据存储工具类
 */
public class SpUtil {

    private static SharedPreferences sp;
    private static SpUtil instance;
    private static Editor editor;

    private static final String SP_NAME = AppConstant.SpConstants.USER_INFO;
    private static final String TOKEN = "TOKEN";


    @SuppressLint("WrongConstant")
    private SpUtil(Context mContext) {
        if (null == mContext) {
            throw new NullPointerException("此类没有进行初始化");
        }

        if (null == sp) {
            sp = mContext.getSharedPreferences(SP_NAME, MODE_APPEND);
        }
    }

    public static SpUtil getInstance(Context mContext) {
        if (null == instance) {
            synchronized (SpUtil.class) {
                if (null == instance) {
                    instance = new SpUtil(mContext);
                }
            }
        }
        return instance;
    }

    public void setToken(String token) {
        sp.edit().putString(TOKEN, token).apply();
    }

    public String getToken() {
        return sp.getString(TOKEN, null);
    }

    /**
     * 存储Sp
     *
     * @param key   key键
     * @param value 值
     */
    public void saveSpString(String key, String value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    /**
     * 获取Sp 的String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 返回取到的值，没有取到返回默认值
     */
    public String getSpString(String key, String defaultValue) {
        if (key != null && !key.equals("")) {
            return sp.getString(key, defaultValue);
        }
        return null;
    }

    public void putLongValue(String key, long value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putLong(key, value);

            editor.commit();
        }
    }

    public long getLongValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getLong(key, 0);
        }
        return 0;
    }

    public void putIntValue(String key, int value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public int getIntValue(String key, int defaultValue) {
        if (key != null && !key.equals("")) {
            return sp.getInt(key, defaultValue);
        }
        return 0;
    }

    public void putBooleanValue(String key, boolean value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        if (key != null && !key.equals("")) {
            return sp.getBoolean(key, defaultValue);
        }
        return true;
    }

    public void putFloatValue(String key, Float value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putFloat(key, value);
            editor.commit();
        }
    }

    public float getFloatValue(String key, float defaultValue) {
        if (key != null && !key.equals("")) {
            return sp.getFloat(key, defaultValue);
        }
        return 0;
    }

    public void saveStringSet(String key, HashSet<String> set) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putStringSet(key, set);
            editor.commit();
        }

    }

    public HashSet<String> getStringSet(String key, HashSet<String> var2) {
        if (key != null && !key.equals("")) {
            return (HashSet<String>) sp.getStringSet(key, var2);
        }
        return null;

    }


    public void cleanStringValue(String key) {
        if (key != null) {
            editor = sp.edit();
            editor.putString(key, null);
            editor.commit();
        }
    }

    public void removeValue(String key) {

        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.remove(key);
            editor.commit();
        }
    }

    public void removeAll() {
        editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 清空 SharedPreferences 中信息。
     *
     * @param context 应用程序上下文环境
     */
    @SuppressLint("WrongConstant")
    public void clear(Context context) {
        if (null == context) {
            return;
        }
        SharedPreferences pref = context.getSharedPreferences(SP_NAME, MODE_APPEND);
        Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    //保存用户信息
    public LoginBean.DataBean getUserInfo() {
        LoginBean.DataBean userInfoBean = new Gson().fromJson(sp.getString(AppConstant.SpConstants.USER, ""), LoginBean.DataBean.class);
        return userInfoBean != null ? userInfoBean : new LoginBean.DataBean();
    }

    public void setUserInfo(LoginBean.DataBean userBean) {
        if (userBean != null) {
            sp.edit().putString(AppConstant.SpConstants.USER, new Gson().toJson(userBean)).apply();
        }
    }

}
