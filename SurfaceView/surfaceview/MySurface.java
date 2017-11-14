package com.example.sunwoo.surfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MySurface extends AppCompatActivity implements SurfaceHolder.Callback,
        View.OnTouchListener, View.OnClickListener{

    private SurfaceView sv;
    private LinearLayout ll;
    private SecondThread mSecondThread;
    private TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ll = (LinearLayout) findViewById(R.id.baselayout);
        ll.setOnTouchListener(this);
        sv = (SurfaceView) findViewById(R.id.surfaceview);
        sv.getHolder().addCallback(this);
        tv = (TextView) findViewById(R.id.textview);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("SurfaceLog", "surfaceCreated : ");
        mSecondThread = new SecondThread(holder, getBaseContext(),
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        tv.setVisibility(msg.getData().getInt("viz"));
                        tv.setText(msg.getData().getString("text"));
                    }
                });
        mSecondThread.setRunning(true);
        mSecondThread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("SurfaceLog", "surfaceChanged : " + Integer.toString(width) + " : "
                + Integer.toString(height));

        mSecondThread.setSurfaceSize(width, height);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("SurfaceLog", "surfaceDestroyed : ");

        boolean retry = true;
        mSecondThread.setRunning(false);

        while(retry) {
            try {
                mSecondThread.join();
                retry = false;
            }
            catch (InterruptedException e) {
            }
        }
    }

    public boolean onTouch(View view, MotionEvent event) {
        float cur_x = event.getRawX();
        float cur_y = event.getRawY();

        ll.setPadding((int)cur_x - 100, (int)cur_y - 120, 0, 0);

        return true;
    }

    public void onClick(View view) {

    }
}

class SecondThread extends Thread {

    private boolean mRun = false;
    private SurfaceHolder mSurfaceHolder;
    //private Bitmap mBackgroundImage;
    private Bitmap bitmap;
    private Handler mHandler;
    private int drawCount = 0;

    SecondThread(SurfaceHolder holder, Context context, Handler handler) {
        mSurfaceHolder = holder;
        mHandler = handler;
        /*Resources res = context.getResources();
        mBackgroundImage = BitmapFactory.decodeResource(res, R.drawable.android);*/
        imagetoBitmap();
    }

    @Override
    public void run() {
        while(mRun) {
            Canvas c = null;

            try {
                Log.i("SurfaceLog", "lock");
                c = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    doDraw(c);
                }
            }
            finally {
                if(c != null) {
                    Log.i("SurfaceLog", "unlock");
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    private void doDraw(Canvas canvas) {
        //canvas.drawBitmap(mBackgroundImage, 0, 0, null);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Message msg = mHandler.obtainMessage();
        Bundle b = new Bundle();

        b.putString("text", Integer.toString(drawCount));
        b.putInt("viz", View.VISIBLE);
        msg.setData(b);
        mHandler.sendMessage(msg);
        drawCount++;
    }

    public void setRunning(boolean b) {
        mRun = b;
    }

    public void setSurfaceSize(int width, int height) {
        synchronized (mSurfaceHolder) {
            /*mBackgroundImage = Bitmap.createScaledBitmap(mBackgroundImage,
                    width, height, true);*/
            bitmap = Bitmap.createScaledBitmap(bitmap,
                    width, height, true);
        }
    }

    public void imagetoBitmap() {
        String imagepath = "/storage/emulated/0/DCIM/Camera/20120120_060136.jpg";
        int width = 100;
        int height = 100;
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeFile(imagepath, options);
        bitmap = BitmapFactory.decodeFile(imagepath, options);
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}
