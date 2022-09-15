package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.db.MyDbManager;
import com.example.myapplication.models.ActivityModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    EditText hoursInput;
    EditText minutesInput;
    AutoCompleteTextView activityCombo;
    private MyDbManager myDbManager;
    ArrayAdapter adapterActivitiesRowList, adapterActivitiesCombo;
    ListView activityList;

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
        activityCombo = (AutoCompleteTextView) view.findViewById(R.id.dropDownActivity);
        activityList = (ListView) view.findViewById(R.id.activityListView);

        myDbManager = new MyDbManager(view.getContext());

        //Заполнение комбика активностями
        String [] ACTIVITIES = new String[] {"Работа", "Учёба", "Спорт", "Социальные сети", "Уборка", "Семья"};
        List<String> activitiesList = Arrays.asList(ACTIVITIES);
        adapterActivitiesCombo = new ArrayAdapter<String>(view.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, activitiesList);
        activityCombo.setAdapter(adapterActivitiesCombo);

        //Обработка нажатия добавить - действия по записи
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Получение значений полей
                String hoursString = hoursInput.getText().toString();
                String minutesString = minutesInput.getText().toString();
                String activity = activityCombo.getText().toString();
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

                //Запись в БД
                myDbManager.insertToDb(activity, minutes, "01.01.2001");
                Toast.makeText(view.getContext(), "Активность записана", Toast.LENGTH_SHORT).show();

                //Получение и вывод всех данных из БД
                adapterActivitiesRowList = new ArrayAdapter<ActivityModel>(view.getContext(),
                        android.R.layout.simple_list_item_1,
                        myDbManager.getFromDb());
                activityList.setAdapter(adapterActivitiesRowList);

                /*activityCombo.setText("");
                hoursInput.setText("");
                minutesInput.setText("");*/

                List<TextView> objectList = new ArrayList<>();
                objectList.add(activityCombo);
                objectList.add(hoursInput);
                objectList.add(minutesInput);

                for (TextView tw : objectList) {
                    tw.setText("");
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
        binding = null;
        myDbManager.closeDb();
    }


}