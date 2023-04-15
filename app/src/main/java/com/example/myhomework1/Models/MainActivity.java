package com.example.myhomework1.Models;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.myhomework1.Logic.GameManager;
import com.example.myhomework1.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private final int ROWS = 6;
    private final int COLS = 3;
    private final int START_LIFE = 3;
    private final int DELAY =1000;
    private AppCompatImageView main_IMG_background;
    private RelativeLayout main_RLT_startGame;
    private MaterialButton main_BTN_start;
    private FloatingActionButton main_FAB_left;
    private FloatingActionButton main_FAB_right;
    private ShapeableImageView[] main_IMG_hearts; // the three hearts layout
    private ShapeableImageView[][] main_IMG_enemy; // enemy matrix
    private ShapeableImageView[] main_IMG_player; // player array
    private ShapeableImageView main_IMG_crash; // crash event image
    private boolean isCrash = false;
    private GameManager gameManager;
    private Timer timer;
    private final String toastText = "DROR!";
    private MediaPlayer drorSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        drorSound = MediaPlayer.create(this, R.raw.dror_record);
        gameManager = new GameManager(ROWS,COLS,START_LIFE);
        initViews();
    }
    private void initViews() {
        main_RLT_startGame.setVisibility(View.VISIBLE);
        main_BTN_start.setOnClickListener(v -> {
            startGameInView();
            startTimer();
        });
        main_FAB_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowClick(eMove.left);
            }
        });
        main_FAB_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowClick(eMove.right);
            }
        });
        for (int i=0;i<main_IMG_enemy.length;i++) {
            for (int j=0;j<main_IMG_enemy[i].length;j++) {
                main_IMG_enemy[i][j].setVisibility(View.INVISIBLE);
            }
        }
        int playerCol = gameManager.getPlayer().getCurrentCol();
        for (int i=0;i<main_IMG_player.length;i++) {
            //if (i != playerCol)
                main_IMG_player[i].setVisibility(View.INVISIBLE);
        }
        main_IMG_crash.setVisibility(View.INVISIBLE);
    }
    private void arrowClick(eMove move) {
        main_IMG_player[gameManager.getPlayer().getCurrentCol()].setVisibility(View.INVISIBLE);
        gameManager.moveThePlayer(move);
        main_IMG_player[gameManager.getPlayer().getCurrentCol()].setVisibility(View.VISIBLE);
    }
    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameManager.nextMoveAllEnemies();
                        refreshUI();
                    }
                });
            }
        },DELAY,DELAY);
    }
    private void startGameInView() {
        main_RLT_startGame.setVisibility(View.INVISIBLE);
        main_IMG_player[gameManager.getPlayer().getCurrentCol()].setVisibility(View.VISIBLE);
        //main_IMG_enemy[0][gameManager.getEnemyColInRowIndex(0)].setVisibility(View.VISIBLE);
        refreshUI();
    }
    private void refreshUI() {
        if (isCrash) {
            main_IMG_crash.setVisibility(View.INVISIBLE);
            isCrash = false;
        }
        moveEnemiesInView();
        checkCrashEvent();
    }
    private void moveEnemiesInView() {
        int enemyCol;

        for (int i=0;i<ROWS-1;i++) {
            for (int j=0; j<COLS; j++) {
                main_IMG_enemy[i][j].setVisibility(View.INVISIBLE);
            }
        }

        for (int i=0; i<ROWS-1; i++) {
            enemyCol = gameManager.getEnemyColInRowIndex(i);
            if (enemyCol>=0)
                main_IMG_enemy[i][enemyCol].setVisibility(View.VISIBLE);
        }
    }
    private void checkCrashEvent() {
        if (gameManager.isEnemyOnPlayer())
            crashEvent();
    }
    private void crashEvent() {
        try {
           drorSound.start();
        } catch (IllegalStateException e) {
            // do nothing
        }
        isCrash = true;
        main_IMG_crash.setVisibility(View.VISIBLE);
        vibrate();
        main_IMG_hearts[gameManager.getLife()].setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }
    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_RLT_startGame = findViewById(R.id.main_RLT_start);
        main_BTN_start = findViewById(R.id.main_BTN_start);
        main_FAB_left = findViewById(R.id.arrow_FAB_left);
        main_FAB_right = findViewById(R.id.arrow_FAB_right);


        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};

        main_IMG_enemy = new ShapeableImageView[][] {
                {findViewById(R.id.gridLayout_IMG_grandma00),
                findViewById(R.id.gridLayout_IMG_grandma01),
                findViewById(R.id.gridLayout_IMG_grandma02)},
                {findViewById(R.id.gridLayout_IMG_grandma10),
                findViewById(R.id.gridLayout_IMG_grandma11),
                findViewById(R.id.gridLayout_IMG_grandma12)},
                {findViewById(R.id.gridLayout_IMG_grandma20),
                findViewById(R.id.gridLayout_IMG_grandma21),
                findViewById(R.id.gridLayout_IMG_grandma22)},
                {findViewById(R.id.gridLayout_IMG_grandma30),
                findViewById(R.id.gridLayout_IMG_grandma31),
                findViewById(R.id.gridLayout_IMG_grandma32)},
                {findViewById(R.id.gridLayout_IMG_grandma40),
                findViewById(R.id.gridLayout_IMG_grandma41),
                findViewById(R.id.gridLayout_IMG_grandma42)}};

        main_IMG_player = new ShapeableImageView[] {
                findViewById(R.id.gridLayout_IMG_dror50),
                findViewById(R.id.gridLayout_IMG_dror51),
                findViewById(R.id.gridLayout_IMG_dror52)};

        main_IMG_crash = findViewById(R.id.main_IMG_father);

        Glide
                .with(this)
                .load(R.drawable.ic_background_haunted_house)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(main_IMG_background);
    }
}