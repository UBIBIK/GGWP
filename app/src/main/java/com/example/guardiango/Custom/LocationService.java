package com.example.guardiango.Custom;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.guardiango.entity.LocationData;
import com.example.guardiango.entity.SharedPreferencesGroup;
import com.example.guardiango.entity.SharedPreferencesHelper;
import com.example.guardiango.entity.UserInfo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationResult;

public class LocationService {
    private FusedLocationProviderClient fusedLocationClient;
    private Activity activity;

    private LocationData locationData;


    public LocationService(Activity activity) {
        this.activity = activity;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        checkPermissions();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);  // 10 seconds
        locationRequest.setFastestInterval(5000);  // 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateLocationData(location.getLatitude(), location.getLongitude());
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void updateLocationData(double latitude, double Longitude) {
        locationData = new LocationData(latitude, Longitude);
    }

    public LocationData getCurrentLocationData() {
        return locationData;
    }
}
