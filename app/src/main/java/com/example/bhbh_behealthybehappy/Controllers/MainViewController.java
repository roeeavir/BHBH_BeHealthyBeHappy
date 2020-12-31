package com.example.bhbh_behealthybehappy.Controllers;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.example.bhbh_behealthybehappy.Activities.SearchActivity;


public class MainViewController { // Main Activity Controller Class

    // Variables
    private Context context;

    DatePickerDialog datePickerDialog;


    public MainViewController(Context context) {
        this.context = context;
        findViews();
        initViews();

    }


    private void initViews() {

    }


    private void searchItem(Button btn){
        btn.setEnabled(false);// Prevents info activity to open more than once
        Intent myIntent = new Intent(context, SearchActivity.class);
        context.startActivity(myIntent);// Opens winner activity
        btn.setEnabled(true);
    }

    private void findViews() {// Initializes views

    }

//    private void showDatePickerDialog() {
//        datePickerDialog = new DatePickerDialog(((MainActivity) context),
//                ((MainActivity) context),
//                Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
//                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.show();
//    }

}


