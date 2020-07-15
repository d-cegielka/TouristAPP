package com.sw.touristapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Compass compass;
    private Flashlight flashlight;
    private ThemeManager themeManager;
    private Pressure pressure;
    Coordinate coordinate;
    private SignalSOS signalSOS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compass = new Compass(this);
        flashlight = new Flashlight(this);
        themeManager = new ThemeManager(this);
        coordinate = new Coordinate(this);
        pressure = new Pressure(this, coordinate);
        signalSOS = new SignalSOS(this, flashlight);
    }

    @Override
    protected void onStart() {
        super.onStart();
        compass.start();
        themeManager.start();
        pressure.start();
        coordinate.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
        themeManager.start();
        pressure.start();
        coordinate.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
        themeManager.stop();
        pressure.stop();
        coordinate.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compass.stop();
        themeManager.stop();
        pressure.stop();
        coordinate.stop();
    }

}