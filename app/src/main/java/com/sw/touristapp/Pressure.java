package com.sw.touristapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Pressure implements SensorEventListener {
    private AppCompatActivity app;
    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private TextView pressureTextView;
    float pressureValue;

    /**
     * Konstruktor parametrowy baromatru.
     * @param context kontekst aplikacji
     */
    public Pressure(Context context) {
        app = (AppCompatActivity) context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        pressureTextView = app.findViewById(R.id.pressureView);
        if (pressureSensor == null) {
            pressureTextView.setText("N/A");
        }
    }

    /**
     * Rejestracja wymaganych czujników (barometr).
     */
    public void start(){
        sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * Wyrejestrowanie wszystkich zarejestrowanych czujników.
     */
    public void stop(){
        sensorManager.unregisterListener(this);
    }

    /**
     * Metoda zostaje wywołana gdy zmieni się wartość czujnika.
     * @param event zdarzenie sensora
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        pressureValue = event.values[0];
        pressureTextView.setText(String.format("%.1f hPa", pressureValue));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
