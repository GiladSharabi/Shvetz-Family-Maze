package com.example.myhomework1.Models;

public class Coin extends GameCharacter {

    public Coin(int maxRowIndex, int maxColIndex, int currentCol) {
        super(maxRowIndex,maxColIndex);
        super.currentRow = 0;
        super.currentCol = currentCol;
    }

    public boolean moveCoin() {
        if (this.currentRow == this.maxRowsIndex) {
            return false;
        }
        this.currentRow++;
        return true;
    }
}
