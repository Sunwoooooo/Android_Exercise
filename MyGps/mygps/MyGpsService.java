package com.example.sunwoo.mygps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class MyGpsService extends Service {

    String GPS_FILTER = "com.example.sunwoo.mygps";
    Thread triggerService;
    LocationManager lm;
    GPSListener myLocationListener;
    boolean isRunning = true;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("MyGpsService-onStart", "I am alive-GPS!");

        triggerService = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.prepare();

                    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    myLocationListener = new GPSListener();

                    long minTime = 10000;
                    float minDistance = 50;

                    /**
                     * @try~catch permission check for higher than API 23 device.
                     */
                    try {
                        lm.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                minTime,
                                minDistance,
                                myLocationListener);
                    }
                    catch (SecurityException e) {
                        e.printStackTrace();
                    }


                    Looper.loop();
                }
                catch (Exception e) {
                    Log.e("MyGps", e.getMessage());
                }
            }
        });

        triggerService.start();
    }

    private class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Intent myFilteredResponse = new Intent(GPS_FILTER);
            myFilteredResponse.putExtra("latitude", latitude);
            myFilteredResponse.putExtra("longitude", longitude);

            sendBroadcast(myFilteredResponse);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
