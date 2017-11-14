package com.example.sunwoo.myservice;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyServiceController extends AppCompatActivity {

    private IMyService iMyService;
    private TextView textView;
    private Button startButton;
    private Button bindButton;
    private Button unbindButton;
    private Button stopButton;

    private ServiceConnection svcConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyService = IMyService.Stub.asInterface(service);
            String serviceName = null;

            try{
                serviceName = iMyService.getServiceName();
                textView.append(", Receive Result Value : " + serviceName);
            }
            catch(RemoteException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            iMyService = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service_controller);
        textView = (TextView) findViewById(R.id.textView);
        startButton = (Button) findViewById(R.id.startservice);
        bindButton = (Button) findViewById(R.id.bindservice);
        unbindButton = (Button) findViewById(R.id.unbindservice);
        stopButton = (Button) findViewById(R.id.stopservice);

        setEnableButtons(true, false, false, false);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStartService();
            }
        });

        bindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBindService();
            }
        });

        unbindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doUnbindService();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStopService();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doStopService();
    }

    private void doStartService()
    {
        Intent service = new Intent();
        ComponentName comp = new ComponentName("com.example.sunwoo.myservice",
                "com.example.sunwoo.myservice.MyService");
        service.setComponent(comp);

        startService(service);
        textView.setText("Start Service");
        setEnableButtons(false, true, false, true);
    }

    private void doBindService()
    {
        Intent service = new Intent();
        ComponentName comp = new ComponentName("com.example.sunwoo.myservice",
                "com.example.sunwoo.myservice.MyService");
        service.setComponent(comp);

        if(bindService(service, svcConn, BIND_AUTO_CREATE))
        {
            textView.setText("Bind Service");
            setEnableButtons(false, false, true, false);
        }
    }

    private void doUnbindService()
    {
        Intent service = new Intent();
        ComponentName comp = new ComponentName("com.example.sunwoo.myservice",
                "com.example.sunwoo.myservice.MyService");
        service.setComponent(comp);

        unbindService(svcConn);
        textView.setText("Unbind Service");
        setEnableButtons(false, false, false, true);
    }

    private void doStopService()
    {
        Intent service = new Intent();
        ComponentName comp = new ComponentName("com.example.sunwoo.myservice",
                "com.example.sunwoo.myservice.MyService");
        service.setComponent(comp);

        stopService(service);
        textView.setText("Stop Service");
        setEnableButtons(false, false, false, false);
    }

    private void setEnableButtons(boolean isEnableStart, boolean isEnableBind,
                                  boolean isEnableUnbind, boolean isEnableStop)
    {
        startButton.setEnabled(isEnableStart);
        startButton.setFocusable(isEnableStart);

        bindButton.setEnabled(isEnableBind);
        bindButton.setFocusable(isEnableBind);

        unbindButton.setEnabled(isEnableUnbind);
        unbindButton.setFocusable(isEnableUnbind);

        stopButton.setEnabled(isEnableStop);
        stopButton.setFocusable(isEnableStop);
    }
}
