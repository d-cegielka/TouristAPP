package com.sw.touristapp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Compass compass;
    private Flashlight flashlight;
    private ThemeManager themeManager;
    private Pressure pressure;
    private Coordinates coordinates;
    private SignalSOS signalSOS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compass = new Compass(this);
        flashlight = new Flashlight(this);
        themeManager = new ThemeManager(this);
        pressure = new Pressure(this);
        coordinates = new Coordinates(this);
        signalSOS = new SignalSOS(this, flashlight);
    }

    @Override
    protected void onStart() {
        super.onStart();
        compass.start();
        themeManager.start();
        pressure.start();
        coordinates.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
        themeManager.start();
        pressure.start();
        coordinates.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
        themeManager.stop();
        pressure.stop();
        coordinates.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compass.stop();
        themeManager.stop();
        pressure.stop();
        coordinates.stop();
    }

}