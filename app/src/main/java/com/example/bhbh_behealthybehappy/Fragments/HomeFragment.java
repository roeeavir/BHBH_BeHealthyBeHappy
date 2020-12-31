package com.example.bhbh_behealthybehappy.Fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bhbh_behealthybehappy.Activities.SearchActivity;
import com.example.bhbh_behealthybehappy.Models.HomeViewModel;
import com.example.bhbh_behealthybehappy.R;

import java.util.Calendar;

public class HomeFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private HomeViewModel homeViewModel;

    private Button main_BTN_changeDate, main_BTN_addDrink, main_BTN_addActivity, main_BTN_addFood;

    DatePickerDialog datePickerDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        findViews(root);
        initViews();

        setCurrentDate();

        return root;

    }

    private void initViews() {
        main_BTN_changeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        main_BTN_addDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem(v, main_BTN_addDrink);
            }
        });
        main_BTN_addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem(v, main_BTN_addActivity);
            }
        });
        main_BTN_addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem(v, main_BTN_addFood);
            }
        });
    }


    private void searchItem(View root, Button btn){
        btn.setEnabled(false);// Prevents info activity to open more than once
        Intent myIntent = new Intent(root.getContext(), SearchActivity.class);
        root.getContext().startActivity(myIntent);// Opens winner activity
        btn.setEnabled(true);
    }

    private void findViews(View root) {// Initializes views
        main_BTN_changeDate = root.findViewById(R.id.main_BTN_changeDate);
        main_BTN_addDrink = root.findViewById(R.id.main_BTN_addDrink);
        main_BTN_addActivity = root.findViewById(R.id.main_BTN_addActivity);
        main_BTN_addFood = root.findViewById(R.id.main_BTN_addFood);
    }

    private void showDatePickerDialog(View root) {
        datePickerDialog = new DatePickerDialog(root.getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + month + "/" + year;
        updateMain_LBL_date(date);
    }


    // Views update method
    public void setCurrentDate() {
        String date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" +
                Calendar.getInstance().get(Calendar.MONTH) + "/"
                + Calendar.getInstance().get(Calendar.YEAR);
        main_BTN_changeDate.setText(date);
    }


    private void updateMain_LBL_date(String date) {
        main_BTN_changeDate.setText(date);
    }
}