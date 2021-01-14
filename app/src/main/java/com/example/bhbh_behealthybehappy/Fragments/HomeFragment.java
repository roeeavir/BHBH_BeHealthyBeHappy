package com.example.bhbh_behealthybehappy.Fragments;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Utils.FirebaseHelper;
import com.example.bhbh_behealthybehappy.Utils.ItemAdapter;
import com.example.bhbh_behealthybehappy.Utils.MyHelper;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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

    private HashMap<String, ItemEntry> itemEntryHashMap = new HashMap<>();
    private ArrayList<String> namesOfItems;
    private ArrayList<ItemEntry> items;

    private ItemAdapter item_adapter;

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
                namesOfItems = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String temp = postSnapshot.getValue(String.class);
                    if (temp != null)
                        namesOfItems.add(temp);
                    Log.d("pttt", "Name of item is: " + temp);
                }
                if (namesOfItems != null)
                    LoadItems();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("pttt", "Failed to read value.", error.toException());
            }
        });

//        itemEntryHashMap.put()
    }

    private void LoadItems() {
        DatabaseReference myUserRef = FirebaseHelper.getInstance().getDatabaseReference(ITEMS_REF);

        myUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                itemEntryHashMap.clear();
                for (String s : namesOfItems) {
                    for (Enums.ITEM_THEME type : Enums.ITEM_THEME.values())
                        for (DataSnapshot postSnapshot : dataSnapshot.
                                child(type.toString()).getChildren()) {
                            ItemEntry temp = postSnapshot.getValue(ItemEntry.class);
                            if (temp != null && s.equals(temp.getName()))
                                itemEntryHashMap.put(temp.getName(), temp);
                            Log.d("pttt", "Name of item is: " + temp);
                        }
                }

                if (getActivity() != null)
                    if (itemEntryHashMap != null)
                        setItemsInList();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("pttt", "Failed to read value.", error.toException());
            }
        });

    }

    private void setItemsInList() {

        items = new ArrayList<>(itemEntryHashMap.values());

        item_adapter = new ItemAdapter(getActivity(), items);

        item_adapter.setClickListener(new ItemAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyHelper.getInstance().toast(items.get(position).getName());
            }

            @Override
            public void onReadMoreClicked(View view, ItemEntry item) {

//                openInfo(item);
            }

            @Override
            public void onAddItemClicked(View view, ItemEntry item) {
                view.setEnabled(false);
                remove(item);
            }
        });

        home_LST_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        home_LST_list.setAdapter(item_adapter);
    }

    private void remove(ItemEntry item) {
        item_adapter.removeItem(item);

        FirebaseUser user = FirebaseHelper.getInstance().getUser();
        DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF);

        myRef.child(user.getUid()).child(DATES_REF).child(main_BTN_changeDate.getText().toString())
                .child(item.getName()).removeValue();
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
                            Log.d("pttt", "Value is: " + userInfo.getUserName());
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

//        userInfo = gson.fromJson(MySP.getInstance().getString(USER_INFO, ""), UserInfo.class);


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

        item_adapter.clear();
        loadUserItems();
        item_adapter.notifyDataSetChanged();
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

}