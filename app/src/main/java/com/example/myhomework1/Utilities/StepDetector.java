package com.example.myhomework1.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.example.myhomework1.Interfaces.StepCallback;

public class StepDetector {
    private Sensor sensor;

    private SensorManager sensorManager;

    private StepCallback stepCallback;
    private long timestamp = 0;

    private SensorEventListener sensorEventListener;

    public StepDetector(Context context, StepCallback stepCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.stepCallback = stepCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float z = event.values[1];

                calculateStep(x, z);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void calculateStep(float x, float z) {
        if (System.currentTimeMillis() - timestamp > 300) {
            timestamp = System.currentTimeMillis();
            if (x > 2.0) {
                if (stepCallback != null)
                    stepCallback.stepLeft();
            }
            if (x < -2.0) {
                if (stepCallback != null)
                    stepCallback.stepRight();
            }
            if (z > 9.0) {
                if (stepCallback != null)
                    stepCallback.speedDown();
            }
            if (z < -1.0) {
                if (stepCallback != null)
                    stepCallback.speedUp();
            }
        }
    }

    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
