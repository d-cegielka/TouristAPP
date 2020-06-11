package com.sw.touristapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {
    private Compass compass;
    private Flashlight flashlight;
    private ThemeManager themeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compass = new Compass(this);
        flashlight = new Flashlight(this);
        themeManager = new ThemeManager(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        compass.start();
        themeManager.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compass.stop();
        themeManager.stop();
    }
}