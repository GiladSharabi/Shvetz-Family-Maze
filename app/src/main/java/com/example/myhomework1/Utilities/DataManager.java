package com.example.myhomework1.Utilities;

import static com.example.myhomework1.Activities.MenuActivity.RECORDS_LIST_JSON_KEY;

import android.util.Log;

import com.example.myhomework1.Models.Record;
import com.example.myhomework1.Models.RecordsList;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DataManager {
    private static DataManager instance = new DataManager();
    private static RecordsList recordsList = new RecordsList();

    public DataManager() {
    }
    public static DataManager getInstance() {
        return instance;
    }
    public void updateRecordsListJson() {
        Log.d("current list:",DataManager.getInstance().getRecordsList().toString());
        String toJson = new Gson().toJson(DataManager.getInstance().getRecordsList());
        Log.d("JSON UPDATE:",toJson);
        MySP.getInstance().putString(RECORDS_LIST_JSON_KEY,toJson);
    }

    public static RecordsList getRecordsList() {
        return recordsList;
    }

    public static void setRecordsList(RecordsList recordsList) {
        DataManager.recordsList = recordsList;
    }
}
