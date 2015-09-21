package com.prateek.reminderapp.activity;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.prateek.reminderapp.R;

import java.io.IOException;

/**
 * Created by Prateek on 17-09-2015.
 */
public class AlarmActivity extends Activity/* implements MediaPlayer.OnPreparedListener*/{

//    Button bt_dismiss;

    MediaPlayer mMediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** Turn Screen On and Unlock the keypad when this alert dialog is displayed */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_alarm);

//        bt_dismiss = (Button)findViewById(R.id.bt_dismiss_id);

        ringAlarm();
    }

    public void ringAlarm() {
        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMediaPlayer = MediaPlayer.create(this, alert);

        mMediaPlayer.start();

        /*if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setLooping(true);
//            mMediaPlayer.start();
        }*/

    }

    public void stopAlarm(View view) {

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            finish();
        }

    }

    /*@Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }*/
}
