package com.example.bhbh_behealthybehappy.Controllers;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.bhbh_behealthybehappy.Activities.CustomeItemActivity;
import com.example.bhbh_behealthybehappy.Models.ItemEntry;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;
import com.example.bhbh_behealthybehappy.Utils.FirebaseHelper;
import com.example.bhbh_behealthybehappy.Utils.MyHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;

public class CustomItemViewController {

    // Variables
    private Context context;

    private Enums.ITEM_THEME theme;
    private Enums.SCORE score;

    private MaterialButton item_BTN_save;
    private TextInputLayout item_EDT_name;
    private TextInputLayout item_EDT_calories;
    private TextInputLayout item_EDT_carbs;
    private TextInputLayout item_EDT_notes;
    private TextInputLayout item_EDT_caloriesBurned;
    private ImageButton item_IMB_back;
    private TextView item_LBL_header;
    private TextView item_LBL_subHeader;

    public CustomItemViewController(Context context) {
        this.context = context;

        findViews();
        initViews();

    }

    private void findViews() {
        item_EDT_name = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_name);
        item_EDT_calories = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_calories);
        item_EDT_carbs = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_carbs);
        item_EDT_notes = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_notes);
        item_EDT_caloriesBurned = ((CustomeItemActivity) context).findViewById(R.id.item_EDT_caloriesBurned);
        item_BTN_save = ((CustomeItemActivity) context).findViewById(R.id.item_BTN_save);
        item_IMB_back = ((CustomeItemActivity) context).findViewById(R.id.item_IMB_back);
        item_LBL_header = ((CustomeItemActivity) context).findViewById(R.id.item_LBL_header);
        item_LBL_subHeader = ((CustomeItemActivity) context).findViewById(R.id.item_LBL_subHeader);
    }

    public void updateTheme(Enums.ITEM_THEME th) {
        Log.d("pttt", "Setting activity by theme (" + th.toString() + ")");
        String s1, s2;
        theme = th;

        // Changing views visibility and wording depending on the theme
        if (theme == Enums.ITEM_THEME.ACTIVITY) {
            item_EDT_calories.setVisibility(View.GONE);
            item_EDT_carbs.setVisibility(View.GONE);
            item_EDT_caloriesBurned.setVisibility(View.VISIBLE);
            s1 = theme.toString() + " duration " + item_LBL_subHeader.getText() + "\n15 minutes";
        } else if (theme == Enums.ITEM_THEME.DRINK)
            s1 = theme.toString() + " amount " + item_LBL_subHeader.getText() + "\n100 milliliters";
        else
            s1 = theme.toString() + " weight " + item_LBL_subHeader.getText() + "\n100 grams";

        s2 = theme.toString() + " " + item_LBL_header.getText();

        item_LBL_subHeader.setText(s1);
        item_LBL_header.setText(s2);
        item_EDT_name.setHint(item_EDT_name.getHint() + " " + theme.toString());
        item_EDT_notes.setHint(theme.toString() + " " + item_EDT_notes.getHint());
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
                Log.d("pttt", "Finishing CustomItemActivity");
                ((CustomeItemActivity) context).finish();// Quits activity
            }
        });
    }

    // Method for saving item
    private void saveInfo() {
        if (checkInfo()) { // checks if info is proper
            String name; // Sets name from text
            name = item_EDT_name.getEditText().getText().toString().substring(0, 1).toUpperCase()
                    + item_EDT_name.getEditText().getText().toString().substring(1);
            DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference("Items");
            score = setScore();
            if (theme == Enums.ITEM_THEME.DRINK)
                myRef.child(theme.toString()).child(name).setValue(new ItemEntry().setName(name).setItemType(theme)
                        .setCarbs(Integer.parseInt(item_EDT_carbs.getEditText().getText().toString()))
                        .setScoreType(score).setNotes(item_EDT_notes.getEditText().getText().toString())
                        .setCalories(Integer.parseInt(item_EDT_calories.getEditText().getText().toString()))
                        .updateScore()
                );
            else if (theme == Enums.ITEM_THEME.ACTIVITY)
                myRef.child(theme.toString()).child(name).setValue(new ItemEntry().setName(name).setItemType(theme)
                        .setCaloriesBurned(Integer.parseInt(item_EDT_caloriesBurned.getEditText().getText().toString()))
                        .setScoreType(score).setNotes(item_EDT_notes.getEditText().getText().toString())
                        .updateScore()
                );
            else
                myRef.child(theme.toString()).child(name).setValue(new ItemEntry().setName(name).setItemType(theme)
                        .setCarbs(Integer.parseInt(item_EDT_carbs.getEditText().getText().toString()))
                        .setScoreType(score).setNotes(item_EDT_notes.getEditText().getText().toString())
                        .setCalories(Integer.parseInt(item_EDT_calories.getEditText().getText().toString()))
                        .updateScore()
                );
            MyHelper.getInstance().toast("Item has been added to database!");
            Log.d("pttt", "Item " + name + " has been saved!");
        } else{
            MyHelper.getInstance().toast("Some Variables seem to have bad inputs");
            Log.d("pttt", "Could not save item");
        }


    }

    // checks if info is proper
    private boolean checkInfo() {
        if (item_EDT_name.getEditText().getText().toString().isEmpty()) {
            MyHelper.getInstance().toast("Item name is missing");
            return false;
        } else if (theme == Enums.ITEM_THEME.ACTIVITY)
            return checkActivityInfo();
        else
            return checkFoodOrDrinkInfo();
    }

    // checks if info relating food or drink is proper
    private boolean checkFoodOrDrinkInfo() {
        int calories, carbs;
        if (item_EDT_calories.getEditText().getText().toString().equals("")) {
            MyHelper.getInstance().toast("Calories variable needs to be filled");
            return false;
        }
        try {
            calories = Integer.parseInt(item_EDT_calories.getEditText().getText().toString());
            if (item_EDT_carbs.getEditText().getText().toString().equals("")) {
                item_EDT_carbs.getEditText().setText("0");
                return calories >= 0;
            } else {
                carbs = Integer.parseInt(item_EDT_carbs.getEditText().getText().toString());
                if (carbs > 100)
                    return false;
                return calories >= 0 && carbs >= 0;
            }
        } catch (Exception e) {
            MyHelper.getInstance().toast("Calories and carbs (if entered) should all be numbers");
            return false;
        }
    }

    // checks if info relating activities is proper
    private boolean checkActivityInfo() {
        int caloriesPerHour;
        if (item_EDT_caloriesBurned.getEditText().getText().toString().equals("")) {
            MyHelper.getInstance().toast("Some Variables seem to be missing");
            return false;
        }
        try {
            caloriesPerHour = Integer.parseInt(item_EDT_caloriesBurned.getEditText().getText().toString());
            return caloriesPerHour > 0;
        } catch (Exception e) {
            MyHelper.getInstance().toast("Calories per hour should be a number");
            return false;
        }
    }

    // Sets score type by using info
    private Enums.SCORE setScore() {
        if (theme == Enums.ITEM_THEME.ACTIVITY)
            return Enums.SCORE.GREEN_STAR;
        else if (!item_EDT_carbs.getEditText().getText().toString().equals("")) {
            int carbs = Integer.parseInt(item_EDT_carbs.getEditText().getText().toString());
            // if more than 10% of the item is made of carbs - the item will be branded with a black star score
            if (((double) carbs / 100) > 0.1)
                return Enums.SCORE.BLACK_HEART;
            else
                return Enums.SCORE.RED_HEART;
        } else
            return Enums.SCORE.RED_HEART;
    }
}
