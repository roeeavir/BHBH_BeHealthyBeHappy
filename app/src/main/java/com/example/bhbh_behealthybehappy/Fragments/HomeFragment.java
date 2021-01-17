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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bhbh_behealthybehappy.Activities.SearchActivity;
import com.example.bhbh_behealthybehappy.Constants_Enums.Enums;
import com.example.bhbh_behealthybehappy.Models.HomeViewModel;
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
import com.google.firebase.database.FirebaseDatabase;
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
    private HomeViewModel homeViewModel;

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

    private UserItemAdapter item_adapter;

    private double red_score = 0;
    private double black_score = 0;
    private double green_score = 0;
    private int water_glasses = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
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

        loadUserInfo(root);

        loadUserItems();

        return root;

    }

    private void loadUserItems() {
        FirebaseUser user = FirebaseHelper.getInstance().getUser();
        DatabaseReference myUserRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF).
                child(user.getUid()).child(DATES_REF).child(main_BTN_changeDate.getText().toString());

        myUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                namesOfItems.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String temp = postSnapshot.getKey();
                    int temp_quantity = postSnapshot.getValue(Integer.class);
                    if (temp != null) {
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

    private void setItemsInList() {

        userItems = new ArrayList<>(userItemEntryHashMap.values());

        Collections.sort(userItems);

        item_adapter = new UserItemAdapter(getActivity(), userItems);

        item_adapter.setClickListener(new UserItemAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyHelper.getInstance().toast(userItems.get(position).getItemEntry().getName());
            }

            @Override
            public void onSetQuantityClicked(View view, UserItemEntry item) {
                openQuantityPopUp(view, item);
            }

            @Override
            public void onRemoveItemClicked(View view, UserItemEntry item) {
                remove(item);
            }

        });

        home_LST_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        home_LST_list.setAdapter(item_adapter);
    }

    private void openQuantityPopUp(View view, UserItemEntry item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Quantity");

        final EditText et = new EditText(getActivity());
        et.setInputType(InputType.TYPE_CLASS_NUMBER);

        builder.setView(et);

        // set dialog message
        builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    int quantity = Integer.parseInt(et.getText().toString());

                    FirebaseUser user = FirebaseHelper.getInstance().getUser();
                    DatabaseReference myUserRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF).
                            child(user.getUid()).child(DATES_REF).child(main_BTN_changeDate.getText().toString());

                    myUserRef.child(item.getItemEntry().getName()).setValue(quantity);
                } catch (Exception e) {
                    MyHelper.getInstance().toast("Quantity should only be numeric");
                }

                item_adapter.notifyDataSetChanged();
                setScore();
            }
        });

        // create alert dialog
        AlertDialog alertDialog = builder.create();
        // show it
        alertDialog.show();


    }

    private void remove(UserItemEntry item) {
        FirebaseUser user = FirebaseHelper.getInstance().getUser();
        DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF);

        myRef.child(user.getUid()).child(DATES_REF).child(main_BTN_changeDate.getText().toString())
                .child(item.getItemEntry().getName()).removeValue();

        item_adapter.removeItem(item);

        MyHelper.getInstance().toast(item.getItemEntry().getName() + " has been removed from your list");
        Log.w("pttt", item.getItemEntry().getName() + " has been removed from your list");

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

    private void loadUserInfo(View root) {
//        Gson gson = new Gson();

        DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF);
        FirebaseUser user = FirebaseHelper.getInstance().getUser();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                userInfo = dataSnapshot.child(user.getUid()).child(USER_INFO_REF).getValue(UserInfo.class);
                if (userInfo == null) {
                    main_LBL_name.setText(getActivity().getResources().getString(R.string.hello_none));
                    main_LBL_weight.setText(getActivity().getResources().getString(R.string.weight_none));
                    main_LBL_bmi.setText(getActivity().getResources().getString(R.string.weight_none));
                    main_LBL_progress.setText(getActivity().getResources().getString(R.string.score_none));
                    Log.d("pttt", "User has no name yet");
                } else {
                    try {
                        if (getActivity() != null) {
                            String s;
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


    private void searchItem(View root, Button btn) {
        setButtons(false);
        Intent myIntent = new Intent(root.getContext(), SearchActivity.class);
        if (btn == main_BTN_addDrink)
            myIntent.putExtra(SearchActivity.SEARCH_ITEM, DRINK);
        else if (btn == main_BTN_addActivity)
            myIntent.putExtra(SearchActivity.SEARCH_ITEM, SPORTS_ACTIVITY);
        else
            myIntent.putExtra(SearchActivity.SEARCH_ITEM, FOOD);
        myIntent.putExtra(SearchActivity.USER_DATE, main_BTN_changeDate.getText().toString());
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
        home_LST_list = root.findViewById(R.id.home_LST_list);
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
        String date = dayOfMonth + "-" + month + "-" + year;
        updateMain_LBL_date(date);

        DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF);
        FirebaseUser user = FirebaseHelper.getInstance().getUser();

        resetScores();

        item_adapter.clear();
        item_adapter.notifyDataSetChanged();
        loadUserItems();
//        myRef.child(user.getUid()).child(USER_INFO_REF).setValue(userInfo);
    }


    public void setCurrentDate() {
        String date = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-" +
                (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
                + Calendar.getInstance().get(Calendar.YEAR);
        main_BTN_changeDate.setText(date);
    }

    private void updateMain_LBL_date(String date) {
        main_BTN_changeDate.setText(date);
    }

    private void updateMain_LBL_progress() {
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