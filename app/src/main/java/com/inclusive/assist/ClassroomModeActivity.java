package com.inclusive.assist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Locale;

public class ClassroomModeActivity extends AppCompatActivity {

    private SpeechRecognizer speechRecognizer;
    private Intent speechIntent;
    private TextView tvCaptions, tvStatus;
    private ScrollView scrollView;
    private boolean isListening = true;
    private String fullTranscript = ""; // Stores the whole lecture

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom_mode);

        tvCaptions = findViewById(R.id.tvCaptions);
        tvStatus = findViewById(R.id.tvStatus);
        scrollView = findViewById(R.id.scrollView);
        Button btnClear = findViewById(R.id.btnClear);

        // Check Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        initializeSpeechRecognizer();
        startListening();

        btnClear.setOnClickListener(v -> {
            fullTranscript = "";
            tvCaptions.setText("");
        });
    }

    private void initializeSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true); // Show words instantly

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {}

            @Override
            public void onBeginningOfSpeech() {
                tvStatus.setText("🎤 Speaker is talking...");
            }

            @Override
            public void onRmsChanged(float rmsdB) {}
            @Override
            public void onBufferReceived(byte[] buffer) {}
            @Override
            public void onEndOfSpeech() {
                // IMPORTANT: When silence happens, don't stop. Restart!
                tvStatus.setText("⏳ Waiting for speech...");
                startListening();
            }

            @Override
            public void onError(int error) {
                // If error occurs (like no internet or silence), restart automatically
                startListening();
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    String newSentence = matches.get(0);
                    // Append new sentence to the full lecture text
                    fullTranscript += newSentence + ". \n\n";
                    tvCaptions.setText(fullTranscript);

                    // Auto-scroll to bottom
                    scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
                }
                startListening(); // Ensure loop continues
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // Optional: You can show "typing..." effect here
            }

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });
    }

    private void startListening() {
        if (speechRecognizer != null) {
            // Must run on UI thread to avoid crashes
            runOnUiThread(() -> {
                try {
                    speechRecognizer.startListening(speechIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
    }
}