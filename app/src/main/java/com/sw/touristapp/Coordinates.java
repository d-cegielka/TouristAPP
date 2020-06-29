package com.sw.touristapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Coordinates implements LocationListener {
    private AppCompatActivity app;
    private LocationManager locationManager;
    private TextView coordinatesTextView;
    private TextView altitudeTextView;
    private static int ACCESS_FINE_LOCATION_CODE = 50;

    public Coordinates(Context context) {
        app = (AppCompatActivity) context;
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(app, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CODE);
        }

        locationManager = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);
        coordinatesTextView = app.findViewById(R.id.coordinatesView);
        altitudeTextView = app.findViewById(R.id.altitudeView);
    }

    public void start() {
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            coordinatesTextView.setText("Brak uprawnień!");
            altitudeTextView.setText("Brak uprawnień!");
            return;
        } else {
            coordinatesTextView.setText("Współrzędne niedostępne!");
            altitudeTextView.setText("N/A");
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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
