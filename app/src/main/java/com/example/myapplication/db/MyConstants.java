package com.example.myapplication.db;

public class MyConstants {
    public static final String INFO_TABLE_NAME = "info";
    public static final String ACTIVITIES_TABLE_NAME = "activities";
    public static final String _ID = "_id";
    public static final String ACTIVITY = "activity";
    public static final String TIME = "time";
    public static final String DATE = "date";
    public static final String DB_NAME = "my_db.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_INFO_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
            INFO_TABLE_NAME + " ( " + _ID + " INTEGER PRIMARY KEY," + ACTIVITY + " TEXT," +
            TIME + " INT(144)," + DATE + " TEXT)";
    public static final String TABLE_ACTIVITIES_STRUCTURE = "CREATE TABLE IF NOT EXISTS " +
            ACTIVITIES_TABLE_NAME + " ( " + _ID + " INTEGER PRIMARY KEY," + ACTIVITY + " TEXT, UNIQUE(activity))";

    public static final String TABLE_ACTIVITIES_FILLING = "INSERT INTO " + ACTIVITIES_TABLE_NAME + " (" + ACTIVITY + ") " +
            "VALUES ('Работа'), ('Сон'), ('Учёба'), ('Социальные сети'), ('Семья'), ('Друзья'), ('Дача'), ('Уборка'), ('Дорога')";

    public static final String DROP_TABLE_INFO = "DROP TABLE IF EXISTS" + INFO_TABLE_NAME;
    public static final String DROP_TABLE_ACTIVITIES = "DROP TABLE IF EXISTS" + ACTIVITIES_TABLE_NAME;

}
