package com.example.myhomework1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.myhomework1.Fragments.ListFragment;
import com.example.myhomework1.Fragments.MapFragment;
import com.example.myhomework1.Interfaces.CallBack_SendClick;
import com.example.myhomework1.Models.Record;
import com.example.myhomework1.R;

public class RecordsActivity extends AppCompatActivity {
    private ListFragment listFragment;
    private MapFragment mapFragment;
    CallBack_SendClick callBack_SendClick = new CallBack_SendClick() {
        @Override
        public void userRecordClicked(Record record) {
            showUserLocation(record);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        initFragments();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        beginTransactions();
    }
    private void showUserLocation(Record record) {
        mapFragment.zoomOnUser(record.getXPosition(),record.getYPosition());
    }

    private void initFragments() {
        listFragment = new ListFragment();
        listFragment.setCallBack(callBack_SendClick);
        mapFragment = new MapFragment();
    }

    private void beginTransactions() {
        getSupportFragmentManager().beginTransaction().add(R.id.records_FRAME_list, listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.records_FRAME_map, mapFragment).commit();
    }
}
