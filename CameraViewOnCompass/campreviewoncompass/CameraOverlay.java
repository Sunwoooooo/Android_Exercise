package com.example.sunwoo.campreviewoncompass;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class CameraOverlay extends AppCompatActivity
        implements SurfaceHolder.Callback{

    private static final String TAG = "CamOverlay";
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SampleView mView;
    private float[] mValues;

    Camera camera;
    SurfaceView sv;
    TextView tvAzimuth;
    LinearLayout compass;
    FrameLayout fl;
    Button takepicture;

    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    LayoutInflater controlInflater = null;

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

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFormat(PixelFormat.UNKNOWN);

        sv = (SurfaceView) findViewById(R.id.surfaceview_preview);
        surfaceHolder = sv.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mView = new SampleView(this);
        controlInflater = LayoutInflater.from(getApplicationContext());
        View viewControl = controlInflater.inflate(R.layout.control, null);
        ViewGroup.LayoutParams layoutParamsControl =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        this.addContentView(viewControl, layoutParamsControl);

        fl = (FrameLayout) findViewById(R.id.framelayout_preview);
        tvAzimuth = (TextView) findViewById(R.id.textview_azimuth);
        compass = (LinearLayout) findViewById(R.id.linearlayout_compass);
        fl.addView(mView);
        fl.bringChildToFront(compass);

        takepicture = (Button) findViewById(R.id.button_takepicture);
        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(shutterCallback, rawCallback, jpegCallback);
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if(previewing) {
            camera.stopPreview();
            previewing = false;
        }

        if(camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Uri uriTarget = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());

            OutputStream imageFile;

            try {
                imageFile = getContentResolver().openOutputStream(uriTarget);
                imageFile.write(data);
                imageFile.flush();
                imageFile.close();
                Log.i("Image Path", uriTarget.toString());

                Toast.makeText(getApplicationContext(), "image saved : " + uriTarget.toString(), Toast.LENGTH_LONG).show();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            camera.startPreview();
        }
    };

    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

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
