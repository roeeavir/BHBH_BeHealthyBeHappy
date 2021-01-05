package com.example.bhbh_behealthybehappy.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.bhbh_behealthybehappy.Constants_Enums.Constants.SP_FILE;

public class MySP {

//    public interface KEYS {
//        public static final String KEY_USER_USER_NAME = "KEY_USER_USER_NAME";
//        public static final String KEY_USER_THEME = "KEY_USER_THEME";
//
//    }

    private static MySP instance;
    private SharedPreferences prefs;

    public static MySP getInstance() {
        return instance;
    }

    private MySP(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(SP_FILE , Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new MySP(context);
        }
    }



    //// ---------------------------------------------------------- ////
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String def) {
        return prefs.getString(key, def);
    }

    public void removeKey(String key) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(key).apply();
    }

}
