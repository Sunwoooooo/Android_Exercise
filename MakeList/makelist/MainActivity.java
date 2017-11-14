package com.example.sunwoo.makelist;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PARAM = 1;

    private Button call_ImageList;
    private Button call_AudioList;
    private Button call_VideoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        call_AudioList = (Button) findViewById(R.id.button_audiolist);
        call_ImageList = (Button) findViewById(R.id.button_imagelist);
        call_VideoList = (Button) findViewById(R.id.button_videolist);

        call_AudioList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                ComponentName comp = new ComponentName("com.example.sunwoo.makelist",
                        "com.example.sunwoo.makelist.AudioListActivity");

                intent.setComponent(comp);
                startActivityForResult(intent, REQUEST_CODE_PARAM);
            }
        });

        call_ImageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                ComponentName comp = new ComponentName("com.example.sunwoo.makelist",
                        "com.example.sunwoo.makelist.ImageListActivity");

                intent.setComponent(comp);
                startActivityForResult(intent, REQUEST_CODE_PARAM);
            }
        });

        call_VideoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                ComponentName comp = new ComponentName("com.example.sunwoo.makelist",
                        "com.example.sunwoo.makelist.VideoListActivity");

                intent.setComponent(comp);
                startActivityForResult(intent, REQUEST_CODE_PARAM);
            }
        });
    }
}
