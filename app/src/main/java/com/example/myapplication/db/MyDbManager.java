package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

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

        Cursor cursor = db.query(MyConstants.INFO_TABLE_NAME, null, "activity = '" + activity + "' AND date = '" + date + "'", null, null, null, null);
        List<ActivityModel> listOfRows = new ArrayList<>();
        int timerow = 0;
        int count = cursor.getCount();
        while (cursor.moveToNext()) {
            String activityrow = cursor.getString(1);
            timerow = cursor.getInt(2);
            String daterow = cursor.getString(3);
             listOfRows = new ArrayList<>();
            listOfRows.add(new ActivityModel(activityrow, daterow, timerow));
        }
        if (cursor.getCount() > 0) {
            time += timerow;
            cv.put(MyConstants.TIME, time);
            db.update(MyConstants.INFO_TABLE_NAME, cv, "activity=? AND date=?", new String[] {activity, date});
        } else {
            cv.put(MyConstants.TIME, time);
            db.insert(MyConstants.INFO_TABLE_NAME, null, cv);
        }
        cursor.close();
    }

    public List<ActivityModel> getFromDb() {

        List<ActivityModel> list = new ArrayList<>();
        Cursor cursor = db.query(MyConstants.INFO_TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String activity1 = cursor.getString(1);
            int time1 = cursor.getInt(2);
            String date = cursor.getString(3);
            list.add(new ActivityModel(activity1, date, time1));
        }
        cursor.close();
        return list;
    }

    public Cursor readAddData(String date) {
        db = myDbHelper.getWritableDatabase();
        Cursor cursor = db.query(MyConstants.INFO_TABLE_NAME, null, "date like '" + date + "'", null, null, null, "time DESC");
        return cursor;
    }

    public Cursor readAddTime(String date) {
        db = myDbHelper.getWritableDatabase();
        Cursor cursor = db.query(MyConstants.INFO_TABLE_NAME, new String[]{"time"}, "date like '" + date + "'", null, null, null, "time DESC");
        return cursor;
    }

    public void closeDb() {
        myDbHelper.close();
    }

    public Cursor readAllActivitiesFromActivities() {
        db = myDbHelper.getWritableDatabase();
        return db.query(MyConstants.ACTIVITIES_TABLE_NAME, new String[]{MyConstants.ACTIVITY}, null, null, null, null, null);
    }

    public void insertActivityToDb(String activity, Context context) {
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.ACTIVITY, activity);

        Cursor cursor = db.query(MyConstants.ACTIVITIES_TABLE_NAME, null, "activity like '" + activity + "'", null, null, null, null);
        List<ActivityModel> listOfRows = new ArrayList<>();
        int count = cursor.getCount();
        if (cursor.getCount() > 0) {
            Toast.makeText(context, "Активность уже существует", Toast.LENGTH_SHORT).show();
        } else {
            db.insert(MyConstants.ACTIVITIES_TABLE_NAME, null, cv);
        }
        cursor.close();
    }

    public void deleteActivityInfo(String activity, String date, Context context) {
        db = myDbHelper.getWritableDatabase();
        int a = db.delete(MyConstants.INFO_TABLE_NAME, MyConstants.ACTIVITY + "=? and " + MyConstants.DATE + "=?", new String[]{activity, date});
        System.out.println(a);
    }

    public int updateActivityInfo(String activity, String date, String time, Context context) {
        db = myDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MyConstants.TIME, Integer.valueOf(time));
        return db.update(MyConstants.INFO_TABLE_NAME, cv, MyConstants.ACTIVITY + "=? and " + MyConstants.DATE + "=?", new String[]{activity, date});
    }

    public int deleteActivity(String activity, Context context) {
        db = myDbHelper.getWritableDatabase();
        return db.delete(MyConstants.ACTIVITIES_TABLE_NAME, MyConstants.ACTIVITY + "=?", new String[]{activity});
    }
}
