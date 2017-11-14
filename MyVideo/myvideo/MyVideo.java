package com.example.sunwoo.myvideo;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

public class MyVideo extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener, View.OnTouchListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener,
        SurfaceHolder.Callback{

    private static final String TAG = "MyVideo";
    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    private LinearLayout videolayout;

    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private String path = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        videolayout = (LinearLayout) findViewById(R.id.videolayout);
        videolayout.setOnTouchListener(this);

        mPreview = (SurfaceView) findViewById(R.id.videoview);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated called");
        playVideo();
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void playVideo() {
        doCleanUp();

        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
        }
        catch (Exception e) {
            Log.e(TAG, "error : " + e.getMessage(), e);
        }
    }

    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared called");

        mVideoWidth = mMediaPlayer.getVideoWidth();
        mVideoHeight = mMediaPlayer.getVideoHeight();

        if(mVideoWidth != 0 && mVideoHeight != 0) {
            holder.setFixedSize(mVideoWidth, mVideoHeight);
            mMediaPlayer.start();
        }

        mIsVideoReadyToBePlayed = true;

        if(mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            Log.d(TAG, "startVideo in onPrepared");
            startVideoPlayback();
        }
    }

    private void startVideoPlayback() {
        Log.d(TAG, "startVideoPlayback");
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int i, int j, int k) {
        Log.d(TAG, "surfaceChanged called");
    }

    public void onCompletion(MediaPlayer arg0) {
        Log.d(TAG, "onCompletion called");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed called");
    }

    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
        Log.v(TAG, "onVideoSizeChanged");

        if(width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");

            return;
        }

        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;

        if(mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            Log.d(TAG, "startVideo in onVideoSizeChanged");
            startVideoPlayback();
        }
    }

    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        Log.d(TAG, "onBufferingUpdate percent : " + percent);
    }

    public boolean onTouch(View v, MotionEvent e) {
        float cur_x = e.getRawX();
        float cur_y = e.getRawY();

        videolayout.setPadding((int)cur_x - 100, (int)cur_y - 120, 0, 0);

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
