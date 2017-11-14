package com.example.sunwoo.mybroadcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyBroadcastActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button broadcast = (Button) findViewById(R.id.broadcast);

        broadcast.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                sendBroadcast(new Intent("com.example.sunwoo.mybroadcast.SAMPLE_BROADCAST", null));
            }
        });
    }
}
