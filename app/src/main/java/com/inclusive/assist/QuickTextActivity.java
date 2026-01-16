package com.inclusive.assist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class QuickTextActivity extends AppCompatActivity {

    private TextView tvFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_text);

        tvFullScreen = findViewById(R.id.tvFullScreen);

        // Setup all buttons using a helper function
        setupButton(R.id.btnMsg1, "I am Deaf.\nPlease communicate visually.");
        setupButton(R.id.btnMsg2, "Please write it down.");
        setupButton(R.id.btnMsg3, "Where is the Hospital?");
        setupButton(R.id.btnMsg4, "I need Police help.");
        setupButton(R.id.btnMsg5, "Where is the Restroom?");
        setupButton(R.id.btnMsg6, "Thank You!");

        // Tap the full screen text to close it
        tvFullScreen.setOnClickListener(v -> {
            tvFullScreen.setVisibility(View.GONE);
        });
    }

    private void setupButton(int btnId, String message) {
        Button btn = findViewById(btnId);
        btn.setOnClickListener(v -> {
            tvFullScreen.setText(message);
            tvFullScreen.setVisibility(View.VISIBLE);
        });
    }
}