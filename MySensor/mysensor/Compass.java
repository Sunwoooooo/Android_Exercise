package com.example.sunwoo.mysensor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Compass extends Activity {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SampleView mView;
    private float[] mValues;

    TextView tvAzimuth;
    LinearLayout layout;

    WindowManager wm;
    Display display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        mView = new SampleView(this);
        layout = (LinearLayout) findViewById(R.id.layout);
        layout.addView(mView);
        tvAzimuth = (TextView) findViewById(R.id.textview_azimuth);
    }

    private final SensorEventListener mListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            mValues = event.values;

            switch (display.getRotation()) {
                case Surface.ROTATION_0:
                    break;
                case Surface.ROTATION_90:
                    mValues[0] = (mValues[0] + 90) % 360;
                    break;
                case Surface.ROTATION_180:
                    break;
                case Surface.ROTATION_270:
                    mValues[0] = (mValues[0] + 270) % 360;
                    break;
            }

            tvAzimuth.setText("Angle : " + mValues[0]);

            if(mView != null) {
                mView.invalidate();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(mListener);
        super.onStop();
    }

    private class SampleView extends View {

        private Paint mPaint = new Paint();
        private Path mPath = new Path();
        private boolean mAnimate;
        private long mNextTime;

        public SampleView(Context context) {
            super(context);

            mPath.moveTo(0, -50);
            mPath.lineTo(-20, 60);
            mPath.lineTo(0, 50);
            mPath.lineTo(20, 60);
            mPath.close();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Paint paint = mPaint;

            canvas.drawColor(Color.WHITE);

            paint.setAntiAlias(true);
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);

            int w = canvas.getWidth();
            int h = canvas.getHeight();
            int cx = w/2;
            int cy = h/2;

            canvas.translate(cx, cy);

            if(mValues != null) {
                canvas.rotate(-mValues[0]);
            }

            canvas.drawPath(mPath, paint);
        }

        @Override
        protected void onAttachedToWindow() {
            mAnimate = true;
            super.onAttachedToWindow();
        }

        @Override
        protected void onDetachedFromWindow() {
            mAnimate = false;
            super.onDetachedFromWindow();
        }
    }
}


