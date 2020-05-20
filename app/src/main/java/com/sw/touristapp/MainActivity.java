package com.sw.touristapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Compass compass;
    private ImageView compassImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compass = new Compass(this);
        compassImage = findViewById(R.id.compassImage);
    }

    @Override
    protected void onStart() {
        super.onStart();
        compass.run();

    }

    @Override
    protected void onStop() {
        super.onStop();
        compass.stop();
    }

    public ImageView getCompassImage() {
        return compassImage;
    }
}