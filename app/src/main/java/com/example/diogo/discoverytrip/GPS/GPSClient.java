package com.example.diogo.discoverytrip.GPS;

import android.location.Location;

/**
 * Created by diogo on 12/06/17.
 */

public interface GPSClient {

    public void locationChange(Location location);

    public void onProviderEnabled(String provider);

    public void onProviderDisabled(String provider);
}
