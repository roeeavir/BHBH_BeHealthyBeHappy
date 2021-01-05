package com.example.bhbh_behealthybehappy.Fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bhbh_behealthybehappy.Models.SettingsViewModel;
import com.example.bhbh_behealthybehappy.Models.UserInfo;
import com.example.bhbh_behealthybehappy.R;
import com.example.bhbh_behealthybehappy.Utils.CallBack;
import com.example.bhbh_behealthybehappy.Utils.MyHelper;
import com.example.bhbh_behealthybehappy.Utils.MySP;
import com.example.bhbh_behealthybehappy.Utils.ScreenUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.USER_INFO;

public class SettingsFragment extends Fragment {

    // Variables
    private SettingsViewModel settingsViewModel;

    private TextInputLayout settings_EDT_name;
    TextInputLayout settings_EDT_age;
    TextInputLayout settings_EDT_weight;
    TextInputLayout settings_EDT_height;
    private Button info_BTN_save;

    private UserInfo userInfo;

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        findViews(root);
        initViews();

        loadText();
        return root;
    }

    private void loadText() {
        Gson gson = new Gson();

        userInfo = gson.fromJson(MySP.getInstance().getString(USER_INFO, ""), UserInfo.class);

        if (userInfo != null) {
            settings_EDT_name.getEditText().setText(userInfo.getUserName());
            settings_EDT_age.getEditText().setText("" + userInfo.getUserAge());
            settings_EDT_weight.getEditText().setText("" + userInfo.getUserWeight());
            settings_EDT_height.getEditText().setText("" + userInfo.getUserHeight());
        }

    }

    private void initViews() {
        info_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
                ScreenUtils.hideSystemUI((AppCompatActivity) getActivity());
            }
        });
    }

    private void saveInfo() {
        Gson gson = new Gson();

        if (checkInfo()) {
            userInfo = new UserInfo(settings_EDT_name.getEditText().getText().toString(),
                    Integer.parseInt(settings_EDT_age.getEditText().getText().toString()),
                    Integer.parseInt(settings_EDT_weight.getEditText().getText().toString()),
                    Integer.parseInt(settings_EDT_height.getEditText().getText().toString()));
            MySP.getInstance().putString(USER_INFO, gson.toJson(userInfo));
            MyHelper.getInstance().toast("User info has been updated!");
        }


    }

    private boolean checkInfo() {
        if(settings_EDT_age.getEditText().getText().toString().equals("") ||
                settings_EDT_weight.getEditText().getText().toString().equals("") ||
                settings_EDT_height.getEditText().getText().toString().equals("")){
            MyHelper.getInstance().toast("Some Variables seems to be missing");
            return false;
        }
        if (Integer.parseInt(settings_EDT_age.getEditText().getText().toString()) > 0 &&
                Integer.parseInt(settings_EDT_weight.getEditText().getText().toString()) > 0
                && Integer.parseInt(settings_EDT_height.getEditText().getText().toString()) > 0){
            return true;
        }
        MyHelper.getInstance().toast("Some Variables seems to be wrong");
        return false;
    }

    private UserInfo generateData(SharedPreferences prefs, Gson gson) {
        String jsonFromMemory = prefs.getString(USER_INFO, "");
        return gson.fromJson(jsonFromMemory, UserInfo.class);
    }

    private void findViews(View root) {
        settings_EDT_name = root.findViewById(R.id.settings_EDT_name);
        settings_EDT_age = root.findViewById(R.id.settings_EDT_age);
        settings_EDT_weight = root.findViewById(R.id.settings_EDT_weight);
        settings_EDT_height = root.findViewById(R.id.settings_EDT_height);
        info_BTN_save = root.findViewById(R.id.info_BTN_save);
    }

}