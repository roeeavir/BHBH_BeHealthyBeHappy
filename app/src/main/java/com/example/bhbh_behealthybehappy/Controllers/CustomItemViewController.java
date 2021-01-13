package com.example.bhbh_behealthybehappy.Controllers;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

import com.example.bhbh_behealthybehappy.Activities.CustomeItemActivity;
import com.example.bhbh_behealthybehappy.Models.ItemEntry;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;
import com.example.bhbh_behealthybehappy.Utils.FirebaseHelper;
import com.example.bhbh_behealthybehappy.Utils.MyHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomItemViewController {

    // Variables
    private Context context;

    private Enums.ITEM_THEME theme;
    private Enums.SCORE score;

    private MaterialButton item_BTN_save;
    private TextInputLayout item_EDT_name;
    private TextInputLayout item_EDT_weight;
    private TextInputLayout item_EDT_amount;
    private TextInputLayout item_EDT_calories;
    private TextInputLayout item_EDT_carbs;
    private TextInputLayout item_EDT_notes;
    private TextInputLayout item_EDT_time;
    private TextInputLayout item_EDT_caloriesPerHour;
    private ImageButton item_IMB_back;

    public CustomItemViewController(Context context) {
        this.context = context;

        findViews();
        initViews();

    }

    private void findViews() {
        item_EDT_name = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_name);
        item_EDT_weight = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_weight);
        item_EDT_amount = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_amount);
        item_EDT_calories = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_calories);
        item_EDT_carbs = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_carbs);
        item_EDT_notes = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_notes);
        item_EDT_time = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_time);
        item_EDT_caloriesPerHour = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_caloriesPerHour);
        item_BTN_save = ((CustomeItemActivity) context).findViewById(R.id.item_BTN_save);
        item_IMB_back = ((CustomeItemActivity) context).findViewById(R.id.item_IMB_back);
    }

    public void updateTheme(Enums.ITEM_THEME th) {
        theme = th;

        if (theme == Enums.ITEM_THEME.ACTIVITY) {
            item_EDT_weight.setVisibility(View.GONE);
            item_EDT_calories.setVisibility(View.GONE);
            item_EDT_carbs.setVisibility(View.GONE);
            item_EDT_time.setVisibility(View.VISIBLE);
            item_EDT_caloriesPerHour.setVisibility(View.VISIBLE);
        } else if (theme == Enums.ITEM_THEME.DRINK) {
            item_EDT_amount.setVisibility(View.VISIBLE);
            item_EDT_weight.setVisibility(View.GONE);
        }

        item_EDT_name.setHint(item_EDT_name.getHint() + " " + theme.toString());


    }

    private void initViews() {
        item_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();// Adds item into firebase realtime database
            }
        });

        item_IMB_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CustomeItemActivity) context).finish();// Quits activity
            }
        });
    }

    private void saveInfo() {
        if (checkInfo()) {
            String name;
            name = item_EDT_name.getEditText().getText().toString().substring(0, 1).toUpperCase()
                    + item_EDT_name.getEditText().getText().toString().substring(1);
            DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference("Items");
//            myRef.setValue(null);
            score = setScore();
            if (theme == Enums.ITEM_THEME.DRINK)
                myRef.child(theme.toString()).child(name).setValue(new ItemEntry().setName(name).setItemType(theme).
                        setAmount(Integer.parseInt(item_EDT_amount.getEditText().getText().toString()))
                        .setCarbs(Integer.parseInt(item_EDT_carbs.getEditText().getText().toString()))
                        .setScoreType(score).setNotes(item_EDT_notes.getEditText().getText().toString())
                        .setCalories(Integer.parseInt(item_EDT_calories.getEditText().getText().toString()))
                        .updateScore()
                );
            else if (theme == Enums.ITEM_THEME.ACTIVITY)
                myRef.child(theme.toString()).child(name).setValue(new ItemEntry().setName(name).setItemType(theme).
                        setTime(Integer.parseInt(item_EDT_time.getEditText().getText().toString()))
                        .setCaloriesPerHour(Integer.parseInt(item_EDT_caloriesPerHour.getEditText().getText().toString()))
                        .setScoreType(score).setNotes(item_EDT_notes.getEditText().getText().toString())
                        .updateScore()
                );
            else
                myRef.child(theme.toString()).child(name).setValue(new ItemEntry().setName(name).setItemType(theme).
                        setWeight(Integer.parseInt(item_EDT_weight.getEditText().getText().toString()))
                        .setCarbs(Integer.parseInt(item_EDT_carbs.getEditText().getText().toString()))
                        .setScoreType(score).setNotes(item_EDT_notes.getEditText().getText().toString())
                        .setCalories(Integer.parseInt(item_EDT_calories.getEditText().getText().toString()))
                        .updateScore()
                );
            MyHelper.getInstance().toast("Item has been added to database!");
        }
//        else
//            MyHelper.getInstance().toast("Some Variables seems to have bad inputs");

    }


    private boolean checkInfo() {
        if (item_EDT_name.getEditText().getText().toString().equals("")) {
            MyHelper.getInstance().toast("Item name is missing");
            return false;
        } else if (theme == Enums.ITEM_THEME.ACTIVITY)
            return checkActivityInfo();
        else if (theme == Enums.ITEM_THEME.DRINK)
            return checkDrinkInfo();
        else
            return checkFoodInfo();


    }

    private boolean checkDrinkInfo() {
        int amount, calories, carbs;
        if (item_EDT_amount.getEditText().getText().toString().equals("") ||
                item_EDT_calories.getEditText().getText().toString().equals("")) {
            MyHelper.getInstance().toast("Some Variables seems to be missing");
            return false;
        }
        amount = Integer.parseInt(item_EDT_amount.getEditText().getText().toString());
        calories = Integer.parseInt(item_EDT_calories.getEditText().getText().toString());
        if (item_EDT_carbs.getEditText().getText().toString().equals("")) {
            item_EDT_carbs.getEditText().setText("0");
            return amount > 0 && calories >= 0;
        } else {
            carbs = Integer.parseInt(item_EDT_carbs.getEditText().getText().toString());
            if (amount < carbs)
                return false;
            return amount > 0 && calories >= 0 && carbs >= 0;
        }
    }

    private boolean checkFoodInfo() {
        int weight, calories, carbs;
        if (item_EDT_weight.getEditText().getText().toString().equals("") ||
                item_EDT_calories.getEditText().getText().toString().equals("")) {
            MyHelper.getInstance().toast("Some Variables seems to be missing");
            return false;
        }
        weight = Integer.parseInt(item_EDT_weight.getEditText().getText().toString());
        calories = Integer.parseInt(item_EDT_calories.getEditText().getText().toString());
        if (item_EDT_carbs.getEditText().getText().toString().equals("")) {
            item_EDT_carbs.getEditText().setText("0");
            return weight > 0 && calories >= 0;
        } else {
            carbs = Integer.parseInt(item_EDT_carbs.getEditText().getText().toString());
            if (weight < carbs)
                return false;
            return weight > 0 && calories >= 0 && carbs >= 0;
        }
    }

    private boolean checkActivityInfo() {
        int time, caloriesPerHour;
        if (item_EDT_time.getEditText().getText().toString().equals("") ||
                item_EDT_caloriesPerHour.getEditText().getText().toString().equals("")) {
            MyHelper.getInstance().toast("Some Variables seems to be missing");
            return false;
        }
        time = Integer.parseInt(item_EDT_time.getEditText().getText().toString());
        caloriesPerHour = Integer.parseInt(item_EDT_caloriesPerHour.getEditText().getText().toString());
        return time > 0 && caloriesPerHour > 0;
    }

    private Enums.SCORE setScore() {
        if (theme == Enums.ITEM_THEME.ACTIVITY)
            return Enums.SCORE.GREEN_STAR;
        else if (!item_EDT_carbs.getEditText().getText().toString().equals("")) {
            int carbs = Integer.parseInt(item_EDT_carbs.getEditText().getText().toString());
            if (theme == Enums.ITEM_THEME.DRINK) {
                int amount = Integer.parseInt(item_EDT_amount.getEditText().getText().toString());
                if (((double) carbs / amount) > 0.1)
                    return Enums.SCORE.BLACK_HEART;
                else
                    return Enums.SCORE.RED_HEART;
            } else {
                int weight = Integer.parseInt(item_EDT_weight.getEditText().getText().toString());
                if (((double) carbs / weight) > 0.1)
                    return Enums.SCORE.BLACK_HEART;
                else
                    return Enums.SCORE.RED_HEART;
            }

        } else
            return Enums.SCORE.RED_HEART;
    }
}
