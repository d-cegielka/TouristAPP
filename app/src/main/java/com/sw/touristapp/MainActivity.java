package com.sw.touristapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Compass compass;
    private Flashlight flashlight;
    private ThemeManager themeManager;
    private Pressure pressure;
    Coordinate coordinate;
    private SignalSOS signalSOS;

    /**
     * Metoda wywoływana przy tworzeniu instancji aplikacji.
     * @param savedInstanceState Jeżeli aktywność zostanie zainicjalizowana ponownie, jest to pakiet zawierający wcześniejszy zablokowany stan działania.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compass = new Compass(this);
        flashlight = new Flashlight(this);
        themeManager = new ThemeManager(this);
        coordinate = new Coordinate(this);
        pressure = new Pressure(this, coordinate);
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

}