package com.sw.touristapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Coordinate implements LocationListener {
    private AppCompatActivity app;
    private LocationManager locationManager;
    private TextView coordinatesTextView;
    private TextView altitudeTextView;
    private double altitudeValueFromLocation;
    boolean isAvailableAltitudeFromPressureSensor;
    private static int ACCESS_FINE_LOCATION_CODE = 50;

    /**
     * Konstruktor parametrowy koordynatów.
     * @param context kontekst aplikacji
     */
    public Coordinate(Context context) {
        app = (AppCompatActivity) context;
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(app, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, ACCESS_FINE_LOCATION_CODE);
        }
        locationManager = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);
        coordinatesTextView = app.findViewById(R.id.coordinatesView);
        altitudeTextView = app.findViewById(R.id.altitudeView);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            createAlertGPSNotAvailable();
        }
    }

    /**
     * Metoda inicjalizująca GPS.
     * Sprawdzane jest pozwolenie na dostęp do dokładnej lokalizacji (jeśli brak to wysyłana jest prośba o uprawnienie),
     * następnie rejestrowane jest żądanie aktualizacji lokalizacji urządzenia.
     */
    public void start() {
        if (ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            coordinatesTextView.setText("Brak uprawnień do GPS!");
            altitudeTextView.setText("Brak uprawnień do GPS!");
            return;
        } else {
            coordinatesTextView.setText("Ustalanie współrzędnych...");
            altitudeTextView.setText("N/A");
        }

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    /**
     * Usunięcie żądania aktualizacji lokalizacji.
     */
    public void stop(){
        locationManager.removeUpdates(this);
    }

    /**
     * Aktualizacja wartości wysokości.
     * Metoda wywoływana przez klasę obsługująca barometr.
     * @param altitudeValueFromPressureSensor wysokość n.p.m. na podstawie ciśnienia atmosferycznego
     */
    void updateAltitude(double altitudeValueFromPressureSensor) {
        synchronized (this) {
            if (isAvailableAltitudeFromPressureSensor && altitudeValueFromLocation != 0d) {
                double avgAltitude = (altitudeValueFromPressureSensor + altitudeValueFromLocation) / 2;
                altitudeTextView.setText(String.format("%.1f m n.p.m", avgAltitude));
            } /*else if (altitudeValueFromLocation != 0d) {
                altitudeTextView.setText(String.format("%.1f m n.p.m", altitudeValueFromLocation));
            } */else if (isAvailableAltitudeFromPressureSensor) {
                altitudeTextView.setText(String.format("%.1f m n.p.m", altitudeValueFromPressureSensor));
            } else {
                altitudeTextView.setText("N/A");
            }
        }
    }

    /**
     * Metoda wywoływana po zmianie lokalizacji.
     * @param location zaaktualizowana lokalizacja
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        altitudeValueFromLocation = location.getAltitude();
        coordinatesTextView.setText(String.format("%.4f, %.4f", latitude, longitude));
        if(!isAvailableAltitudeFromPressureSensor) {
            altitudeTextView.setText(String.format("%.1f m n.p.m", altitudeValueFromLocation));
        }
    }

    /**
     * Stworzenie alertu z komunikatem dot. wyłączonego GPS.
     */
    private void createAlertGPSNotAvailable(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(app);
        builder.setMessage("GPS jest wyłączony. Czy chcesz włączyć GPS?")
                .setCancelable(false)
                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        app.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    /**
     * Metoda wywoływana po włączeniu dostawcy lokalizacji w urządzeniu.
     * @param provider dostawca lokalizacji
     */
    @Override
    public void onProviderEnabled(@NonNull String provider) {
        stop();
        start();
    }

    /**
     * Metoda wywoływana po wyłączeniu dostawcy lokalizacji w urządzeniu.
     * @param provider dostawca lokalizacji
     */
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        createAlertGPSNotAvailable();
    }
}
