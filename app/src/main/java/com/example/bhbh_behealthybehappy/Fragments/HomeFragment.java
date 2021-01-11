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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bhbh_behealthybehappy.Activities.SearchActivity;
import com.example.bhbh_behealthybehappy.Models.HomeViewModel;
import com.example.bhbh_behealthybehappy.Models.ItemEntry;
import com.example.bhbh_behealthybehappy.Models.UserInfo;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Utils.MySP;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;

import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.DRINK;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.FOOD;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.SPORTS_ACTIVITY;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.USER_INFO;

public class HomeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    // Variables
    private HomeViewModel homeViewModel;

    private Button main_BTN_changeDate;
    private Button main_BTN_addDrink;
    private Button main_BTN_addActivity;
    private Button main_BTN_addFood;
    private TextView main_LBL_name;
    private TextView main_LBL_weight;
    private TextView main_LBL_progress;
    private TextView main_LBL_bmi;

    private UserInfo userInfo;
    private DatePickerDialog datePickerDialog;

    private HashMap<String, ItemEntry> itemEntryHashMap = new HashMap<>();

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

        loadInfo(root);

        generateItems();

        return root;

    }

    private void generateItems() {
//        itemEntryHashMap.put()
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

    private void loadInfo(View root) {
        Gson gson = new Gson();

        userInfo = gson.fromJson(MySP.getInstance().getString(USER_INFO, ""), UserInfo.class);

        if (userInfo == null) {
            main_LBL_name.setText(getActivity().getResources().getString(R.string.hello_none));
            main_LBL_weight.setText(getActivity().getResources().getString(R.string.weight_none));
            main_LBL_bmi.setText(getActivity().getResources().getString(R.string.weight_none));
        } else {
            String s;
            double weightD = (double) userInfo.getUserWeight();
            double heightD = (double) userInfo.getUserHeight();
            double bmi = weightD / Math.pow(heightD / 100, 2);
            s = getActivity().getResources().getString(R.string.hello) + " " +
                    userInfo.getUserName();
            main_LBL_name.setText(s);
            s = getActivity().getResources().getString(R.string.last_weight) + " " +
                    userInfo.getUserWeight() + "kg";
            main_LBL_weight.setText(s);
            s = getActivity().getResources().getString(R.string.bmi) + " " +
                    String.format("%.2f", bmi);
            main_LBL_bmi.setText(s);
        }

    }


    private void searchItem(View root, Button btn) {
        setButtons(false);
        Intent myIntent = new Intent(root.getContext(), SearchActivity.class);
        if(btn == main_BTN_addDrink)
            myIntent.putExtra(SearchActivity.SEARCH_ITEM, DRINK);
        else if (btn == main_BTN_addActivity)
            myIntent.putExtra(SearchActivity.SEARCH_ITEM, SPORTS_ACTIVITY);
        else
            myIntent.putExtra(SearchActivity.SEARCH_ITEM, FOOD);
        root.getContext().startActivity(myIntent);// Opens winner activity
        setButtons(true);
    }

    // Prevents info activity to open more than once
    private void setButtons(Boolean b) {
        main_BTN_addDrink.setEnabled(b);
        main_BTN_addActivity.setEnabled(b);
        main_BTN_addFood.setEnabled(b);
    }

    private void findViews(View root) {// Initializes views
        main_BTN_changeDate = root.findViewById(R.id.main_BTN_changeDate);
        main_BTN_addDrink = root.findViewById(R.id.main_BTN_addDrink);
        main_BTN_addActivity = root.findViewById(R.id.main_BTN_addActivity);
        main_BTN_addFood = root.findViewById(R.id.main_BTN_addFood);
        main_LBL_name = root.findViewById(R.id.main_LBL_name);
        main_LBL_weight = root.findViewById(R.id.main_LBL_weight);
        main_LBL_progress = root.findViewById(R.id.main_LBL_progress);
        main_LBL_bmi = root.findViewById(R.id.main_LBL_bmi);
    }

    private void showDatePickerDialog(View root) {
        datePickerDialog = new DatePickerDialog(root.getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR), (1 + Calendar.getInstance().get(Calendar.MONTH)),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + month + "/" + year;
        updateMain_LBL_date(date);
    }


    public void setCurrentDate() {
        String date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" +
                (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/"
                + Calendar.getInstance().get(Calendar.YEAR);
        main_BTN_changeDate.setText(date);
    }

    private void updateMain_LBL_date(String date) {
        main_BTN_changeDate.setText(date);
    }

}