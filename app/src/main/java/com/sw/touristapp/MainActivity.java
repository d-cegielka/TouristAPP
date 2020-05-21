package com.sw.touristapp;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {
    private Compass compass;
    private Flashlight flashlight;
    private Switch switchTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compass = new Compass(this);
        flashlight = new Flashlight(this);
        switchTheme = (Switch) findViewById(R.id.switch_theme);
    }

    @Override
    protected void onStart() {
        super.onStart();
        compass.run();
        switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeTheme(isChecked);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        compass.stop();
    }

    private void changeTheme(final boolean isChecked){
        if(isChecked){
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else{
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

    }
}