package com.example.sunwoo.makelist;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MyAudio extends AppCompatActivity implements
        SeekBar.OnSeekBarChangeListener {

    private final Timer timer = new Timer();
    private MediaPlayer mMediaPlayer;
    private String path;
    private SeekBar mSeekBar;
    private TextView mProgressText;
    private TextView mTrackingText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaudio);

        Intent intent = getIntent();
        path = intent.getStringExtra("audio_path");
        Log.i("Path", path);

        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(this);
        mProgressText = (TextView) findViewById(R.id.progress);
        mTrackingText = (TextView) findViewById(R.id.tracking);

        playAudio();

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                int currentPosition = mMediaPlayer.getCurrentPosition();
                int duration = mMediaPlayer.getDuration();
                int per = (int) Math.floor((currentPosition * 100) / duration);

                mSeekBar.setProgress(per);

                if(per == 100)
                    timer.cancel();
            }
        }, 1000, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            timer.cancel();
        }
    }

    private void playAudio() {
        try {
            mMediaPlayer = new MediaPlayer();

            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            mMediaPlayer.start();

            Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Log.e("MyAudio", "error : " + e.getMessage(), e);
        }
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        mProgressText.setText(progress + " = " + fromTouch);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        mTrackingText.setText("on");
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        mTrackingText.setText("off");
        Log.i("PROGRESS", Integer.toString(seekBar.getProgress()));
        mMediaPlayer.pause();

        int pos = mMediaPlayer.getDuration() * seekBar.getProgress() / 100;
        mMediaPlayer.seekTo(pos);
        mMediaPlayer.start();
    }
}
