package com.example.vincentale.leafguard_core.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Inspired by https://www.codeofaninja.com/2014/04/get-the-users-current-location-in-android.html
 * Helper class used to retrive User location;
 */

public class LocationHelper {

    final String TAG = "LocationHelper";
    public final static int DURATION = 60000;

    private static LocationHelper instance;

    private Timer timer;
    private LocationManager locationManager;
    private LocationResult locationResult;
    private boolean gpsEnabled = false;
    private boolean networkEnabled = false;

    public static LocationHelper getInstance() {
        if (instance == null) {
            instance = new LocationHelper();
        }

        return instance;
    }

    public void cancel() {
        timer.cancel();
        locationManager.removeUpdates(locationListenerNetwork);
        locationManager.removeUpdates(locationListenerGps);
    }

    public boolean getLocation(Context context, LocationResult result) {

        // I use LocationResult callback class to pass location value from
        // LocationHelper to user code.
        locationResult = result;

        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        // exceptions will be thrown if provider is not permitted.
        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // don't start listeners if no provider is enabled
        if (!gpsEnabled && !networkEnabled) {
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            // if gps is enabled, get location updates
            if (gpsEnabled) {
                Log.e(TAG, "gpsEnabled, requesting updates.");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
            }

            // if network is enabled, get location updates
            if (networkEnabled) {
                Log.e(TAG, "networkEnabled, requesting updates.");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
            }
            // the timer
            timer = new Timer();
            timer.schedule(new GetLastLocationTask(), DURATION);

            return true;
        } else {
            return false;
        }
    }

    LocationListener locationListenerGps = new LocationListener() {

        public void onLocationChanged(Location location) {

            Log.d(TAG, "onLocationChanged called !");
            // gave a location, cancel the timer
            timer.cancel();

            // put the location value
            locationResult.acceptLocation(location);

            // if you want to stop listening to gps location updates, un-comment the code below

            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerNetwork);

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    LocationListener locationListenerNetwork = new LocationListener() {

        public void onLocationChanged(Location location) {

            timer.cancel();

            // put the location value
            locationResult.acceptLocation(location);

            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerGps);

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    class GetLastLocationTask extends TimerTask {

        @Override
        public void run() {

            // In my case, I do not return the last known location, so I DO NOT remove the updates, just return a location value of null
            // or else, if you need the opposite un-comment the comment below
            Location netLocation = null, gpsLocation = null;
            if (gpsEnabled) {
                try {
                    gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } catch (SecurityException ex) {
                    ex.printStackTrace();
                }
            }

            if (networkEnabled){
                try {
                    netLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } catch (SecurityException ex) {
                    ex.printStackTrace();
                }
            }

            locationManager.removeUpdates(locationListenerGps);
            locationManager.removeUpdates(locationListenerNetwork);

            // if there are both values use the latest one
            if (gpsLocation != null && netLocation != null) {

                if (gpsLocation.getTime() > netLocation.getTime()){
                    locationResult.acceptLocation(gpsLocation);
                }else{
                    locationResult.acceptLocation(netLocation);
                }
                return;
            }

            if (gpsLocation != null) {
                locationResult.acceptLocation(gpsLocation);
                return;
            }
            if (netLocation != null) {
                locationResult.acceptLocation(netLocation);
                return;
            }

            locationResult.acceptLocation(null);
        }
    }


    public interface LocationResult {
        void acceptLocation(Location location);
    }
}
