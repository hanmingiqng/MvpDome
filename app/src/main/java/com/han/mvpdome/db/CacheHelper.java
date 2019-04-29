package com.han.mvpdome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.han.mvpdome.AppConstant;

/**
 * Created by yatoooon on 2017/9/28.
 */

//创建数据库和表
public class CacheHelper extends SQLiteOpenHelper {


    public CacheHelper(Context context) {
        super(context, AppConstant.Db.DB_NAME, null, AppConstant.Db.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + AppConstant.Db.CACHE_TABLE +
                " (urlKey text, params text, value text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + AppConstant.Db.CACHE_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }
}
