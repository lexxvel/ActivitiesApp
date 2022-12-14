package com.example.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {

    public MyDbHelper(@Nullable Context context) {
        super(context, MyConstants.DB_NAME, null, MyConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MyConstants.TABLE_INFO_STRUCTURE);
        sqLiteDatabase.execSQL(MyConstants.TABLE_ACTIVITIES_STRUCTURE);
        sqLiteDatabase.execSQL(MyConstants.TABLE_ACTIVITIES_FILLING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(MyConstants.DROP_TABLE_INFO);
        sqLiteDatabase.execSQL(MyConstants.DROP_TABLE_ACTIVITIES);
        onCreate(sqLiteDatabase);
    }
}
