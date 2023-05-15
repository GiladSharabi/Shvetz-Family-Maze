package com.example.myhomework1.Logic;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.example.myhomework1.Models.Coin;
import com.example.myhomework1.Models.Enemy;
import com.example.myhomework1.Models.GameCharacter;
import com.example.myhomework1.Models.Player;
import com.example.myhomework1.Models.eMove;

public class GameManager {

    private int rows;
    private int cols;
    private int life;
    private final int COIN_RATE = 4;
    public final int COIN_SCORE = 3;
    private int currentScore = 0;



    private GameCharacter gameBoard[][];
    private Player player;

    public GameManager(int rows, int cols ,int life) { // rows=6 cols=3 life=3
        this.rows = rows;
        this.cols = cols;
        this.life = life;
        this.gameBoard = new GameCharacter[rows][cols]; // matrix 6x3
        this.player = new Player(rows-1,cols-1); //  maxRowIndex=5 maxColIndex=2
        startGame();
    }

    public void startGame() {
        createNewEnemyOnBoard();
        gameBoard[this.getPlayer().getCurrentRow()][this.getPlayer().getCurrentCol()] = this.getPlayer();
    }

    public boolean isGameOver() {
        return (life == 0);
    }

    public int getEnemyColInRowIndex(int rowIndex) {
        for (int j=0;j<cols;j++) {
            if (gameBoard[rowIndex][j] instanceof Enemy)
                return j;
        }
        return -1;
    }
    public int getCoinColInRowIndex(int rowIndex) {
        for (int j=0;j<cols;j++) {
            if (gameBoard[rowIndex][j] instanceof Coin)
                return j;
        }
        return -1;
    }

    public void nextMoveAllEnemies () {
        for (int i=rows-1;i>=0;i--) {
            for (int j=cols-1;j>=0;j--) {
                if (gameBoard[i][j] instanceof Enemy) {
                    if (((Enemy)gameBoard[i][j]).moveEnemy()) // this function also check if enemy in the last row
                        gameBoard[i+1][j] = gameBoard[i][j];
                    gameBoard[i][j] = null;
                }
            }
        }
        createNewEnemyOnBoard();
    }
    public void nextMoveAllCoins () {
        for (int i=rows-1;i>=0;i--) {
            for (int j=cols-1;j>=0;j--) {
                if (gameBoard[i][j] instanceof Coin) {
                    if (((Coin)gameBoard[i][j]).moveCoin()) // this function also check if enemy in the last row
                        gameBoard[i+1][j] = gameBoard[i][j];
                    gameBoard[i][j] = null;
                }
            }
        }

        int isCreateNewCoin = (int) (Math.random() * COIN_RATE), randomCol;
        if (isCreateNewCoin == 0) {
            do {
                randomCol = (int) (Math.random() * cols);
            } while (gameBoard[0][randomCol] != null);
            gameBoard[0][randomCol] = new Coin(rows - 1, cols - 1, randomCol);
        }
    }
    public void nextMoveBoard() {
        nextMoveAllEnemies();
        currentScore++;
        nextMoveAllCoins();
    }

    private void createNewEnemyOnBoard() {
        int randomCol;
        do {
            randomCol = (int) (Math.random() * cols);
        } while (gameBoard[0][randomCol] != null);
        gameBoard[0][randomCol] = new Enemy(rows - 1, cols - 1, randomCol);
    }
    private void createNewCoinOnBoard() {
        int randomCol;
        do {
            randomCol = (int) (Math.random() * cols);
        } while (gameBoard[0][randomCol] != null);
        gameBoard[0][randomCol] = new Coin(rows - 1, cols - 1, randomCol);
    }


    public void moveThePlayer(eMove move) {
        gameBoard[player.getCurrentRow()][player.getCurrentCol()] = null;
        player.movePlayer(move);
        gameBoard[player.getCurrentRow()][player.getCurrentCol()] = player;
    }

    public boolean isEnemyOnPlayer() {
        if (gameBoard[this.player.getCurrentRow()][this.player.getCurrentCol()] instanceof Enemy) {
            if (this.life>0)
                this.life--;
            return true;
        }
        return false;
    }
    public boolean isCoinOnPlayer() {
        if (gameBoard[this.player.getCurrentRow()][this.player.getCurrentCol()] instanceof Coin) {
            currentScore += COIN_SCORE;
            return true;
        }
        return false;
    }




    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    public int getCols() {
        return cols;
    }
    public void setCols(int cols) {
        this.cols = cols;
    }
    public int getLife() {
        return life;
    }
    public void setLife(int life) {
        this.life = life;
    }
    public GameCharacter[][] getGameBoard() {
        return gameBoard;
    }
    public void setGameBoard(GameCharacter[][] gameBoard) {
        this.gameBoard = gameBoard;
    }
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public int getCurrentScore() {
        return currentScore;
    }
    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }


}
