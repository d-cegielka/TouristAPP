package com.sw.touristapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import static java.lang.Math.round;
import static java.lang.Math.toDegrees;

/**
 * Klasa odpowiedzialna za funkcjonalność kompasu.
 */
public class Compass implements SensorEventListener {
    AppCompatActivity app;
    private final SensorManager sensorManager;
    private final Sensor accelerometerSensor;
    private final Sensor geoMagneticSensor;
    private final TextView angleView;
    private final ImageView pointerView;
    private final EditText samplingFrequencyField;
    private int samplingPeriodUs = 20000;
    private float azimuth;
    private FileWriter accWriter, geoWriter;
    private File accFile, geoFile;
    private static final String TAG = "Compass";

    private final float[] gravity = new float[3];
    private final float[] geoMagnetic = new float[3];
    private final float[] rotationMatrix = new float[9];

    /**
     * Konstruktor parametrowy kompasu.
     *
     * @param context kontekst aplikacji
     */
    public Compass(Context context) {
        app = (AppCompatActivity) context;
        File logsDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "logs");
       if (!logsDirectory.exists())
            logsDirectory.mkdirs();
        angleView = app.findViewById(R.id.angleView);
        pointerView = app.findViewById(R.id.compassPointer);
        samplingFrequencyField = app.findViewById(R.id.samplingFrequencyField);
        Button setSamplingFrequencyButton = app.findViewById(R.id.setSamplingFrequencyButton);
        setSamplingFrequencyButton.setOnClickListener(v -> {
            if (!samplingFrequencyField.getText().toString().isEmpty()) {
                samplingPeriodUs = Integer.parseInt(samplingFrequencyField.getText().toString()) * 1000;
                stop();
                start();
            }
        });
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        geoMagneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    /**
     * Rejestracja wymaganych czujników (akceleremoter i magnetometr).
     */
    public void start() {
        sensorManager.registerListener(this, accelerometerSensor, samplingPeriodUs);
        sensorManager.registerListener(this, geoMagneticSensor, samplingPeriodUs);
    }

    /**
     * Wyrejestrowanie wszystkich zarejestrowanych czujników.
     */
    public void stop() {
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    /**
     * Wywoływana podczas wznowienia aktywności aplikacji.
     * Metoda odpowiedzialna jest za tworzenie plików przechowujących logi akcelerometru i magnetometru.
     */
    public void resume() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssZ", Locale.getDefault());
        if (accFile == null || (accFile.length() / 1024) > 2048)
            if (accelerometerSensor != null)
                accFile = new File(Environment.getExternalStorageDirectory() + "/Documents/logs/", "log_acc_" + sdf.format(System.currentTimeMillis()) + ".csv");
        if (geoFile == null || (geoFile.length() / 1024) > 2048)
            if (geoMagneticSensor != null)
                geoFile = new File(Environment.getExternalStorageDirectory() + "/Documents/logs/", "log_geo_" + sdf.format(System.currentTimeMillis()) + ".csv");
        try {
            if (accFile != null) {
                accWriter = new FileWriter(accFile, true);
                if (accFile.length() == 0) accWriter.write("Time(milliseconds),x,y,z\n");
            }
            if (geoFile != null) {
                geoWriter = new FileWriter(geoFile, true);
                if (geoFile.length() == 0) geoWriter.write("Time(milliseconds),x,y,z\n");
            }
        } catch (IOException e) {
            Log.w(TAG, Objects.requireNonNull(e.getLocalizedMessage()));
        }
    }

    /**
     * Wywoływana podczas zatrzymania aktywności aplikacji.
     * Metoda odpowiedzialna jest za zapis plików przechowujących logi akcelerometru i magnetometru.
     */
    public void pause() {
        if (accWriter != null) {
            try {
                if (accFile != null)
                    accWriter.close();
                if (geoFile != null)
                    geoWriter.close();
            } catch (IOException e) {
                Log.w(TAG, Objects.requireNonNull(e.getLocalizedMessage()));
            }
        }
    }

    /**
     * Zapis danych do pliku
     *
     * @param writer FileWriter przeznaczony do pisania strumieni znaków w pliku
     * @param data   strumień znaków do zapisania
     */
    private void saveData(FileWriter writer, String data) {
        try {
            writer.write(data);
        } catch (IOException e) {
            Log.w(TAG, Objects.requireNonNull(e.getLocalizedMessage()));
        }
    }

    /**
     * Aktualizacja azymutu w UI.
     *
     * @param newAzimuth nowy wartość azymutu do aktualizacji
     */
    private void changeUI(float newAzimuth) {
        angleView.setText(round(normalizeDegree(-newAzimuth)) + "°");
        Animation compassImageRotate = new RotateAnimation(azimuth, newAzimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        azimuth = newAzimuth;

        compassImageRotate.setDuration(500);
        compassImageRotate.setRepeatCount(0);
        compassImageRotate.setFillAfter(true);
        pointerView.setAnimation(compassImageRotate);
    }

    /**
     * Metoda zostaje wywołana gdy zmieni się wartość czujnika.
     *
     * @param event zdarzenie sensora
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.96f;
        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
                if (accWriter != null)
                    saveData(accWriter, System.currentTimeMillis() + "," + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n");
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                geoMagnetic[0] = alpha * geoMagnetic[0] + (1 - alpha) * event.values[0];
                geoMagnetic[1] = alpha * geoMagnetic[1] + (1 - alpha) * event.values[1];
                geoMagnetic[2] = alpha * geoMagnetic[2] + (1 - alpha) * event.values[2];
                if (geoWriter != null)
                    saveData(geoWriter, System.currentTimeMillis() + "," + event.values[0] + "," + event.values[1] + "," + event.values[2] + "\n");
            }

            if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geoMagnetic)) {
                final float[] orientation = new float[3];
                SensorManager.getOrientation(rotationMatrix, orientation);
                app.runOnUiThread(() -> changeUI((float) -toDegrees(orientation[0])));
            }
        }
    }

    /**
     * Normalizacja azymutu (0-360 stopni).
     *
     * @param value azymut do normalizacji
     * @return azymut po normalizacji
     */
    private float normalizeDegree(float value) {
        if (value >= 0.0f && value <= 180.0f) {
            return value;
        } else {
            return 180 + (180 + value);
        }
    }

    /**
     * Metoda zostaje wywołana gdy zmieni się dokładność zarejestrowanego czujnika.
     *
     * @param sensor   obiekt sensora
     * @param accuracy nowa dokładność czujnika
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
