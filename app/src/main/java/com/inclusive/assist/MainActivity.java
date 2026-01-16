package com.inclusive.assist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private Button btnBlind, btnDeaf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBlind = findViewById(R.id.btnBlind);
        btnDeaf = findViewById(R.id.btnDeaf);

        // 1. Initialize Voice (TTS)
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            }
        });

        // 2. Check All Permissions on Start (Best Practice)
        checkAllPermissions();

        // 3. BLIND MODE BUTTON
        btnBlind.setOnClickListener(v -> {
            speak("Blind Mode Activated");
            Intent intent = new Intent(MainActivity.this, BlindMenuActivity.class);
            startActivity(intent);
        });

        // 4. DEAF MODE BUTTON (Updated)
        btnDeaf.setOnClickListener(v -> {
            speak("Deaf Mode Activated");
            // Navigate to the new Deaf Menu
            Intent intent = new Intent(MainActivity.this, DeafMenuActivity.class);
            startActivity(intent);
        });
    }

    // Helper function to speak text
    private void speak(String text) {
        if (tts != null) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    // Helper to check permissions (Camera, Audio, Location, etc.)
    private void checkAllPermissions() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.VIBRATE
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 100);
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}