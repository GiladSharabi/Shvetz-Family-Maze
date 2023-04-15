package com.example.myhomework1.Models;

public class Player extends GameCharacter{


    public Player(int maxRowsIndex, int maxColsIndex) { // maxRowsIndex=5 maxColsIndex=2
        super(maxRowsIndex, maxColsIndex);
        this.setCurrentRow(maxRowsIndex);
        this.setCurrentCol(maxColsIndex/2);
    }

    public boolean movePlayer(eMove move) {
        if (move == eMove.left) {
            if (super.getCurrentCol() <= 0)
                return false;
            this.currentCol--;
        }
        else if (move == eMove.right) {
            if (super.getCurrentCol() >= this.maxColsIndex)
                return false;
            this.currentCol++;
        }
        else
            return false;
        return true;
    }
}
