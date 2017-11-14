package com.example.sunwoo.myintentsr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyIntentReceiver extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_intent_receiver);

        final Button btn = (Button) findViewById(R.id.Button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyIntentReceiver.this, MyIntentSender.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TextView view_name = (TextView) findViewById(R.id.name);
        TextView view_number = (TextView) findViewById(R.id.number);

        if(resultCode == RESULT_OK)
        {
            if(requestCode == 1)
            {
                view_name.setText(data.getStringExtra("data_name"));
                view_number.setText(data.getStringExtra("data_number"));
            }
        }
    }
}
