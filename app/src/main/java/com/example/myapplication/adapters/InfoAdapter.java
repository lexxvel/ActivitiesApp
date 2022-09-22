package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.db.MyDbManager;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {

    Context context;
    ArrayList activity, time, date, percents;
    InfoAdapter infoAdapter;
    ActivityMainBinding binding;
    AlertDialog dialog;
    MyDbManager myDbManager;
    IInfoRecycler mListener;
    String pickedDate;

    public InfoAdapter(Context context, ArrayList activity, ArrayList time, ArrayList date, ArrayList percents, String pickedDate, IInfoRecycler mListener) {
        this.context = context;
        this.activity = activity;
        this.date = date;
        this.time = time;
        this.percents = percents;
        this.mListener = mListener;
        this.pickedDate = pickedDate;
    }

    @NonNull
    @Override
    public InfoAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.raw_example, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_activity.setText(String.valueOf(activity.get(position)));
        holder.tv_date.setText(String.valueOf(date.get(position)));

        int totalTime = Integer.parseInt(String.valueOf(time.get(position)));
        int totalTimeMinutes = totalTime % 60;
        int totalTimeHours = (totalTime - totalTimeMinutes) / 60;

        holder.tv_time.setText(String.valueOf(totalTimeHours) + "ч " + String.valueOf(totalTimeMinutes) + "мин.");
        holder.tv_percents.setText(String.valueOf(percents.get(position)));

        holder.cardViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 myDbManager = new MyDbManager(view.getContext());
        EditText editText = new EditText(view.getContext());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setText(String.valueOf(time.get(position)));
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                })
                .setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myDbManager.deleteActivityInfo(String.valueOf(activity.get(position)), String.valueOf(date.get(position)), view.getContext());

                        activity.remove(position);
                        time.remove(position);
                        date.remove(position);
                        percents.remove(position);
                        dialog.cancel();
                        mListener.storeInfoDataInArrays(pickedDate);
            }
                })
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newTime = editText.getText().toString();
                        if (newTime.equals(String.valueOf(time.get(position)))) {
                            Toast.makeText(view.getContext(), "Значение не было изменено, сохранение не требуется", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        } else {
                            int status = myDbManager.updateActivityInfo(String.valueOf(activity.get(position)), String.valueOf(date.get(position)), newTime, view.getContext());
                            if (status == 1) {
                                Toast.makeText(view.getContext(), "Запись изменена", Toast.LENGTH_SHORT).show();
                                time.set(position, Integer.parseInt(newTime));
                                notifyItemChanged(position);
                                dialog.cancel();
                                mListener.storeInfoDataInArrays(pickedDate);
                            } else {
                                Toast.makeText(view.getContext(), "Произошла ошибка при изменении записи", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setTitle("Редактировать активность")
                .setMessage("Редактирование активности " + String.valueOf(activity.get(position)) + ", за " + String.valueOf(date.get(position)))
                .setView(editText);

        dialog = builder.create();
        dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return activity.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_activity, tv_date, tv_time, tv_percents;
        CardView cardViewInfo;
        IInfoRecycler mListener;

        public MyViewHolder(@NonNull View itemView, IInfoRecycler mListener) {
            super(itemView);
            this.mListener = mListener;
            tv_activity = itemView.findViewById(R.id.tv_activity);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_percents = itemView.findViewById(R.id.tv_percents);
            cardViewInfo = itemView.findViewById(R.id.cardViewInfo);
        }
    }

    public interface IInfoRecycler {
        @RequiresApi(api = Build.VERSION_CODES.O)
        void storeInfoDataInArrays(String pickedDate);
    }
}
