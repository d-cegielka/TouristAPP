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
    private Coordinate coordinate;

    public Pressure(Context context, Coordinate coordinate) {
        app = (AppCompatActivity) context;
        this.coordinate = coordinate;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        pressureTextView = app.findViewById(R.id.pressureView);
        if(pressureSensor == null){
            pressureTextView.setText(" niedostępny");
            coordinate.isAvailableAltitudeFromPressureSensor = false;
        } else {
            coordinate.isAvailableAltitudeFromPressureSensor = true;
        }
    }

    public void start(){
        sensorManager.registerListener(this,pressureSensor,SensorManager.SENSOR_DELAY_UI);
    }

    public void stop(){
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //float[] value = event.values;
        pressureValue = event.values[0];
        pressureTextView.setText(String.format("%.1f hPa", pressureValue));
        coordinate.updateAltitude(SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressureValue));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
