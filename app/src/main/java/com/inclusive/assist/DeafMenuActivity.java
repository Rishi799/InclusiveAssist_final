package com.inclusive.assist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DeafMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deaf_menu);

        // 1. Speech to Text
        Button btnSpeech = findViewById(R.id.btnSpeechToText);
        btnSpeech.setOnClickListener(v -> {
            startActivity(new Intent(this, SpeechToTextActivity.class));
        });

        // 2. Quick Messages (We will build this next)
        Button btnQuick = findViewById(R.id.btnQuickText);
        btnQuick.setOnClickListener(v -> {
            // startActivity(new Intent(this, QuickTextActivity.class));
        });

        // 3. Sound Alerts (Future)
        Button btnSound = findViewById(R.id.btnSoundAlerts);
        btnSound.setOnClickListener(v -> {
            // startActivity(new Intent(this, SoundAlertActivity.class));
        });

        // 4. SOS (Future)
        Button btnSos = findViewById(R.id.btnDeafSOS);
        btnSos.setOnClickListener(v -> {
            // startActivity(new Intent(this, DeafSOSActivity.class));
        });
    }
}