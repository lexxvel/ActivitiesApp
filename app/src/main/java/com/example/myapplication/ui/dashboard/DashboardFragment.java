package com.example.myapplication.ui.dashboard;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ActivityAdapter;
import com.example.myapplication.databinding.FragmentDashboardBinding;
import com.example.myapplication.db.MyDbManager;
import com.example.myapplication.models.ActivityModel;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    RecyclerView recyclerView;
    ActivityAdapter activityAdapter;
    MyDbManager myDbManager;
    ArrayList<String> activity, date, time;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.recycleView);
        myDbManager = new MyDbManager(view.getContext());
        activity = new ArrayList<>();
        time = new ArrayList<>();
        date = new ArrayList<>();

        storeDataInArrays(view);

        activityAdapter = new ActivityAdapter(view.getContext(), activity, time, date);
        recyclerView.setAdapter(activityAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }

    void storeDataInArrays(View view) {
        myDbManager = new MyDbManager(view.getContext());
        Cursor cursor = myDbManager.readAddData();
        while (cursor.moveToNext()) {
            activity.add(cursor.getString(1));
            date.add(cursor.getString(2));
            time.add(cursor.getString(3));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}