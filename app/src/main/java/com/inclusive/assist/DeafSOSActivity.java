package com.inclusive.assist;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DeafSOSActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private String cameraId;
    private Vibrator vibrator;
    private boolean isSOSActive = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Simple UI created programmatically to save time
        TextView tv = new TextView(this);
        tv.setText("SOS ACTIVE!\nTAP TO STOP");
        tv.setTextSize(40);
        tv.setGravity(android.view.Gravity.CENTER);
        tv.setBackgroundColor(android.graphics.Color.RED);
        tv.setTextColor(android.graphics.Color.WHITE);
        setContentView(tv);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        try {
            if (cameraManager != null) {
                cameraId = cameraManager.getCameraIdList()[0];
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        startSOS();

        // Tap anywhere to stop
        tv.setOnClickListener(v -> {
            stopSOS();
            finish(); // Close activity
        });
    }

    private void startSOS() {
        isSOSActive = true;
        handler.post(sosRunnable);
    }

    private void stopSOS() {
        isSOSActive = false;
        handler.removeCallbacks(sosRunnable);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
            }
        } catch (Exception e) {}
        vibrator.cancel();
    }

    // The loop that flashes light and vibrates
    private Runnable sosRunnable = new Runnable() {
        boolean lightOn = false;
        @Override
        public void run() {
            if (!isSOSActive) return;

            try {
                // Toggle Flashlight
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && cameraId != null) {
                    cameraManager.setTorchMode(cameraId, !lightOn);
                    lightOn = !lightOn;
                }

                // Vibrate
                if (lightOn) {
                    vibrator.vibrate(200);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Repeat every 200ms (Fast strobe)
            handler.postDelayed(this, 200);
        }
    };

    @Override
    protected void onDestroy() {
        stopSOS();
        super.onDestroy();
    }
}