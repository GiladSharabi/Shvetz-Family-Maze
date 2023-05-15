package com.example.myhomework1.Models;

public class Record {
    String name;
    private int score;
    private double xPosition;
    private double yPosition;

    public Record(String name, int score, double xPosition, double yPosition) {
        this.name = name;
        this.score = score;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public double getXPosition() {
        return xPosition;
    }
    public void setX(double x) {
        this.xPosition = x;
    }
    public double getYPosition() {
        return yPosition;
    }
    public void setY(double y) {
        this.yPosition = y;
    }

    @Override
    public String toString() {
        return "Record{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", xPosition=" + xPosition +
                ", yPosition=" + yPosition +
                '}';
    }
}
