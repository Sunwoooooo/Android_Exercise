package com.example.sunwoo.myservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

public class MyService extends Service {
    private final String SERVICE_NAME = "Test My Service";
    private final IMyService.Stub binder = new IMyService.Stub()
    {
        public String getServiceName() throws RemoteException
        {
            return SERVICE_NAME;
        }
    };

    @Override
    public void onCreate() {
        Toast toast = Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_LONG);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Toast toast = Toast.makeText(getBaseContext(), "onStart", Toast.LENGTH_LONG);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onDestroy() {
        Toast toast = Toast.makeText(getBaseContext(), "onDestroy", Toast.LENGTH_LONG);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast toast = Toast.makeText(getBaseContext(), "onBind", Toast.LENGTH_LONG);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
        return binder;
    }
}
