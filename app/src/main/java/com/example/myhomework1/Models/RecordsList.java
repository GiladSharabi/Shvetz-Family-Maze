package com.example.myhomework1.Models;

import java.util.ArrayList;

public class RecordsList {
    public static final int RECORD_LIST_SIZE = 10;
    private ArrayList<Record> list = new ArrayList<>(RECORD_LIST_SIZE);
    public RecordsList() {
    }
    public ArrayList<Record> getList() {
        return list;
    }

    public void setList(ArrayList<Record> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        for (int i=0;i<list.size();i++) {
            s.append(list.get(i).toString());
        }
        return s.toString();
    }
}
