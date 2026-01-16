package com.inclusive.assist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.util.List;
import java.util.Locale;

public class VisualLocationActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private TextView tvLocationText;
    private Button btnRefresh, btnShare;
    private String currentAddress = "Unknown Location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_location);

        tvLocationText = findViewById(R.id.tvLocationText);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnShare = findViewById(R.id.btnShare);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Get location immediately on start
        getLocation();

        btnRefresh.setOnClickListener(v -> {
            tvLocationText.setText("Updating...");
            getLocation();
        });

        btnShare.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Help! I am deaf and currently at: " + currentAddress);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share Location"));
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        currentAddress = address.getAddressLine(0);
                        tvLocationText.setText(currentAddress);
                    } else {
                        tvLocationText.setText("Location found, but address unknown.");
                    }
                } catch (Exception e) {
                    tvLocationText.setText("Error fetching address.");
                }
            } else {
                tvLocationText.setText("GPS Signal Weak. Please move outside.");
            }
        });
    }
}