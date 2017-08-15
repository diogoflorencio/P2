package com.example.diogo.discoverytrip.GPS;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diogo on 12/06/17.
 */

public class GPS implements LocationListener {

    public static GPS instance;
    private List<GPSClient> clients;
    private static LocationManager locationManager;
    private static final int REQUEST_LOCATION = 2;

    public GPS(Activity activity){
        if(locationManager == null){
            clients = new ArrayList<>();
            startGPS(activity);
        }
    }

    public void addClient(GPSClient gpsClient){
        clients.add(gpsClient);

    }

    private void startGPS(Activity activity) {
        Log.d("Logger", "LocalizacaoFragment startGPS");
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        //permissão de GPS
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) ;
                // Esperando usuário autorizar permissão
            else
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
        } else
        if(verificaConexao(activity))
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
        else
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
    }

    public void stopGPS(Activity activity){
        //permissão de GPS
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) ;
                // Esperando usuário autorizar permissão
            else
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
        } else locationManager.removeUpdates(this);
    }

    public void removeClient(GPSClient gpsClient){
        clients.remove(gpsClient);
    }

    @Override
    public void onLocationChanged(Location location) {
        for (GPSClient client:clients ) {
            client.locationChange(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        for (GPSClient client:clients ) {
            client.onProviderEnabled(provider);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        for (GPSClient client:clients ) {
            client.onProviderDisabled(provider);
        }
    }

    private boolean verificaConexao(Activity activity) {
        //verificando se o dispositivo tem conexão com internet
        ConnectivityManager conectivtyManager =
                (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }
}
