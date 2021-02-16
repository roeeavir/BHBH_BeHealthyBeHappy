package com.example.bhbh_behealthybehappy.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bhbh_behealthybehappy.Models.UserInfo;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Utils.FirebaseHelper;
import com.example.bhbh_behealthybehappy.Utils.MyHelper;
import com.example.bhbh_behealthybehappy.Utils.ScreenUtils;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.USERS_REF;
import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.USER_INFO_REF;

public class SettingsFragment extends Fragment {

    // Variables
    private TextInputLayout settings_EDT_name;
    private TextInputLayout settings_EDT_age;
    private TextInputLayout settings_EDT_weight;
    private TextInputLayout settings_EDT_height;
    private TextInputLayout settings_EDT_dailyScore;
    private Button info_BTN_save;
    private Button info_BTN_logOut;

    private UserInfo userInfo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("pttt", "Created SettingsFragment");
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        findViews(root);
        initViews();

        loadText();
        return root;
    }

    // Loads user info (if available) from firebase database
    private void loadText() {
        DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF);
        FirebaseUser user = FirebaseHelper.getInstance().getUser();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                userInfo = dataSnapshot.child(user.getUid()).child(USER_INFO_REF).getValue(UserInfo.class);
                if (userInfo != null) {
                    settings_EDT_name.getEditText().setText(userInfo.getUserName());
                    settings_EDT_age.getEditText().setText("" + userInfo.getUserAge());
                    settings_EDT_weight.getEditText().setText("" + userInfo.getUserWeight());
                    settings_EDT_height.getEditText().setText("" + userInfo.getUserHeight());
                    settings_EDT_dailyScore.getEditText().setText("" + userInfo.getUserDailyScore());
                    Log.d("pttt", "Loaded user text");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("pttt", "Failed to read value.", error.toException());
            }
        });

    }

    private void initViews() {
        info_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
                ScreenUtils.hideSystemUI((AppCompatActivity) getActivity());
            }
        });

        info_BTN_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(getActivity());
                Log.d("pttt", "Login out and finishing MainActivity");
                getActivity().finish();
            }
        });
    }

    // Saves user info to firebase database
    private void saveInfo() {
        DatabaseReference myRef = FirebaseHelper.getInstance().getDatabaseReference(USERS_REF);
        FirebaseUser user = FirebaseHelper.getInstance().getUser();

        if (checkInfo()) {// checks if user info is proper
            userInfo = new UserInfo().setUserName(settings_EDT_name.getEditText().getText().toString())
                    .setUserAge(Integer.parseInt(settings_EDT_age.getEditText().getText().toString()))
                    .setUserWeight(Integer.parseInt(settings_EDT_weight.getEditText().getText().toString()))
                    .setUserHeight(Integer.parseInt(settings_EDT_height.getEditText().getText().toString()))
                    .setUserDailyScore(Integer.parseInt(settings_EDT_dailyScore.getEditText().getText().toString()));
            myRef.child(user.getUid()).child(USER_INFO_REF).setValue(userInfo);
            Log.d("pttt", "Updated user info");
            MyHelper.getInstance().toast("User info has been updated!");
        }
        else
            Log.d("pttt", "Could not save user info");
    }

    // checks if user info is proper
    private boolean checkInfo() {
        if (settings_EDT_age.getEditText().getText().toString().isEmpty() ||
                settings_EDT_weight.getEditText().getText().toString().isEmpty() ||
                settings_EDT_height.getEditText().getText().toString().isEmpty() ||
                settings_EDT_name.getEditText().getText().toString().isEmpty()) {
            MyHelper.getInstance().toast("Some Variables seem to be missing");
            return false;
        }
        try {
            if (Integer.parseInt(settings_EDT_age.getEditText().getText().toString()) > 0 &&
                    Integer.parseInt(settings_EDT_weight.getEditText().getText().toString()) > 0
                    && Integer.parseInt(settings_EDT_height.getEditText().getText().toString()) > 0
                    && Integer.parseInt(settings_EDT_dailyScore.getEditText().getText().toString()) > 0)
                return true;
            MyHelper.getInstance().toast("Some Variables seem to be wrong");
            return false;
        } catch (Exception e) {
            MyHelper.getInstance().toast("Age, weight, height and score should all be numbers");
            return false;
        }
    }

    private void findViews(@NotNull View root) {
        settings_EDT_name = root.findViewById(R.id.settings_EDT_name);
        settings_EDT_age = root.findViewById(R.id.settings_EDT_age);
        settings_EDT_weight = root.findViewById(R.id.settings_EDT_weight);
        settings_EDT_height = root.findViewById(R.id.settings_EDT_height);
        settings_EDT_dailyScore = root.findViewById(R.id.settings_EDT_dailyScore);
        info_BTN_save = root.findViewById(R.id.info_BTN_save);
        info_BTN_logOut = root.findViewById(R.id.info_BTN_logOut);
    }

}