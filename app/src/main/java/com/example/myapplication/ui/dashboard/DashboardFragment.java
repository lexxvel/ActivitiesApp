package com.example.myapplication.ui.dashboard;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.InfoAdapter;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.db.MyDbManager;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class DashboardFragment extends Fragment implements DatePickerDialog.OnDateSetListener, InfoAdapter.IInfoRecycler {

    private FragmentDashboardBinding binding;
    RecyclerView recyclerView;
    InfoAdapter activityAdapter;
    MyDbManager myDbManager;
    ArrayList<String> activity, date, time, percents;
    private TextView pickedDateTw;
    View view;
    DateTimeFormatter dtf;
    LocalDateTime now;
    String pickedDate = "";
    TextView tv_totalMunutesAndHours;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        this.view = view;
        recyclerView = view.findViewById(R.id.recycleView);
        pickedDateTw = view.findViewById(R.id.pickedDate);
        myDbManager = new MyDbManager(view.getContext());
        activity = new ArrayList<>();
        time = new ArrayList<>();
        date = new ArrayList<>();
        percents = new ArrayList<>();

        dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        now = LocalDateTime.now();
        pickedDateTw.setText(dtf.format(now));

        pickedDate = "";
        storeInfoDataInArrays(null);

        activityAdapter = new InfoAdapter(view.getContext(), activity, time, date, percents, pickedDate, this);
        recyclerView.setAdapter(activityAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //слушатель нажатия кнопки выбора даты
        view.findViewById(R.id.show_dialogPickDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

    }

    /**
     * Открытие диалогового окна календаря
     * @param view View
     */
    private void showDatePickerDialog(View view) {

        //Условие на наличие выбранной даты для передачи выбранной даты в календарь
        if (!pickedDate.equals("")) {

            String day1 = null;
            String month1 = null;
            String year1 = null;

            String regex = "[.]+";
            String[] arr = pickedDate.split(regex);
            for (String uniqueValue : arr) {
                if (day1 == null) {
                    day1 = uniqueValue;
                } else if (month1 == null) {
                    month1 = uniqueValue;
                } else if (year1 == null) {
                    year1 = uniqueValue;
                } else {
                    System.out.println("До данного шага не доходим.");
                }
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    view.getContext(),
                    this,
                    Integer.parseInt(year1),
                    Integer.parseInt(month1)-1,
                    Integer.parseInt(day1)
            );
            datePickerDialog.show();
        } else {
            //Передача текущей даты в календарь, открытие диалогового окна выбора даты
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    view.getContext(),
                    this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        month += 1;

        if (month < 10) {
            if (dayOfMonth < 10) {
                pickedDate = "0" + dayOfMonth + ".0" + month + "." + year;
            } else {
                pickedDate = dayOfMonth + ".0" + month + "." + year;
            }
        } else {
            if (dayOfMonth < 10) {
                pickedDate = "0" + dayOfMonth + "." + month + "." + year;
            } else {
                pickedDate = dayOfMonth + "." + month + "." + year;
            }
        }
        //Запись выбранной даты в TextView
        pickedDateTw.setText(pickedDate);

        //Запрос в БД и применение фильтра
        storeInfoDataInArrays(pickedDate);
    }


    /**
     * Получение данных за выбранную/текущую дату. Заполняет массивы ArrayList<String> activity, date, time
     * @param pickedDate Выбранная дата, если имеется. Если её нет передавать "null"
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void storeInfoDataInArrays(String pickedDate) {
        //Очистка записей грида перед применением фильтра
        activity.clear();
        date.clear();
        time.clear();
        percents.clear();

        myDbManager = new MyDbManager(view.getContext());
        now = LocalDateTime.now();
        Cursor cursor;
        Cursor timeCursor;
        if (pickedDate == null || pickedDate.equals("")) {
            cursor = myDbManager.readAddData(dtf.format(now));
            timeCursor = myDbManager.readAddTime(dtf.format(now));
        } else {
            cursor = myDbManager.readAddData(pickedDate);
            timeCursor = myDbManager.readAddTime(pickedDate);
        }
        int totalTime = 0;
        while (timeCursor.moveToNext()) {
            System.out.println(timeCursor.getString(0));
            totalTime += Integer.parseInt(timeCursor.getString(0));
        }
        while (cursor.moveToNext()) {
            activity.add(cursor.getString(1));
            String cursorTime = cursor.getString(2);
            time.add(cursorTime);
            date.add(cursor.getString(3));
            DecimalFormat df = new DecimalFormat("###.##");
            String persentCounted = String.valueOf(df.format((double) (Integer.parseInt(cursorTime) * 100) / totalTime)).toString();
            percents.add(persentCounted);
        }

        int totalTimeMinutes = totalTime % 60;
        int totalTimeHours = (totalTime - totalTimeMinutes) / 60;

        tv_totalMunutesAndHours = view.findViewById(R.id.tv_totalMunutesAndHours);
        tv_totalMunutesAndHours.setText(String.valueOf(totalTimeHours) + "ч " + String.valueOf(totalTimeMinutes) + "мин.");

        activityAdapter = new InfoAdapter(view.getContext(), activity, time, date, percents, pickedDate, this);
        recyclerView.setAdapter(activityAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}