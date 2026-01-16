package com.inclusive.assist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Locale;

public class SpeechToTextActivity extends AppCompatActivity {

    // 1. Components for Listening
    private SpeechRecognizer speechRecognizer;
    private TextView tvOutput;
    private FloatingActionButton btnMic;
    private Intent speechIntent;
    private boolean isListening = false;

    // 2. Components for Speaking (Typing)
    private EditText etTypeBox;
    private Button btnSpeak;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        // Link Views
        tvOutput = findViewById(R.id.tvSpeechOutput);
        btnMic = findViewById(R.id.btnMic);
        etTypeBox = findViewById(R.id.etTypeBox);
        btnSpeak = findViewById(R.id.btnSpeak);

        // --- PART A: SETUP TEXT-TO-SPEECH (For Typing) ---
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            }
        });

        btnSpeak.setOnClickListener(v -> {
            String text = etTypeBox.getText().toString();
            if (!text.isEmpty()) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                Toast.makeText(this, "Speaking...", Toast.LENGTH_SHORT).show();
            }
        });

        // --- PART B: SETUP SPEECH-TO-TEXT (For Listening) ---

        // Check Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                tvOutput.setText("Listening... (Speak clearly)");
                tvOutput.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            }

            @Override
            public void onBeginningOfSpeech() {}
            @Override
            public void onRmsChanged(float rmsdB) {}
            @Override
            public void onBufferReceived(byte[] buffer) {}
            @Override
            public void onEndOfSpeech() {
                isListening = false;
                btnMic.setImageResource(android.R.drawable.ic_btn_speak_now); // Reset Icon
            }

            @Override
            public void onError(int error) {
                tvOutput.setText("Tap mic to try again.");
                isListening = false;
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    tvOutput.setText(matches.get(0));
                    tvOutput.setTextColor(getResources().getColor(android.R.color.black));
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
                // Show text as it is being spoken (Real-time effect)
                ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    tvOutput.setText(matches.get(0));
                }
            }

            @Override
            public void onEvent(int eventType, Bundle params) {}
        });

        btnMic.setOnClickListener(v -> {
            if (!isListening) {
                speechRecognizer.startListening(speechIntent);
                isListening = true;
                btnMic.setImageResource(android.R.drawable.ic_media_pause); // Change Icon to Stop
            } else {
                speechRecognizer.stopListening();
                isListening = false;
                btnMic.setImageResource(android.R.drawable.ic_btn_speak_now);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) speechRecognizer.destroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}