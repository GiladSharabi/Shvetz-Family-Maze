package com.example.myhomework1.Activities;
import static com.example.myhomework1.Activities.MenuActivity.FAST_SPEED;
import static com.example.myhomework1.Activities.MenuActivity.SLOW_SPEED;
import static com.example.myhomework1.Activities.MenuActivity.isButtonMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.myhomework1.Interfaces.StepCallback;
import com.example.myhomework1.Logic.GameManager;
import com.example.myhomework1.Models.eMove;
import com.example.myhomework1.R;
import com.example.myhomework1.Utilities.SignalGenerator;
import com.example.myhomework1.Utilities.StepDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
public class MainActivity extends AppCompatActivity {
    private final int ROWS = 6;
    private final int COLS = 5;
    private final int START_LIFE = 3;
    public final int VIBRATE_LENGTH = 500;
    private AppCompatImageView main_IMG_background;
    private FloatingActionButton main_FAB_left;
    private FloatingActionButton main_FAB_right;
    private MaterialTextView main_LBL_score;
    private ShapeableImageView[] main_IMG_hearts; // the three hearts layout
    private ShapeableImageView[][] main_IMG_enemy; // enemy matrix
    private ShapeableImageView[][] main_IMG_coin; // coin matrix
    private ShapeableImageView[] main_IMG_player; // player array
    private GridLayout gameBoard;
    private ShapeableImageView main_IMG_crash; // crash event image
    private boolean isCrash = false;
    private GameManager gameManager;
    private final String toastText = "DROR!";
    private MediaPlayer drorSound;
    private MediaPlayer coinSound;
    private MediaPlayer gameOverSound;
    public static Intent recordIntent;
    public static Intent gameOverIntent;
    public static final String FINAL_SCORE_KEY = "FINAL_SCORE_KEY";
    private Intent previousIntent;
    private StepDetector stepDetector;
    private int delay = SLOW_SPEED;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        drorSound = MediaPlayer.create(this, R.raw.dror_record);
        gameOverSound = MediaPlayer.create(this,R.raw.erch_record);
        coinSound = MediaPlayer.create(this,R.raw.micky_puke);
        gameManager = new GameManager(ROWS,COLS,START_LIFE);
        isCrash = false;
        previousIntent = getIntent();
        delay = previousIntent.getIntExtra(MenuActivity.DELAY_STATUS,800);
        initStepDetector();
        initViews();
        startGameInView();
        startTimer();
    }
    private void initViews() {
        //Objects.requireNonNull(getSupportActionBar()).setTitle("Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        main_FAB_left.setOnClickListener(v -> movePlayerInView(eMove.left));
        main_FAB_right.setOnClickListener(v -> movePlayerInView(eMove.right));
        if (!isButtonMode) {
            main_FAB_left.setVisibility(View.INVISIBLE);
            main_FAB_right.setVisibility(View.INVISIBLE);
            stepDetector.start();
        }
        else
            stepDetector.stop();
        for (int i=0;i<main_IMG_enemy.length;i++) {
            for (int j=0;j<main_IMG_enemy[i].length;j++) {
                main_IMG_enemy[i][j].setVisibility(View.INVISIBLE);
            }
        }
        for (int i=0;i<main_IMG_coin.length;i++) {
            for (int j=0;j<main_IMG_coin[i].length;j++) {
                main_IMG_coin[i][j].setVisibility(View.INVISIBLE);
            }
        }
        int playerCol = gameManager.getPlayer().getCurrentCol();
        for (int i=0;i<main_IMG_player.length;i++) {
            //if (i != playerCol)
                main_IMG_player[i].setVisibility(View.INVISIBLE);
        }
        main_IMG_crash.setVisibility(View.INVISIBLE);
        recordIntent = new Intent(this,RecordsActivity.class);
        gameOverIntent = new Intent(this, GameOverActivity.class);
    }

    private void initStepDetector() {
        stepDetector = new StepDetector(this, new StepCallback() {
            @Override
            public void stepLeft() {
                movePlayerInView(eMove.left);
            }
            @Override
            public void stepRight() {
                movePlayerInView(eMove.right);
            }
            @Override
            public void speedUp() {
                setDelay(FAST_SPEED);
                SignalGenerator.getInstance().toast("Speed Up",Toast.LENGTH_SHORT);
            }
            @Override
            public void speedDown() {
                setDelay(SLOW_SPEED);
                SignalGenerator.getInstance().toast("Speed Down",Toast.LENGTH_SHORT);
            }
        });
    }
    public void setDelay(int delay) {
        this.delay = delay;
    }

    private void movePlayerInView(eMove move) {
        main_IMG_player[gameManager.getPlayer().getCurrentCol()].setVisibility(View.INVISIBLE);
        gameManager.moveThePlayer(move);
        main_IMG_player[gameManager.getPlayer().getCurrentCol()].setVisibility(View.VISIBLE);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (!isButtonMode)
            stepDetector.stop();
        stopTimer();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!isButtonMode)
            stepDetector.start();
    }
    private void stopTimer() {
        handler.removeCallbacks(runnable);
    }
    private void startTimer() {
        runnable = new Runnable() {
            @Override
            public void run() {
                gameManager.nextMoveBoard();
                refreshUI();
                handler.postDelayed(this, delay);
            }
        };
        handler.post(runnable);
    }
    private void startGameInView() {
        gameBoard.setVisibility(View.VISIBLE);
        main_IMG_player[gameManager.getPlayer().getCurrentCol()].setVisibility(View.VISIBLE);
        refreshUI();
    }
    private void refreshUI() {
        if (isCrash) {
            main_IMG_crash.setVisibility(View.INVISIBLE);
            isCrash = false;
        }
        moveAllCharactersInView();
        checkCrashEvent();
        checkCoinEvent();
        main_LBL_score.setText("" + gameManager.getCurrentScore());
    }

    private void checkCoinEvent() {
        if (gameManager.isCoinOnPlayer()) {
            try {
                coinSound.start();
            } catch (IllegalStateException e) {
                // do nothing
            }
        }
    }
    private void moveAllCharactersInView() {
        moveEnemiesInView();
        moveCoinsInView();
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
    private void moveCoinsInView() {
        int coinCol;

        for (int i=0;i<ROWS-1;i++) {
            for (int j=0; j<COLS; j++) {
                main_IMG_coin[i][j].setVisibility(View.INVISIBLE);
            }
        }

        for (int i=0; i<ROWS-1; i++) {
            coinCol = gameManager.getCoinColInRowIndex(i);
            if (coinCol>=0)
                main_IMG_coin[i][coinCol].setVisibility(View.VISIBLE);
        }
    }
    private void checkCrashEvent() {
        if (gameManager.isEnemyOnPlayer()) {
            crashEvent();
        }
    }
    private void crashEvent() {
        if (gameManager.isGameOver()) { // Game Over - sound+switch to game over activity
            try {
                gameOverSound.start();
                stopTimer();
                openGameOverActivity();
            } catch (IllegalStateException e) {
                // do nothing
            }
        }
        else { // normal crash sound
            try {
                drorSound.start();
            } catch (IllegalStateException e) {
                // do nothing
            }
        }
        SignalGenerator.getInstance().vibrate(VIBRATE_LENGTH);
        SignalGenerator.getInstance().toast(toastText,Toast.LENGTH_SHORT);
        isCrash = true;
        main_IMG_crash.setVisibility(View.VISIBLE);
        main_IMG_hearts[START_LIFE-gameManager.getLife()-1].setVisibility(View.INVISIBLE);
    }

    private void openGameOverActivity() {
        gameOverIntent.putExtra(FINAL_SCORE_KEY,gameManager.getCurrentScore());
        startActivity(gameOverIntent);
        finish();
    }

    private void findViews() {
        main_IMG_background = findViewById(R.id.main_IMG_background);
        main_FAB_left = findViewById(R.id.arrow_FAB_left);
        main_FAB_right = findViewById(R.id.arrow_FAB_right);
        main_LBL_score = findViewById(R.id.main_LBL_score);
        main_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3)};

        gameBoard = findViewById(R.id.main_GRID_LAYOUT_board);
        main_IMG_enemy = new ShapeableImageView[][] {
                {findViewById(R.id.gridLayout_IMG_grandma00),
                        findViewById(R.id.gridLayout_IMG_grandma01),
                        findViewById(R.id.gridLayout_IMG_grandma02),
                        findViewById(R.id.gridLayout_IMG_grandma03),
                        findViewById(R.id.gridLayout_IMG_grandma04)},
                {findViewById(R.id.gridLayout_IMG_grandma10),
                        findViewById(R.id.gridLayout_IMG_grandma11),
                        findViewById(R.id.gridLayout_IMG_grandma12),
                        findViewById(R.id.gridLayout_IMG_grandma13),
                        findViewById(R.id.gridLayout_IMG_grandma14)},
                {findViewById(R.id.gridLayout_IMG_grandma20),
                        findViewById(R.id.gridLayout_IMG_grandma21),
                        findViewById(R.id.gridLayout_IMG_grandma22),
                        findViewById(R.id.gridLayout_IMG_grandma23),
                        findViewById(R.id.gridLayout_IMG_grandma24)},
                {findViewById(R.id.gridLayout_IMG_grandma30),
                        findViewById(R.id.gridLayout_IMG_grandma31),
                        findViewById(R.id.gridLayout_IMG_grandma32),
                        findViewById(R.id.gridLayout_IMG_grandma33),
                        findViewById(R.id.gridLayout_IMG_grandma34)},
                {findViewById(R.id.gridLayout_IMG_grandma40),
                        findViewById(R.id.gridLayout_IMG_grandma41),
                        findViewById(R.id.gridLayout_IMG_grandma42),
                        findViewById(R.id.gridLayout_IMG_grandma43),
                        findViewById(R.id.gridLayout_IMG_grandma44)}};
        main_IMG_coin = new ShapeableImageView[][] {
                {findViewById(R.id.gridLayout_IMG_coin00),
                        findViewById(R.id.gridLayout_IMG_coin01),
                        findViewById(R.id.gridLayout_IMG_coin02),
                        findViewById(R.id.gridLayout_IMG_coin03),
                        findViewById(R.id.gridLayout_IMG_coin04)},
                {findViewById(R.id.gridLayout_IMG_coin10),
                        findViewById(R.id.gridLayout_IMG_coin11),
                        findViewById(R.id.gridLayout_IMG_coin12),
                        findViewById(R.id.gridLayout_IMG_coin13),
                        findViewById(R.id.gridLayout_IMG_coin14)},
                {findViewById(R.id.gridLayout_IMG_coin20),
                        findViewById(R.id.gridLayout_IMG_coin21),
                        findViewById(R.id.gridLayout_IMG_coin22),
                        findViewById(R.id.gridLayout_IMG_coin23),
                        findViewById(R.id.gridLayout_IMG_coin24)},
                {findViewById(R.id.gridLayout_IMG_coin30),
                        findViewById(R.id.gridLayout_IMG_coin31),
                        findViewById(R.id.gridLayout_IMG_coin32),
                        findViewById(R.id.gridLayout_IMG_coin33),
                        findViewById(R.id.gridLayout_IMG_coin34)},
                {findViewById(R.id.gridLayout_IMG_coin40),
                        findViewById(R.id.gridLayout_IMG_coin41),
                        findViewById(R.id.gridLayout_IMG_coin42),
                        findViewById(R.id.gridLayout_IMG_coin43),
                        findViewById(R.id.gridLayout_IMG_coin44)}};

        main_IMG_player = new ShapeableImageView[] {
                findViewById(R.id.gridLayout_IMG_dror50),
                findViewById(R.id.gridLayout_IMG_dror51),
                findViewById(R.id.gridLayout_IMG_dror52),
                findViewById(R.id.gridLayout_IMG_dror53),
                findViewById(R.id.gridLayout_IMG_dror54)};

        main_IMG_crash = findViewById(R.id.main_IMG_father);

        Glide
                .with(this)
                .load(R.drawable.background_haunted_house)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(main_IMG_background);
    }
}