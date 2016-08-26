package com.justsaver.yjh.loverun.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;

import com.justsaver.yjh.loverun.Constant.PreferenceString;

import java.io.IOException;

import timber.log.Timber;

public class AudioService extends Service {

    AudioManager audioManager;
    MediaPlayer mediaPlayer;
    SharedPreferences sharedPreferences;

    public AudioService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("AudioService", "onStartCommand");
        if (intent != null) {
            final OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                }
            };
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            int result = audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_ALARM,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);


            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                SharedPreferences sharedPreferences = getSharedPreferences(PreferenceString.userInfo, MODE_PRIVATE);
                Uri ringTone = null;
                int item = intent.getIntExtra("item", 0);
                if ((item + 2) % 2 == 0) {
                    //run time
                    if (sharedPreferences.contains(PreferenceString.runTimeRingtone)) {
                        ringTone = Uri.parse(sharedPreferences.getString(PreferenceString.runTimeRingtone, ""));
                    } else {
                        ringTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    }
                } else {
                    //rest time
                    if (sharedPreferences.contains(PreferenceString.restTimeRingtone)) {
                        ringTone = Uri.parse(sharedPreferences.getString(PreferenceString.restTimeRingtone, ""));
                    } else {
                        ringTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    }
                }
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                try {
                    mediaPlayer.setDataSource(this, ringTone);
                    mediaPlayer.prepare();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            Log.i("AudioService", "start to play music");
                            mediaPlayer.start(); // then start
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        audioManager.abandonAudioFocus(afChangeListener);
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                });

            }
        }
        //vibrator 5 second
        sharedPreferences = getSharedPreferences(PreferenceString.configInfo, MODE_PRIVATE);
        Boolean vibrate = sharedPreferences.getBoolean(PreferenceString.VIBRATOR,false);
        int permission = getPackageManager().checkPermission(VIBRATOR_SERVICE,getPackageName());
        if(vibrate && ( PackageManager.PERMISSION_GRANTED == permission) ){
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(5000);
        }else{
            Timber.i("not permission");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.i("onDestroy");
    }
}
