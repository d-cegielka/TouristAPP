package com.sw.touristapp;


import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class SignalSOS {
    private AppCompatActivity app;
    private BiometricPrompt biometricPrompt;
    private Executor executor;
    private Button sosButton;
    private Flashlight flashlight;
    private BiometricPrompt.PromptInfo promptInfo;

    //@RequiresApi(api = Build.VERSION_CODES.P)
    public SignalSOS(Context context, final Flashlight flashlight) {
        app = (AppCompatActivity) context;
        executor = ContextCompat.getMainExecutor(app);
        this.flashlight = flashlight;

        sosButton = app.findViewById(R.id.sosButton);

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flashlight.isFlashAvailable) {
                    Toast.makeText(app.getApplicationContext(),"Latarka jest niedostępna!", Toast.LENGTH_LONG).show();
                    return;
                }
                initializeSOSAuthenticate();
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

    private void initializeSOSAuthenticate(){
        biometricPrompt = new BiometricPrompt(app, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                sosButton.setEnabled(false);
                flashlight.flashlightButton.setEnabled(false);
                Toast.makeText(app.getApplicationContext(),"Uruchomiono sygnał SOS!", Toast.LENGTH_LONG).show();
                turnOnSOSFlashSignal(2);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autoryzacja")
                .setSubtitle("Aby uruchomić alarm SOS, autoryzuj uruchomienie!")
                .setDeviceCredentialAllowed(true)
                .build();
    }

    private void turnOnSOSFlashSignal(final int step) {
        String signal = "1010100100100100101010";
        int delay = 0;
        Handler handler = new Handler();
        for (int i = 0; i < step; i++) {
            for (int j = 0; j < signal.length(); j++) {
                if (signal.charAt(j) == '1') {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            flashlight.switchFlashLight(true);
                        }
                    }, (delay += 250));
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flashlight.switchFlashLight(false);
                    }
                }, (delay += 250));
            }
            final int finalI = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(finalI == step - 1) {
                        flashlight.flashlightButton.setEnabled(true);
                        sosButton.setEnabled(true);
                    }
                }
            }, delay += 1500);

        }
    }
}
