package com.han.mvpdome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yatoooon on 2016/11/19.
 */

public class SearchHistoryOpenHelper extends SQLiteOpenHelper {
    public SearchHistoryOpenHelper(Context context) {
        super(context, "searchhistory.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table searchhistory" +
                "(_id integer primary key autoincrement , searchname varchar(20));");
        db.execSQL("create table cookbooksearchhistory" +
                "(_id integer primary key autoincrement , searchname varchar(20));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
