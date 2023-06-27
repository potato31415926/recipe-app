package com.yuzule.recipe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Recipe";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表格的 SQL 语句
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (phone TEXT, password TEXT)";
        db.execSQL(createTableSQL);
        // Create table if it doesn't exist
        db.execSQL("CREATE TABLE IF NOT EXISTS suggestions(title TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}