package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.models.ActivityModel;

import java.util.ArrayList;
import java.util.List;

public class MyDbManager {
    private Context context;
    private MyDbHelper myDbHelper;
    private SQLiteDatabase db;

    public MyDbManager(Context context) {
        this.context = context;
        myDbHelper = new MyDbHelper(context);
    }

    public void openDb() {
        db = myDbHelper.getWritableDatabase();
    }

    public void insertToDb(String activity, int time, String date) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.ACTIVITY, activity);
        cv.put(MyConstants.DATE, date);
        cv.put(MyConstants.TIME, time);
        db.insert(MyConstants.TABLE_NAME, null, cv);
    }

    public List<ActivityModel> getFromDb() {

        List<ActivityModel> list = new ArrayList<>();
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String activity1 = cursor.getString(1);
            int time1 = cursor.getInt(2);
            String date = cursor.getString(3);
            list.add(new ActivityModel(activity1, date, time1));
        }
        cursor.close();
        return list;
    }

    public Cursor readAddData() {
        db = myDbHelper.getWritableDatabase();
        Cursor cursor = db.query(MyConstants.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public void closeDb() {
        myDbHelper.close();
    }
}
