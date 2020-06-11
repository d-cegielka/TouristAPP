package com.sw.touristapp;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import static android.content.Context.SENSOR_SERVICE;

public class ThemeManager implements SensorEventListener {
    private AppCompatActivity app;
    private ToggleButton autoThemeButton;
    private Switch nightThemeSwitch;
    private SensorManager sensorManager;
    private Sensor lightSensor;

    public ThemeManager(Context context) {
        app = (AppCompatActivity) context;
        autoThemeButton = app.findViewById(R.id.autoThemeButton);
        nightThemeSwitch = app.findViewById(R.id.nightThemeSwitch);

        int currentNightMode = app.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(currentNightMode == Configuration.UI_MODE_NIGHT_YES){
            nightThemeSwitch.setChecked(true);
        }

        //light sensor
        sensorManager = (SensorManager) app.getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        nightThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                changeTheme(isChecked);
            }
        });

        autoThemeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    nightThemeSwitch.setClickable(false);
                } else
                    nightThemeSwitch.setClickable(true);
            }
        });
    }

    public void start() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    private void changeTheme(final boolean isChecked){
        if(isChecked){
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //app.getDelegate().onStop();
            app.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //app.getDelegate().onStart();
        } else{
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //app.getDelegate().onStop();
            app.getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //app.getDelegate().onStart();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            if(event.values[0] < 50f) {
                if(autoThemeButton.isChecked()) {
                    nightThemeSwitch.setChecked(true);
                }
            }
            else {
                if(autoThemeButton.isChecked()) {
                    nightThemeSwitch.setChecked(false);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
