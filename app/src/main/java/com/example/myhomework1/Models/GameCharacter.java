package com.example.myhomework1.Models;

import com.example.myhomework1.Logic.GameManager;

public abstract class GameCharacter {

    protected int currentRow;
    protected int currentCol;
    protected int maxRowsIndex;
    protected int maxColsIndex;

    public GameCharacter (int maxRowsIndex, int maxColsIndex) { // maxRowsIndex=5 maxColsIndex=4
        this.maxRowsIndex = maxRowsIndex;
        this.maxColsIndex = maxColsIndex;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public int getCurrentCol() {
        return currentCol;
    }

    public void setCurrentCol(int currentCol) {
        this.currentCol = currentCol;
    }

    public int getMaxRows() {
        return maxRowsIndex;
    }

    public void setMaxRows(int maxRows) {
        this.maxRowsIndex = maxRows;
    }

    public int getMaxCols() {
        return maxColsIndex;
    }

    public void setMaxCols(int maxCols) {
        this.maxColsIndex = maxCols;
    }

}
