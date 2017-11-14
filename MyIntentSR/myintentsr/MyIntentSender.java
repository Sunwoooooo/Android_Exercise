package com.example.sunwoo.myintentsr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MyIntentSender extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_intent_sender);

        Button setInfo = (Button) findViewById(R.id.Button);
        setInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                EditText input_name = (EditText) findViewById(R.id.inputname);
                EditText input_number = (EditText) findViewById(R.id.inputnumber);

                intent.putExtra("data_name", input_name.getText().toString());
                intent.putExtra("data_number", input_number.getText().toString());

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
