package com.example.myhomework1;

import android.app.Application;

import com.example.myhomework1.Utilities.MySP;
import com.example.myhomework1.Utilities.SignalGenerator;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MySP.init(this);
        SignalGenerator.init(this);
    }
}
