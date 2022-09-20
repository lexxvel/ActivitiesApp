package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {

    Context context;
    ArrayList activity, time, date, percents;
    InfoAdapter activityAdapter;

    public InfoAdapter(Context context, ArrayList activity, ArrayList time, ArrayList date, ArrayList percents) {
        this.context = context;
        this.activity = activity;
        this.date = date;
        this.time = time;
        this.percents = percents;
    }

    @NonNull
    @Override
    public InfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.raw_example, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoAdapter.MyViewHolder holder, int position) {
        holder.tv_activity.setText(String.valueOf(activity.get(position)));
        holder.tv_date.setText(String.valueOf(date.get(position)));
        holder.tv_time.setText(String.valueOf(time.get(position)));
        holder.tv_percents.setText(String.valueOf(percents.get(position)));
    }

    @Override
    public int getItemCount() {
        return activity.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_activity, tv_date, tv_time, tv_percents;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_activity = itemView.findViewById(R.id.tv_activity);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_percents = itemView.findViewById(R.id.tv_percents);
        }
    }
}
