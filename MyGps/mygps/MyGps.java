package com.example.sunwoo.mygps;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyGps extends AppCompatActivity {

    Button              btnStopService;
    TextView            txtMsg;

    Intent              intentMyService;
    ComponentName       service;
    BroadcastReceiver   receiver;

    String GPS_FILTER = "com.example.sunwoo.mygps";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        txtMsg = (TextView) findViewById(R.id.txtMsg);

        intentMyService = new Intent(this, MyGpsService.class);
        service = startService(intentMyService);

        txtMsg.setText("MyGpsService started - (see DDMS Log)");

        IntentFilter mainFilter = new IntentFilter(GPS_FILTER);
        receiver = new MyMainLocalReceiver();
        registerReceiver(receiver, mainFilter);

        btnStopService = (Button) findViewById(R.id.btnStopService);
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    stopService(new Intent(intentMyService));

                    txtMsg.setText("After stoping Service: \n" + service.getClassName());
                    btnStopService.setText("Finished");
                    btnStopService.setClickable(false);
                }
                catch (Exception e) {
                    Log.e("MyGps", e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            stopService(intentMyService);
            unregisterReceiver(receiver);
        }
        catch (Exception e) {
            Log.e("MAIN_DESTROY", e.getMessage());
        }
        Log.e("MAIN_DESTROY", "Adios");
    }

    public class MyMainLocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            double latitude = intent.getDoubleExtra("latitude", -1);
            double longitude = intent.getDoubleExtra("longitude", -1);

            String msg = "lat: " + Double.toString(latitude) + " " +
                    " lon: " + Double.toString(longitude);

            txtMsg.setText(msg);

            texting(msg);
        }
    }

    private void texting(String msg) {
        try {
            SmsManager smsMgr = SmsManager.getDefault();

            smsMgr.sendTextMessage("5556", "5554", "Please meet me at: " + msg, null, null);
        }
        catch (Exception e) {
            Toast.makeText(this, "texting\n" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
