package com.example.sunwoo.makelist;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PARAM = 1;

    private static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 1;

    private Button call_ImageList;
    private Button call_AudioList;
    private Button call_VideoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        int permissionExternalStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if(permissionExternalStorage == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_EXTERNAL_STORAGE);
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_EXTERNAL_STORAGE:
                for (int i = 0; i < permissions.length; i++) {

                    String permission = permissions[i];
                    int grantResult = grantResults[i];

                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "storage permission authorized", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "storage permission denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                break;
        }
    }
}
