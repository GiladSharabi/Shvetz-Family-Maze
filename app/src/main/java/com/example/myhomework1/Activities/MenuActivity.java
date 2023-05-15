package com.example.myhomework1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myhomework1.Models.RecordsList;
import com.example.myhomework1.Models.eGameSpeed;
import com.example.myhomework1.Utilities.DataManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import com.example.myhomework1.R;
import com.example.myhomework1.Utilities.MySP;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

public class MenuActivity extends AppCompatActivity {

    private LinearLayoutCompat menu_RLT_startMenu;
    private LinearLayout menu_LL_speed_mode;
    private MaterialButton menu_BTN_start;
    private MaterialButton menu_BTN_records;
    private MaterialButton menu_BTN_buttonsMode;
    private MaterialButton menu_BTN_sensorsMode;
    private MaterialButton menu_BTN_slowSpeed;
    private MaterialButton menu_BTN_fastSpeed;
    private TextView menu_TV_mode;
    private TextView menu_TV_speed;
    public static boolean isButtonMode = true;
    public static final int FAST_SPEED = 500;
    public static final int SLOW_SPEED = 800;
    public static final String RECORDS_LIST_JSON_KEY = "RECORDS_LIST_JSON";
    public static final String DELAY_STATUS = "DELAY_STATUS";
    private eGameSpeed gameSpeed = eGameSpeed.SLOW;
    private Intent mainActivityIntent;

    private Intent recordsActivityIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setTitle("Menu");
        getDataFromJson();
        Log.d("LIST: ", DataManager.getRecordsList().toString());
        findViews();
        initViews();
    }
    private void getDataFromJson() {
        String fromSP =  MySP.getInstance().getString(RECORDS_LIST_JSON_KEY,"");
        RecordsList recordsListFromJson = new Gson().fromJson(fromSP, RecordsList.class);
        if (recordsListFromJson != null) {
            Log.d("From JSON", recordsListFromJson.toString());
            DataManager.getInstance().setRecordsList(recordsListFromJson);
        }
    }


    private void initViews() {
        menu_RLT_startMenu.setVisibility(View.VISIBLE);
        mainActivityIntent = new Intent(this, MainActivity.class);
        recordsActivityIntent = new Intent(this, RecordsActivity.class);
        menu_BTN_start.setOnClickListener(v -> {
            startGame();
        });
        menu_BTN_records.setOnClickListener(v-> {
            openRecords();
        });
        menu_BTN_buttonsMode.setOnClickListener(v -> {
            setButtonMode();
        });
        menu_BTN_sensorsMode.setOnClickListener(v -> {
            setSensorsMode();
        });
        menu_BTN_slowSpeed.setOnClickListener(v -> {
            setSlowSpeed();
        });
        menu_BTN_fastSpeed.setOnClickListener(v -> {
            setFastSpeed();
        });
    }

    private void setSensorsMode() {
        gameSpeed = eGameSpeed.SLOW;
        isButtonMode = false;
        menu_LL_speed_mode.setVisibility(View.INVISIBLE);
        menu_TV_speed.setVisibility(View.INVISIBLE);
        menu_BTN_buttonsMode.setBackgroundColor(getResources().getColor(R.color.red_400));
        menu_BTN_sensorsMode.setBackgroundColor(getResources().getColor(R.color.green_400));
    }

    private void setFastSpeed() {
        gameSpeed = eGameSpeed.FAST;
        menu_BTN_slowSpeed.setBackgroundColor(getResources().getColor(R.color.red_400));
        menu_BTN_fastSpeed.setBackgroundColor(getResources().getColor(R.color.green_400));
    }

    private void setSlowSpeed() {
        gameSpeed = eGameSpeed.SLOW;
        menu_BTN_slowSpeed.setBackgroundColor(getResources().getColor(R.color.green_400));
        menu_BTN_fastSpeed.setBackgroundColor(getResources().getColor(R.color.red_400));
    }

    private void setButtonMode() {
        gameSpeed = eGameSpeed.SLOW;
        isButtonMode = true;
        menu_LL_speed_mode.setVisibility(View.VISIBLE);
        menu_TV_speed.setVisibility(View.VISIBLE);
        menu_BTN_buttonsMode.setBackgroundColor(getResources().getColor(R.color.green_400));
        menu_BTN_sensorsMode.setBackgroundColor(getResources().getColor(R.color.red_400));
        menu_BTN_slowSpeed.setBackgroundColor(getResources().getColor(R.color.green_400));
        menu_BTN_fastSpeed.setBackgroundColor(getResources().getColor(R.color.red_400));
    }

    private void openRecords() {
        startActivity(recordsActivityIntent);
        onPause();
    }

    private void startGame() {
        if (gameSpeed == eGameSpeed.SLOW)
            mainActivityIntent.putExtra(DELAY_STATUS,SLOW_SPEED);
        else if (gameSpeed == eGameSpeed.FAST)
            mainActivityIntent.putExtra(DELAY_STATUS,FAST_SPEED);
        startActivity(mainActivityIntent);
        onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        setButtonMode();
    }
    private void findViews() {
        menu_RLT_startMenu = findViewById(R.id.menu_RLT_start_menu);
        menu_LL_speed_mode = findViewById(R.id.menu_LL_speed_mode);
        menu_BTN_start = findViewById(R.id.menu_BTN_start);
        menu_BTN_records = findViewById(R.id.menu_BTN_records);
        menu_BTN_buttonsMode = findViewById(R.id.menu_BTN_buttons_mode);
        menu_BTN_sensorsMode = findViewById(R.id.menu_BTN_sensors_mode);
        menu_BTN_slowSpeed = findViewById(R.id.menu_BTN_speed_slow);
        menu_BTN_fastSpeed = findViewById(R.id.menu_BTN_speed_fast);
        menu_TV_mode = findViewById(R.id.menu_TEXTVIEW_mode);
        menu_TV_speed = findViewById(R.id.menu_TEXTVIEW_speed);

    }

}
