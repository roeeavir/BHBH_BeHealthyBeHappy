package com.example.bhbh_behealthybehappy.Controllers;

import android.content.Context;
import android.view.View;

import com.example.bhbh_behealthybehappy.Activities.CustomeItemActivity;
import com.example.bhbh_behealthybehappy.Models.ActivityEntry;
import com.example.bhbh_behealthybehappy.Models.DrinkEntry;
import com.example.bhbh_behealthybehappy.Models.FoodEntry;
import com.example.bhbh_behealthybehappy.Models.ItemEntry;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;
import com.example.bhbh_behealthybehappy.Utils.MyHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class CustomItemViewController {

    // Variables
    Context context;

    private Enums.ITEM_THEME theme;
    private Enums.SCORE score;

    private MaterialButton item_BTN_save;
    private TextInputLayout item_EDT_name;
    TextInputLayout item_EDT_weightOrAmount;
    TextInputLayout item_EDT_calories;
    TextInputLayout item_EDT_carbs;
    TextInputLayout item_EDT_notes;
    TextInputLayout item_EDT_time;
    TextInputLayout item_EDT_caloriesPerHour;

    private ItemEntry itemEntry;

    public CustomItemViewController(Context context) {
        this.context = context;

        findViews();
        initViews();

    }

    private void findViews() {
        item_EDT_name = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_name);
        item_EDT_weightOrAmount = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_weightOrAmount);
        item_EDT_calories = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_calories);
        item_EDT_carbs = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_carbs);
        item_EDT_notes = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_notes);
        item_EDT_time = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_time);
        item_EDT_caloriesPerHour = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_caloriesPerHour);
        item_BTN_save = ((CustomeItemActivity) context).findViewById(R.id.item_BTN_save);
    }

    public void updateTheme(Enums.ITEM_THEME th) {
        theme = th;

        if (theme == Enums.ITEM_THEME.ACTIVITY) {
            item_EDT_weightOrAmount.setVisibility(View.GONE);
            item_EDT_calories.setVisibility(View.GONE);
            item_EDT_carbs.setVisibility(View.GONE);
            item_EDT_time.setVisibility(View.VISIBLE);
            item_EDT_caloriesPerHour.setVisibility(View.VISIBLE);
        } else if (theme == Enums.ITEM_THEME.DRINK) {
            item_EDT_weightOrAmount.getEditText().setText(((CustomeItemActivity) context).
                    getResources().getString(R.string.item_amount_milliliters));
        }

        item_EDT_name.setHint(item_EDT_name.getHint() + " " + theme.toString());


    }

    private void initViews() {
        item_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
    }

    private void saveInfo() {
        if (checkInfo()) {
            score = setScore();
            if (theme == Enums.ITEM_THEME.DRINK)
                itemEntry = new DrinkEntry(item_EDT_name.getEditText().getText().toString(), score,
                        item_EDT_notes.getEditText().getText().toString(),
                        Integer.parseInt(item_EDT_weightOrAmount.getEditText().getText().toString()),
                        Integer.parseInt(item_EDT_carbs.getEditText().getText().toString()));
            else if (theme == Enums.ITEM_THEME.ACTIVITY)
                itemEntry = new ActivityEntry(item_EDT_name.getEditText().getText().toString()
                        , score, item_EDT_notes.getEditText().getText().toString(),
                        Integer.parseInt(item_EDT_time.getEditText().getText().toString()),
                        Integer.parseInt(item_EDT_caloriesPerHour.getEditText().getText().toString()));
            else
                itemEntry = new FoodEntry(item_EDT_name.getEditText().getText().toString(), score,
                        item_EDT_notes.getEditText().getText().toString(),
                        Integer.parseInt(item_EDT_weightOrAmount.getEditText().getText().toString()),
                        Integer.parseInt(item_EDT_carbs.getEditText().getText().toString()));
        } else
            MyHelper.getInstance().toast("Some Variables seems to have bad inputs");

    }


    private boolean checkInfo() {
        if (theme == Enums.ITEM_THEME.ACTIVITY)
            return checkActivityInfo();
        else
            return checkFoodOrDrinkInfo();


    }

    private boolean checkFoodOrDrinkInfo() {
        int weight, calories, carbs;
        if (item_EDT_weightOrAmount.getEditText().getText().toString().equals("") ||
                item_EDT_calories.getEditText().getText().toString().equals("")) {
            MyHelper.getInstance().toast("Some Variables seems to be missing");
            return false;
        }
        weight = Integer.parseInt(item_EDT_weightOrAmount.getEditText().getText().toString());
        calories = Integer.parseInt(item_EDT_calories.getEditText().getText().toString());
        if (!item_EDT_carbs.getEditText().getText().toString().equals("")) {
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
        if (item_EDT_weightOrAmount.getEditText().getText().toString().equals("") ||
                item_EDT_calories.getEditText().getText().toString().equals("")) {
            MyHelper.getInstance().toast("Some Variables seems to be missing");
            return false;
        }
        time = Integer.parseInt(item_EDT_weightOrAmount.getEditText().getText().toString());
        caloriesPerHour = Integer.parseInt(item_EDT_calories.getEditText().getText().toString());
        return time > 0 && caloriesPerHour > 0;
    }

    private Enums.SCORE setScore() {
        if (theme == Enums.ITEM_THEME.ACTIVITY)
            return Enums.SCORE.GREEN_STAR;
        else if (!item_EDT_carbs.getEditText().getText().toString().equals("")) {
            int carbs = Integer.parseInt(item_EDT_carbs.getEditText().getText().toString());
            int weight_or_amount = Integer.parseInt(item_EDT_weightOrAmount.getEditText().getText().toString());
            if (((double) weight_or_amount / carbs) > 0.1)
                return Enums.SCORE.BLACK_HEART;
            else
                return Enums.SCORE.RED_HEART;
        } else
            return Enums.SCORE.RED_HEART;
    }
}
