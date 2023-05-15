package com.example.myhomework1.Activities;

import static com.example.myhomework1.Activities.MainActivity.FINAL_SCORE_KEY;
import static com.example.myhomework1.Models.RecordsList.RECORD_LIST_SIZE;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import com.bumptech.glide.Glide;
import com.example.myhomework1.Models.Record;
import com.example.myhomework1.Models.RecordsList;
import com.example.myhomework1.R;
import com.example.myhomework1.Utilities.DataManager;
import com.example.myhomework1.Utilities.GPS;
import com.google.android.material.button.MaterialButton;

public class GameOverActivity extends AppCompatActivity {
    private AppCompatImageView game_over_IMG_background;
    private TextView gameOver_TV_points;
    private EditText gameOver_ETXT_name;
    private MaterialButton gameOver_BTN_save;
    private MaterialButton gameOver_BTN_menu;
    private MaterialButton gameOver_BTN_scoreBoard;
    private int score = 0;
    private String name = "";
    private Intent previousIntent;
    private Intent menuIntent;
    private Intent scoreBoardIntent;
    private GPS gps;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        previousIntent = getIntent();
        menuIntent = new Intent(this, MenuActivity.class);
        scoreBoardIntent = new Intent(this,RecordsActivity.class);
        gps = new GPS(this);
        findViews();
        initViews();
    }

    private void initViews() {
        score = previousIntent.getIntExtra(FINAL_SCORE_KEY,0);
        gameOver_TV_points.append(Integer.toString(score));
        gameOver_BTN_save.setOnClickListener(v-> {
            saveRecord();
            gameOver_BTN_save.setVisibility(View.INVISIBLE);
        });
        gameOver_BTN_menu.setOnClickListener(v-> {
            startActivity(menuIntent);
            finish();
        });
        gameOver_BTN_scoreBoard.setOnClickListener(v-> {
            startActivity(scoreBoardIntent);
            finish();
        });

    }

    private void saveRecord() {
        RecordsList tempList = DataManager.getRecordsList();
        if (tempList.getList().size() == RECORD_LIST_SIZE &&
                tempList.getList().get(RECORD_LIST_SIZE-1).getScore() > score) { // dont add record if its not top 10
            return;
        }
        int i=0;
        name = gameOver_ETXT_name.getText().toString();
        gps.updateLocation(this);
        Record record =new Record(name,score,gps.getX(),gps.getY());
        for (i=0;i<tempList.getList().size();i++) {
            if (tempList.getList().get(i).getScore() <= score)
                break;
        }
        if (tempList.getList().size() == RECORD_LIST_SIZE) { // delete last record if list full
            tempList.getList().remove(RECORD_LIST_SIZE-1);
        }
        tempList.getList().add(i,record);
        DataManager.setRecordsList(tempList);
        DataManager.getInstance().updateRecordsListJson();
    }

    private void findViews() {
        game_over_IMG_background = findViewById(R.id.game_over_IMG_background);
        gameOver_TV_points = findViewById(R.id.gameOver_TV_points);
        gameOver_ETXT_name = findViewById(R.id.gameOver_ETXT_name);
        gameOver_BTN_save = findViewById(R.id.gameOver_BTN_save);
        gameOver_BTN_menu = findViewById(R.id.gameOver_BTN_menu);
        gameOver_BTN_scoreBoard = findViewById(R.id.gameOver_BTN_scoreBoard);

        Glide
                .with(this)
                .load(R.drawable.background_wall)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(game_over_IMG_background);
    }
}
