package com.han.mvpdome.db;

/**
 * Created by yatoooon on 2016/11/19.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class SearchHistoryDao {
    private SearchHistoryOpenHelper searchHistoryOpenHelper;
    private Context context;


    private SearchHistoryDao(Context context){
        this.context = context;
        searchHistoryOpenHelper = new SearchHistoryOpenHelper(context);
    }
    private static SearchHistoryDao appLockDao = null;
    public static SearchHistoryDao getInstance(Context context){
        if(appLockDao == null){
            appLockDao = new SearchHistoryDao(context);
        }
        return appLockDao;
    }

    public void insert(String searchname){
        SQLiteDatabase db = searchHistoryOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("searchname", searchname);

        db.insert("searchhistory", null, contentValues);


        db.close();

    }
    public void delete(String searchname){
        SQLiteDatabase db = searchHistoryOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("searchname", searchname);

        db.delete("searchhistory", "searchname = ?", new String[]{searchname});

        db.close();

    }
    public ArrayList<String> findAll(){
        SQLiteDatabase db = searchHistoryOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("searchhistory", new String[]{"searchname"}, null, null, null, null, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor.moveToNext()){
            list.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return list;
    }


    public void insertcookbook(String searchname){
        SQLiteDatabase db = searchHistoryOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("searchname", searchname);

        db.insert("cookbooksearchhistory", null, contentValues);


        db.close();

    }
    public void deletecookbook(String searchname){
        SQLiteDatabase db = searchHistoryOpenHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("searchname", searchname);

        db.delete("cookbooksearchhistory", "searchname = ?", new String[]{searchname});

        db.close();

    }
    public ArrayList<String> findAllcookbook(){
        SQLiteDatabase db = searchHistoryOpenHelper.getWritableDatabase();
        Cursor cursor = db.query("cookbooksearchhistory", new String[]{"searchname"}, null, null, null, null, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor.moveToNext()){
            list.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return list;
    }


}
