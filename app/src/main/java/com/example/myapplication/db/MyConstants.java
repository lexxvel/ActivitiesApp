package com.example.myapplication.db;

public class MyConstants {
    public  static final String TABLE_NAME = "info";
    public  static final String _ID = "_id";
    public  static final String ACTIVITY = "activity";
    public  static final String TIME = "time";
    public  static final String DATE = "date";
    public  static final String DB_NAME = "my_db.db";
    public  static final int DB_VERSION = 1;
    public  static final String TABLE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " ( " + _ID + " INTEGER PRIMARY KEY," + ACTIVITY + " TEXT," +
            TIME + " INT(144)," + DATE + " TEXT)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME;

}
