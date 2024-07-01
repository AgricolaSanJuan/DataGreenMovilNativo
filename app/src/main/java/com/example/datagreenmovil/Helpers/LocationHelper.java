package com.example.datagreenmovil.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationHelper {

    private LocationManager locationManager;
    private LocationResult locationResult;

    @SuppressLint("MissingPermission")
    public LocationHelper(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (locationResult != null) {
                    locationResult.gotLocation(location);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        });
    }

    public void getLocation(LocationResult result) {
        locationResult = result;
    }

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }
}
