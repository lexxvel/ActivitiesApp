package com.example.myapplication.models;

public class ActivityModel {
    private String activity;
    private String date;
    private int time;

    public ActivityModel(String activity, String date, int time) {
        this.activity = activity;
        this.date = date;
        this.time = time;
    }

    @Override
    public String toString() {
        return "ActivityModel{" +
                "activity='" + activity + '\'' +
                ", date='" + date + '\'' +
                ", time=" + time +
                '}';
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
