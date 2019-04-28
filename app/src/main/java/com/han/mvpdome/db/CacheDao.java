package com.han.mvpdome.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.han.mvpdome.MyApplication;


/**
 * Created by yatoooon on 2017/9/28.
 */

public class CacheDao {
    private static volatile CacheDao cacheDao;

    private CacheHelper helper;
    private SQLiteDatabase db;

    private CacheDao(Context context) {
        helper = new CacheHelper(MyApplication.getApplication());
        db = helper.getWritableDatabase();
    }

    public static CacheDao getInstance(Context context) {
        if (cacheDao == null) {
            synchronized (CacheDao.class) {
                if (cacheDao == null) {
                    cacheDao = new CacheDao(context);
                }
            }
        }
        return cacheDao;
    }

    //查
    public String queryResponse(String urlKey, String params) {
        Cursor cursor = db.query("cache", new String[]{"value"}, "urlKey = ? and params=?", new String[]{urlKey, params}, null, null, null);
        while (cursor.moveToLast()) {
            if (cursor.isLast()) {
                String value = cursor.getString(0);
//                cursor.close();
//                db.close();
                return value;
            }
        }
//        cursor.close();
//        db.close();
        return null;
    }

    //增
    public void insertResponse(String urlKey, String params, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("urlKey", urlKey);
        contentValues.put("params", params);
        contentValues.put("value", value);

        String s = queryResponse(urlKey, params);
        if (null == s || s.isEmpty()) {
            db.insert("cache", null, contentValues);
        } else {
            updateResponse(urlKey, params, value);
        }
//        db.close();
    }

    //改
    public void updateResponse(String urlKey, String params, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("value", value);
        db.update("cache", contentValues, "urlKey = ? and  params = ?", new String[]{urlKey, params});
    }

    //删
    public void deleteResponse(String urlKey, String params) {
    }
}
