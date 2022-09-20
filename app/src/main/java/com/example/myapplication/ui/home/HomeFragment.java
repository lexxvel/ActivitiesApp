package com.example.myapplication.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.db.MyDbManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    EditText hoursInput;
    EditText minutesInput;
    private MyDbManager myDbManager;
    AlertDialog dialog;
    TextView act;
    List<String> activities;
    String selectedActivity = "";
    int selectedActivityInt = -200; //костыль для автоматического выбора установленного значения

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnAdd = (Button) view.findViewById(R.id.buttonAdd);
        hoursInput = (EditText) view.findViewById(R.id.editTextHours);
        minutesInput = (EditText) view.findViewById(R.id.editTextMinutes);
        activities = new ArrayList<>();

        myDbManager = new MyDbManager(view.getContext());

        act = (TextView) view.findViewById(R.id.selectorr);
        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectActivity(view);
            }
        });

        //Обработка нажатия добавить - действия по записи
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (selectedActivity.equals("")) {
                    Toast.makeText(getContext(), "Необходимо выбрать активность", Toast.LENGTH_SHORT).show();
                } else {
                    //Получение и преобразование значений полей
                    String hoursString = hoursInput.getText().toString();
                    String minutesString = minutesInput.getText().toString();
                    int hours;
                    int minutes;

                    if (hoursString.equals("")) {
                        hours = 0;
                    } else {
                        hours = Integer.parseInt(hoursString);
                    }

                    if (minutesString.equals("")) {
                        minutes = 0;
                    } else {
                        minutes = Integer.parseInt(minutesString);
                    }

                    minutes += hours * 60;
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    LocalDateTime now = LocalDateTime.now();
                    System.out.println(dtf.format(now));

                    //Запись в БД
                    myDbManager.insertToDb(selectedActivity, minutes, dtf.format(now));
                    Toast.makeText(view.getContext(), "Активность записана", Toast.LENGTH_SHORT).show();

                    //Очистка полей
                    List<TextView> objectList = new ArrayList<>();
                    objectList.add(hoursInput);
                    objectList.add(minutesInput);

                    for (TextView tw : objectList) {
                        tw.setText("");
                    }
                    act.setText("Выберите активность");
                    selectedActivity = "";
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        myDbManager.openDb();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
        myDbManager.closeDb();
    }


    public void selectActivity(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        /*final CharSequence[] charSequence = new CharSequence[]{
                "Работа", "Учёба", "Социальные сети", "Семья", "Хобби", "Уборка"};*/

        storeActivitiesDataInArray();
        final CharSequence[] charSequence = activities.toArray(new CharSequence[0]);

        final String[] selectedElement = {""};

        builder.setSingleChoiceItems(charSequence, selectedActivityInt == -200 ? -1 : selectedActivityInt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println(charSequence[i].toString());
                        selectedElement[0] = charSequence[i].toString();
                        selectedActivityInt = i;
                    }
                })
                .setTitle("Выбор активности");

        builder
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Применить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (selectedElement[0].toString().equals("")) {
                            if (selectedActivityInt == -200) {
                                Toast.makeText(view.getContext(), "Выберите активность!", Toast.LENGTH_SHORT).show();
                                selectActivity(view);
                            } else {
                                selectedElement[0] = charSequence[selectedActivityInt].toString();
                                selectedActivity = selectedElement[0];
                                act.setText(selectedActivity);
                            }
                        } else {
                            System.out.println(selectedElement[0]);
                            selectedActivity = selectedElement[0];
                            act.setText(selectedActivity);
                        }
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    private void storeActivitiesDataInArray() {
        Cursor cursor;
        cursor = myDbManager.readAllActivitiesFromActivities();
        while (cursor.moveToNext()) {
            System.out.println(cursor.getString(0));
            activities.add(cursor.getString(0));
        }
    }
}