package com.example.bhbh_behealthybehappy.Fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhbh_behealthybehappy.Activities.SearchActivity;
import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;
import com.example.bhbh_behealthybehappy.Models.ItemEntry;
import com.example.bhbh_behealthybehappy.Models.UserInfo;
import com.example.bhbh_behealthybehappy.Models.UserItemEntry;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Utils.FirebaseHelper;
import com.example.bhbh_behealthybehappy.Utils.MyHelper;
import com.example.bhbh_behealthybehappy.Utils.UserItemAdapter;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.DATES_REF;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.DRINK;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.FOOD;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.ITEMS_REF;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.SPORTS_ACTIVITY;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.USERS_REF;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.USER_INFO_REF;

public class HomeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    // Variables
    private Button main_BTN_changeDate;
    private Button main_BTN_addDrink;
    private Button main_BTN_addActivity;
    private Button main_BTN_addFood;
    private TextView main_LBL_name;
    private TextView main_LBL_weight;
    private TextView main_LBL_progress;
    private TextView main_LBL_bmi;

    private RecyclerView home_LST_list;

    private UserInfo userInfo;
    private DatePickerDialog datePickerDialog;

    private HashMap<String, UserItemEntry> userItemEntryHashMap = new HashMap<>();
    private HashMap<String, Integer> namesOfItems = new HashMap<>();
    private ArrayList<UserItemEntry> userItems;

    private UserItemAdapter user_item_adapter;

    private double red_score = 0;
    private double black_score = 0;
    private double green_score = 0;
    private int water_glasses = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        Log.d("pttt", "Created HomeFragment");

        findViews(root);
        initViews();

        setCurrentDate();

        loadUserInfo(root);

        loadUserItems();

        return root;

    }

    // Loads the names of the user's items from firebase database
    private void loadUserItems() {
        FirebaseUser user = FirebaseHelper.getInstance().getUser();
        DatabaseReference myUserRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF).
                child(user.getUid()).child(DATES_REF).child(main_BTN_changeDate.getText().toString());

        myUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int temp_quantity;
                namesOfItems.clear();
                water_glasses = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String temp = postSnapshot.getKey();
                    if (temp != null) {
                        temp_quantity = postSnapshot.getValue(Integer.class);
                        if (temp.toLowerCase().equals("water"))
                            water_glasses = postSnapshot.getValue(Integer.class);
                        else
                            namesOfItems.put(temp, temp_quantity);
                    }

                    Log.d("pttt", "Name of user item is: " + temp);
                }
                if (namesOfItems != null)
                    loadItems();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("pttt", "Failed to read value.", error.toException());
            }
        });

    }

    // Loads the items that fit the names of the user's items from firebase database
    private void loadItems() {
        DatabaseReference myUserRef = FirebaseHelper.getInstance().getDatabaseReference(ITEMS_REF);

        myUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                userItemEntryHashMap.clear();
                for (Map.Entry<String, Integer> entry : namesOfItems.entrySet()) {
                    for (Enums.ITEM_THEME type : Enums.ITEM_THEME.values())
                        for (DataSnapshot postSnapshot : dataSnapshot.
                                child(type.toString()).getChildren()) {
                            ItemEntry temp = postSnapshot.getValue(ItemEntry.class);
                            if (temp != null && entry.getKey().equals(temp.getName())) {
                                UserItemEntry userItemEntry = new UserItemEntry().setItemEntry(temp).setQuantity(entry.getValue());
                                userItemEntry.updateScore_by_quantity();
                                userItemEntryHashMap.put(temp.getName(), userItemEntry);
                                Log.d("pttt", "Item in your list: " + temp.getName());
                            }
                        }
                }

                if (getActivity() != null)
                    if (userItemEntryHashMap != null) {
                        setItemsInList();
                        if (userInfo != null)
                            setScore();
                    }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("pttt", "Failed to read value.", error.toException());
            }
        });

    }

    // Sets the scores shown in the activity
    private void setScore() {
        resetScores();
        for (UserItemEntry item : userItems) {
            if (item.getItemEntry().getItemType() == Enums.ITEM_THEME.FOOD || item.getItemEntry().getItemType() == Enums.ITEM_THEME.DRINK) {
                if (item.getItemEntry().getScoreType() == Enums.SCORE.BLACK_HEART)
                    black_score += item.getScore_by_quantity();
                red_score += item.getScore_by_quantity();
            } else if (item.getItemEntry().getItemType() == Enums.ITEM_THEME.ACTIVITY)
                green_score += item.getScore_by_quantity();
        }

        updateMain_LBL_progress();

    }

    // Setting items in list using user_item_adapter
    private void setItemsInList() {

        userItems = new ArrayList<>(userItemEntryHashMap.values());

        Collections.sort(userItems);

        user_item_adapter = new UserItemAdapter(getActivity(), userItems);

        user_item_adapter.setClickListener(new UserItemAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyHelper.getInstance().toast(userItems.get(position).getItemEntry().getName());
                Log.d("pttt", "Clicked on item " + userItems.get(position).getItemEntry().getName());
            }

            @Override
            public void onSetQuantityClicked(UserItemEntry item) {
                openQuantityPopUp(item);
            }

            @Override
            public void onRemoveItemClicked(View view, UserItemEntry item) {
                FirebaseHelper.getInstance().removeUserItem(item,
                        user_item_adapter, main_BTN_changeDate.getText().toString());
                Log.d("pttt", "Removed item " + item.getItemEntry().getName() + " from user list");
            }

        });

        home_LST_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        home_LST_list.setAdapter(user_item_adapter);
    }

    // Opens a popup to set the items quantity
    private void openQuantityPopUp(UserItemEntry item) {
        Log.d("pttt", "Opening " + item.getItemEntry().getName() + "'s quantity popup");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String s = "";
        if (item.getItemEntry().getItemType() == Enums.ITEM_THEME.ACTIVITY)
            s = "'s Duration In Minutes";
        else if (item.getItemEntry().getItemType() == Enums.ITEM_THEME.DRINK)
            s = "'s Amount In Milliliters";
        else
            s = "'s Weight In Grams";

        s = "Enter " + item.getItemEntry().getName() + s;
        builder.setTitle(s);

        final EditText et = createEditText(s, item);

        builder.setView(et);

        // set dialog message
        builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    int quantity = Integer.parseInt(et.getText().toString());

                    // Capping sports activity quantity of minutes as the number of minutes in a day
                    if (item.getItemEntry().getItemType() == Enums.ITEM_THEME.ACTIVITY && quantity > 1440) {
                        quantity = 1440;
                        MyHelper.getInstance().toast("Activity duration cannot be higher" +
                                " than a day's worth of minutes");
                    } else if (quantity > 20000) {// Capping food or drink quantity at 20,000
                        quantity = 20000;
                        MyHelper.getInstance().toast("Food or Drink quantity cannot be higher than 20,000");
                    }

                    FirebaseUser user = FirebaseHelper.getInstance().getUser();
                    DatabaseReference myUserRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF).
                            child(user.getUid()).child(DATES_REF).child(main_BTN_changeDate.getText().toString());

                    myUserRef.child(item.getItemEntry().getName()).setValue(quantity);
                } catch (Exception e) {
                    MyHelper.getInstance().toast("Quantity should only be numeric");
                }

                user_item_adapter.notifyDataSetChanged();
                setScore();
            }
        });

        // create alert dialog
        AlertDialog alertDialog = builder.create();
        // show it
        alertDialog.show();

    }

    // Creating edit text
    private EditText createEditText(String s, UserItemEntry item) {
        final EditText et = new EditText(getActivity());
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setText("" + item.getQuantity());
        et.setGravity(Gravity.CENTER);
        return et;
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
        main_LBL_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfo();
            }
        });

    }

    private void openInfo() { // Opens a dialog window containing scores info
            Log.d("pttt", "Opening info window");
            String s = getActivity().getResources().getString(R.string.info_red_hearts) + "\n\n" +
                    getActivity().getResources().getString(R.string.info_black_hearts) + "\n\n" +
                    getActivity().getResources().getString(R.string.info_green_stars);
            new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                    .setTitle("Scores Info")
                    .setMessage(s)
                    .setPositiveButton("OK", null)
                    .show();
    }

    // Loading user info (if exists) from firebase database
    private void loadUserInfo(View root) {
        DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF);
        FirebaseUser user = FirebaseHelper.getInstance().getUser();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String s;

                userInfo = dataSnapshot.child(user.getUid()).child(USER_INFO_REF).getValue(UserInfo.class);
                if (userInfo == null) {
                    if (user.getDisplayName().isEmpty())
                        s = getActivity().getResources().getString(R.string.hello_none);
                    else
                        s = getActivity().getResources().getString(R.string.hello) + " " + user.getDisplayName();
                    main_LBL_name.setText(s);
                    main_LBL_weight.setText(getActivity().getResources().getString(R.string.weight_none));
                    main_LBL_bmi.setText(getActivity().getResources().getString(R.string.weight_none));
                    main_LBL_progress.setText(getActivity().getResources().getString(R.string.score_none));
                    Log.d("pttt", "User has no name yet");
                } else {
                    try {
                        if (getActivity() != null) {
                            double bmi = (double) userInfo.getUserWeight() / Math.pow((double) userInfo.getUserHeight() / 100, 2);
                            s = getActivity().getResources().getString(R.string.hello) + " " +
                                    userInfo.getUserName();
                            main_LBL_name.setText(s);
                            s = getActivity().getResources().getString(R.string.last_weight) + " " +
                                    userInfo.getUserWeight() + "kg";
                            main_LBL_weight.setText(s);
                            s = getActivity().getResources().getString(R.string.bmi) + " " +
                                    String.format("%.2f", bmi);
                            main_LBL_bmi.setText(s);
                            Log.d("pttt", "User name value is: " + userInfo.getUserName());
                            setScore();
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("pttt", "Failed to read value.", error.toException());
            }
        });
    }

    // Sets and opens search activity
    private void searchItem(View root, Button btn) {
        setButtons(false); // prevents from buttons being clicked more than once
        Intent myIntent = new Intent(root.getContext(), SearchActivity.class);
        if (btn == main_BTN_addDrink)
            myIntent.putExtra(SearchActivity.SEARCH_ITEM, DRINK);
        else if (btn == main_BTN_addActivity)
            myIntent.putExtra(SearchActivity.SEARCH_ITEM, SPORTS_ACTIVITY);
        else
            myIntent.putExtra(SearchActivity.SEARCH_ITEM, FOOD);
        myIntent.putExtra(SearchActivity.USER_DATE, main_BTN_changeDate.getText().toString());
        root.getContext().startActivity(myIntent);// Opens search activity
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
        home_LST_list = root.findViewById(R.id.home_LST_list);
    }

    // Showing the date picker dialog popup
    private void showDatePickerDialog(View root) {
        datePickerDialog = new DatePickerDialog(root.getContext(),
                this,
                Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "-" + (month + 1) + "-" + year;
        updateMain_LBL_date(date);

        resetScores(); // Resets previous shown date's data

        user_item_adapter.clear();
        user_item_adapter.notifyDataSetChanged();
        loadUserItems(); // Loads wanted date relevant data
    }

    // Setting the current date
    public void setCurrentDate() {
        String date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-" +
                (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
                + Calendar.getInstance().get(Calendar.YEAR);
        main_BTN_changeDate.setText(date);
    }

    private void updateMain_LBL_date(String date) {
        main_BTN_changeDate.setText(date);
    }

    // Method for adding colors to Main_LBL_progress
    private void updateMain_LBL_progress() {
        Log.d("pttt", "Adding colors to Main_LBL_progress");
        String s1, s2, s3, s4;
        int len1, len2, len3, len4;
        s1 = getActivity().getResources().getString(R.string.progress_score) + "\n" +
                red_score + " \\ " + userInfo.getUserDailyScore() + " red hearts\n";
        s2 = " " + black_score + " \\ " + ((int) (userInfo.getUserDailyScore() / 4))
                + " black hearts\n";
        s3 = green_score + " green stars";
        s4 = " and " + water_glasses + " water glasses";

        len1 = getActivity().getResources().getString(R.string.progress_score).length();
        len2 = s1.length();
        len3 = len2 + s2.length();
        len4 = len3 + s3.length() + 4;

        // Changes a part of the label's color
        Spannable wordToSpan = new SpannableString(s1 + s2 + s3 + s4);
        wordToSpan.setSpan(new ForegroundColorSpan(Color.RED), len1, s1.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new ForegroundColorSpan(Color.BLACK), len2, s1.length() + s2.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new ForegroundColorSpan(Color.GREEN), len3, s1.length() + s2.length() + s3.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new ForegroundColorSpan(Color.BLUE), len4, s1.length() + s2.length() + s3.length() + s4.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_LBL_progress.setText(wordToSpan);

    }

    private void resetScores() {// resets all the scores
        red_score = 0;
        black_score = 0;
        green_score = 0;
    }

}