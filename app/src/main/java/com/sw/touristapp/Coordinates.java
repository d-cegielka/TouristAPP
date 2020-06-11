package com.sw.touristapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Coordinates implements LocationListener {
    private AppCompatActivity app;
    private LocationManager locationManager;
    private TextView coordinatesTextView;
    private TextView altitudeTextView;


    public Coordinates(Context context) {
        app = (AppCompatActivity) context;
        locationManager = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);
        coordinatesTextView = app.findViewById(R.id.coordinatesView);
        altitudeTextView = app.findViewById(R.id.altitudeView);

    }

    public void start() {
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void stop(){
        locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        coordinatesTextView.setText(String.format("%.4f, %.4f", latitude, longitude));
        altitudeTextView.setText(String.format("%.1f",location.getAltitude()));
    }


}
