package com.han.mvpdome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yatoooon on 2017/9/28.
 */

//创建数据库和表
public class CacheHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "cache.db";
    private static final int DB_VERSION = 1;
    static final String CACHE_TABLE = "cache";

    public CacheHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + CACHE_TABLE +
                " (urlKey text, params text, value text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + CACHE_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }
}
