package com.sw.touristapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa główna aplikacji.
 */
public class MainActivity extends AppCompatActivity {
    private Compass compass;
    private Flashlight flashlight;
    private ThemeManager themeManager;
    private Pressure pressure;
    Coordinate coordinate;
    private SignalSOS signalSOS;

    /**
     * Metoda wywoływana przy tworzeniu instancji aplikacji.
     *
     * @param savedInstanceState Jeżeli aktywność zostanie zainicjalizowana ponownie, jest to pakiet zawierający wcześniejszy zablokowany stan działania.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestRequiredPermissions();
        compass = new Compass(this);
        flashlight = new Flashlight(this);
        themeManager = new ThemeManager(this);
        coordinate = new Coordinate(this);
        pressure = new Pressure(this);
        signalSOS = new SignalSOS(this, flashlight);
    }

    /**
     * Metoda wywoływana, gdy aktywność staje się widoczna dla użytkownika.
     */
    @Override
    protected void onStart() {
        super.onStart();
        compass.start();
        themeManager.start();
        pressure.start();
        coordinate.start();
    }

    /**
     * Metoda następuje zawsze po onPause(), wywoływana jest, gdy aktywność znajduje się na szczycie stosu aktywności
     * i dane wejściowe użytkownika są do niej kierowane.
     */
    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
        compass.resume();
        themeManager.start();
        pressure.start();
        coordinate.start();
    }

    /**
     * Metoda wywoływana kiedy aktywność traci stan pierwszego planu. Aktywność jest nadal widoczna dla użytkownika, dlatego zaleca się, aby była
     * aktywna wizualnie i kontynuowała aktualizajcę interfejsu użytkownika. Po tej motodzie wywoływana jest metoda onResume(), jeśli aktywność
     * powraca na pierwszy plan, lub onStop(), jeśli staje się niewidoczna dla użytkownika.
     */
    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
        compass.pause();
        themeManager.stop();
        pressure.stop();
        coordinate.stop();
    }

    /**
     * Metoda wywoływana, gdy aktywność nie jest już widoczna dla użytkownika. Jest zwykle używana do zatrzymania działania aplikacji lub odświeżenia
     * interfejsu użytkownika.
     */
    @Override
    protected void onStop() {
        super.onStop();
        compass.stop();
        themeManager.stop();
        pressure.stop();
        coordinate.stop();
    }

    private void requestRequiredPermissions() {
        List<String> requiredPermissions = new ArrayList<>();
        final int REQUEST_PERM = 10;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requiredPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (requiredPermissions.size() > 0)
            ActivityCompat.requestPermissions(this, requiredPermissions.toArray(new String[0]), REQUEST_PERM);
    }

}