package com.example.datagreenmovil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetLastLocation {

    private Context context;
    private GeoDataCaptured geoDataCaptured;
    private GeoDataNotCaptured geoDataNotCaptured;

    public GetLastLocation(Context context, GeoDataCaptured geoDataCaptured, GeoDataNotCaptured geoDataNotCaptured) {
        this.context = context;
        this.geoDataCaptured = geoDataCaptured;
        this.geoDataNotCaptured = geoDataNotCaptured;
    }

    public void getGeoDataDetails() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permiso no concedido, solicitar permisos
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return; // No continuar si no se tienen permisos
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation().addOnSuccessListener((Activity) context, location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // Obtener detalles de ubicación
                Address address = getLocationDetails(latitude, longitude);
                if (address != null) {
                    geoDataCaptured.onGeoDataCaptured(address);
                } else {
                    Log.e("GetLastLocation", "No se pudieron obtener detalles de la ubicación.");
                    geoDataNotCaptured.onGeoDataNotCaptured("Empty Address", "No se pudieron obtener detalles de la ubicación.");
//                    throw new RuntimeException("No se pudieron obtener detalles de la ubicación.");
                }
            } else {
                Log.e("GetLastLocation", "Ubicación no disponible.");
//                throw new RuntimeException("Ubicación no disponible.");
                    geoDataNotCaptured.onGeoDataNotCaptured("Empty Address", "Ubicación no disponible.");
            }
        }).addOnFailureListener(e -> {
            Log.e("GetLastLocation", "Error al obtener la ubicación: ");
            geoDataNotCaptured.onGeoDataNotCaptured("Empty Address", e.getMessage());
        });
    }

    private Address getLocationDetails(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
