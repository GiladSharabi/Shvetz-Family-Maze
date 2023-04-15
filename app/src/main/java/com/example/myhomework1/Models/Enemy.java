package com.example.myhomework1.Models;

public class Enemy extends GameCharacter {

    public Enemy(int maxRowIndex, int maxColIndex, int currentCol) {
        super(maxRowIndex, maxColIndex);
        super.currentRow = 0;
        super.currentCol = currentCol;
    }

    public boolean moveEnemy() {
        if (this.currentRow == this.maxRowsIndex) {
            return false;
        }
        this.currentRow++;
        return true;
    }
}
