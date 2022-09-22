package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.db.MyDbManager;

import java.util.ArrayList;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyViewHolder> {

    Context context;
    ArrayList activities;
    AlertDialog dialog;
    MyDbManager myDbManager;

    public ActivitiesAdapter(Context context, ArrayList activities) {
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.raw_activity, parent, false);
        return new ActivitiesAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_activities.setText(String.valueOf(activities.get(position)));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Вопрос")
                        .setMessage("Удалить активность '" + String.valueOf(activities.get(position)) + "'?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                myDbManager = new MyDbManager(context);
                                int status = myDbManager.deleteActivity(String.valueOf(activities.get(position)), context);
                                if (status == 1) {
                                    Toast.makeText(context, "Активность '" + String.valueOf(activities.get(position)) + "' удалена", Toast.LENGTH_SHORT).show();
                                    activities.remove(position);
                                    notifyItemRemoved(position);
                                    dialog.cancel();
                                } else {
                                    Toast.makeText(context, "При удалении произошла ошибка", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.cancel();
                            }
                        });
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView tv_activities;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_activities = itemView.findViewById(R.id.tv_activityList);
            cardView = itemView.findViewById(R.id.cardViewActivity);
        }
    }
}
