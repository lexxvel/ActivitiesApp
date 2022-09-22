package com.example.myapplication.ui.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ActivitiesAdapter;
import com.example.myapplication.adapters.InfoAdapter;
import com.example.myapplication.databinding.FragmentActivitiesBinding;
import com.example.myapplication.db.MyDbManager;

import java.util.ArrayList;

public class ActivitiesFragment extends Fragment {
    private FragmentActivitiesBinding binding;
    MyDbManager myDbManager;
    ArrayList<String> activities;
    RecyclerView recyclerViewActivities;
    ActivitiesAdapter activitiesAdapter;
    Button buttonAdd;
    EditText newActivityName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivitiesViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ActivitiesViewModel.class);

        binding = FragmentActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        activities = new ArrayList<>();
        myDbManager = new MyDbManager(view.getContext());
        recyclerViewActivities = view.findViewById(R.id.recycleViewActivities);
        buttonAdd = view.findViewById(R.id.btn_addActivity);
        newActivityName = view.findViewById(R.id.editTextActivityName);

        //Отображение активностей
        storeActivitiesDataInArrays();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String activity = "";
                activity = newActivityName.getText().toString();
                if (activity.equals("")) {
                    Toast.makeText(getContext(), "Не введена активность", Toast.LENGTH_SHORT).show();
                } else {
                    myDbManager.insertActivityToDb(activity, view.getContext());
                    newActivityName.setText("");
                    activities.clear();
                    storeActivitiesDataInArrays();
                }
            }
        });

    }

    private void storeActivitiesDataInArrays() {
        Cursor cursor;
        cursor = myDbManager.readAllActivitiesFromActivities();
        while (cursor.moveToNext()) {
            System.out.println(cursor.getString(0));
            activities.add(cursor.getString(0));
        }
        activitiesAdapter = new ActivitiesAdapter(getContext(), activities);
        recyclerViewActivities.setAdapter(activitiesAdapter);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
