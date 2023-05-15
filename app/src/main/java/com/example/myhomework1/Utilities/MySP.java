package com.example.myhomework1.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myhomework1.Models.Record;

public class MySP {

    public static final String DB_FILE = "DB_FILE";

    private static MySP instance = null;
    private SharedPreferences sharedPreferences;

    private MySP(Context context) {
        sharedPreferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
    }

    public static void init(Context context){
        if (instance == null){
            instance = new MySP(context);
        }
    }

    public static MySP getInstance() {
        return instance;
    }

    public String getString(String key, String value) {
        return sharedPreferences.getString(key, value);
    }
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public int getInt(String key, int value) {
        return sharedPreferences.getInt(key, value);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
