package com.sw.touristapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static java.lang.Math.*;

public class Compass implements SensorEventListener {
    AppCompatActivity app;
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor geoMagneticSensor;
    private TextView angleView;
    private ImageView pointerView;
    private float azimuth;

    private float[] gravity = new float[3];
    private float[] geoMagnetic = new float[3];
    private float[] rotationMatrix = new float[9];

    public Compass(Context context) {
        app = (AppCompatActivity) context;
        angleView = app.findViewById(R.id.angleView);
        pointerView = app.findViewById(R.id.compassPointer);
        sensorManager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        geoMagneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start() {
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, geoMagneticSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    private void changeUI(float newAzimuth){
        angleView.setText(round(normalizeDegree(-newAzimuth)) + "°");
        Animation compassImageRotate = new RotateAnimation(azimuth, newAzimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        azimuth = newAzimuth;

        compassImageRotate.setDuration(500);
        compassImageRotate.setRepeatCount(0);
        compassImageRotate.setFillAfter(true);
        pointerView.setAnimation(compassImageRotate);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.96f;
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                //gravity = event.values;

                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                //geoMagnetic = event.values;

                geoMagnetic[0] = alpha * geoMagnetic[0] + (1 - alpha) * event.values[0];
                geoMagnetic[1] = alpha * geoMagnetic[1] + (1 - alpha) * event.values[1];
                geoMagnetic[2] = alpha * geoMagnetic[2] + (1 - alpha) * event.values[2];
            }

            if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geoMagnetic)) {
                final float[] orientation = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientation);
                app.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changeUI((float) -toDegrees(orientation[0]));
                    }
                });
            }
        }
    }

    private float normalizeDegree(float value) {
        if (value >= 0.0f && value <= 180.0f) {
            return value;
        } else {
            return 180 + (180 + value);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
