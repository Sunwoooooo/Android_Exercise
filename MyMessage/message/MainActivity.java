package com.example.sunwoo.message;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;

class SubThread extends Thread {
    Handler mMainHandler;

    SubThread(Handler handler) {
        mMainHandler = handler;
    }

    public void run() {
        Looper.prepare();
        Looper.loop();
    }

    public Handler mBackHandler = new Handler() {
        public void handleMessage(Message msg) {
            Message retmsg = Message.obtain();

            switch (msg.what) {
                case 0: retmsg.what = 0;
                        retmsg.arg1 = msg.arg1 + 1;
            }

            try {
                sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            mMainHandler.sendMessage(retmsg);
        }
    };
}

public class MainActivity extends AppCompatActivity {

    TextView mainThreadValue;
    TextView subThreadValue;
    Button callButton;
    SubThread mThread;

    int mainValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainThreadValue = (TextView) findViewById(R.id.mainvalue);
        subThreadValue = (TextView) findViewById(R.id.subvalue);

        mThread = new SubThread(mHandler);
        mThread.setDaemon(true);
        mThread.start();

        callButton = (Button) findViewById(R.id.callbutton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                mainValue++;
                mainThreadValue.setText(Integer.toString(mainValue));
                Message sendmsg = Message.obtain();
                sendmsg.what = 0;
                sendmsg.arg1 = mainValue;
                mThread.mBackHandler.sendMessage(sendmsg);
            }
        });
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            Toast.makeText(getApplicationContext(), "Call SubThread", Toast.LENGTH_SHORT).show();

            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            switch (msg.what) {

                case 0: subThreadValue.setText(Integer.toString(msg.arg1));
                        mainValue = msg.arg1;
                        break;
            }
        }
    };
}

