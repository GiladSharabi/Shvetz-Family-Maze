package com.example.myhomework1.Utilities;

import static android.location.LocationManager.GPS_PROVIDER;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class  GPS implements LocationListener {
    public static int MY_PERMISSIONS_REQUEST_LOCATION=1;

    // default location: eilat
    private double x = 29.557445;
    private double y = 34.952294;
    private LocationManager locationManager;

    public GPS(AppCompatActivity activity){
        locationManager= (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION
            );
        }
        locationManager.requestLocationUpdates(GPS_PROVIDER,0,0,this);
    }
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            x = location.getLatitude();
            y = location.getLongitude();
            stopLocationTrack();
        }
    }

    public void updateLocation(AppCompatActivity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);
            if (location != null) {
                x = location.getLatitude();
                y = location.getLongitude();
                stopLocationTrack();
            }
        }
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    private void stopLocationTrack() {
        locationManager.removeUpdates(this);
    }
}
