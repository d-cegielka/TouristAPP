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
    private AppCompatActivity app;
    private CameraManager cameraManager;
    private String cameraId;
    private ToggleButton flashlightButton;
    private Vibrator vibrator;

    public Flashlight(Context context) {
        app = (AppCompatActivity) context;
        cameraManager =  (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        flashlightButton = app.findViewById(R.id.flashlightButton);

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        final boolean isFlashAvailable = app.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

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

    private void switchFlashLight(boolean status) {
        try {
            cameraManager.setTorchMode(cameraId,status);
        }catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


}
