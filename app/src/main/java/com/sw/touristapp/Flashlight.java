package com.sw.touristapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Vibrator;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import static android.content.Context.VIBRATOR_SERVICE;

public class Flashlight {
    private CameraManager cameraManager;
    private String cameraId;
    private Vibrator vibrator;
    final boolean isFlashAvailable;
    final ToggleButton flashlightButton;

    /**
     * Konstruktor parametrowy latarki.
     * @param context kontekst aplikacji.
     */
    public Flashlight(Context context) {
        AppCompatActivity app = (AppCompatActivity) context;
        cameraManager =  (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        flashlightButton = app.findViewById(R.id.flashlightButton);

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        isFlashAvailable = app.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        flashlightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchFlashLight(isChecked);
            }
        });

        if(!isFlashAvailable) {
            flashlightButton.setEnabled(false);
        }

        flashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
            }
        });
    }

    /**
     * Metoda służąca do włączenia latarki.
     * @param status wartość true - włączenie latarki, wartość false - wyłączenie latarki.
     */
    void switchFlashLight(boolean status) {
        try {
            cameraManager.setTorchMode(cameraId, status);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
