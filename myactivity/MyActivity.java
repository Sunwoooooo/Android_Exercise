package com.example.sunwoo.myactivity;

import android.content.ComponentName;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MyActivity extends Activity {

    private static final int REQUEST_CODE_PARAM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Button btn_next = (Button) findViewById(R.id.next);

        btn_next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();

                ComponentName comp = new ComponentName("com.example.sunwoo.myactivity",
                        "com.example.sunwoo.myactivity.MyNextActivity");

                i.setComponent(comp);
                startActivityForResult(i, REQUEST_CODE_PARAM);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_PARAM)
        {
            Toast toast = Toast.makeText(getBaseContext(), "onActivityResult", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast toast = Toast.makeText(getBaseContext(), "onPause", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast toast = Toast.makeText(getBaseContext(), "onResume", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast toast = Toast.makeText(getBaseContext(), "onRestart", Toast.LENGTH_LONG);
        toast.show();
    }
}
