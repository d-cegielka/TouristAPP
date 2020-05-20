package com.sw.touristapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.sw.touristapp.MainActivity;

public class Compass implements SensorEventListener {
    private Context app;
    private SensorManager sensorManager;
    private Sensor accelerometrSensor;
    private Sensor geoMagneticSensor;
    private float azimuth;

    private float[] gravity = new float[3];
    private float[] geoMagnetic = new float[3];
    private float[] rotationMatrix = new float[9];

    public Compass(Context context) {
        app = context;
        sensorManager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometrSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        geoMagneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    void run() {
        sensorManager.registerListener(this, accelerometrSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, geoMagneticSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    void stop() {
        sensorManager.unregisterListener(this);
    }

    void changeUI(final float newAzimuth){
        Animation compassImageRotate = new RotateAnimation(-azimuth, -newAzimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        azimuth = newAzimuth;

        compassImageRotate.setDuration(300);
        compassImageRotate.setRepeatCount(0);
        compassImageRotate.setFillAfter(true);
        Activity activity = (Activity) app;
        ((Activity) app).findViewById(R.id.compassImage).setAnimation(compassImageRotate);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                gravity = event.values;
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                geoMagnetic = event.values;
            }

            if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geoMagnetic)) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientation);
                changeUI((float) Math.toDegrees(orientation[0]));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
