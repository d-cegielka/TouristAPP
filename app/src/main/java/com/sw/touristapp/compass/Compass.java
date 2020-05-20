package com.sw.touristapp.compass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Compass implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometrSensor;
    private Sensor geoMagneticSensor;



    public Compass(Context context) {
        sensorManager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometrSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        geoMagneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
