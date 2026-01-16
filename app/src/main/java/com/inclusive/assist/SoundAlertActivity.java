package com.inclusive.assist;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SoundAlertActivity extends AppCompatActivity {

    private MediaRecorder mRecorder;
    private TextView tvDecibel, tvStatus;
    private ImageView ivSoundIcon;
    private LinearLayout layoutBackground;
    private Button btnToggle;
    private boolean isListening = true;
    private Handler handler = new Handler();
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_alert);

        tvDecibel = findViewById(R.id.tvDecibel);
        tvStatus = findViewById(R.id.tvStatus);
        ivSoundIcon = findViewById(R.id.ivSoundIcon);
        layoutBackground = findViewById(R.id.layoutBackground);
        btnToggle = findViewById(R.id.btnToggle);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Check permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            startRecorder();
        }

        btnToggle.setOnClickListener(v -> {
            if (isListening) {
                stopRecorder();
                btnToggle.setText("Start Listening");
                tvStatus.setText("Paused");
                layoutBackground.setBackgroundColor(Color.BLACK);
            } else {
                startRecorder();
                btnToggle.setText("Stop Listening");
            }
            isListening = !isListening;
        });
    }

    private void startRecorder() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null"); // We don't save the file, just listen

            try {
                mRecorder.prepare();
                mRecorder.start();
                updateDecibelLevel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRecorder() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        handler.removeCallbacksAndMessages(null);
    }

    // Loop to check sound level every 0.5 seconds
    private void updateDecibelLevel() {
        if (mRecorder != null && isListening) {
            double amplitude = mRecorder.getMaxAmplitude();
            if (amplitude > 0) {
                // Convert amplitude to decibels
                double db = 20 * Math.log10(amplitude);
                int finalDb = (int) db;

                tvDecibel.setText(finalDb + " dB");

                // THRESHOLD: If louder than 75dB (Loud talking/Shouting)
                if (finalDb > 75) {
                    triggerAlert();
                } else {
                    resetAlert();
                }
            }
            handler.postDelayed(this::updateDecibelLevel, 200); // Check 5 times a second
        }
    }

    private void triggerAlert() {
        tvStatus.setText("⚠️ LOUD NOISE DETECTED!");
        ivSoundIcon.setColorFilter(Color.RED);
        layoutBackground.setBackgroundColor(Color.parseColor("#330000")); // Dark Red BG

        // Vibrate to warn user
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(100);
        }
    }

    private void resetAlert() {
        tvStatus.setText("Listening...");
        ivSoundIcon.setColorFilter(Color.parseColor("#4CAF50")); // Green
        layoutBackground.setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRecorder();
    }
}