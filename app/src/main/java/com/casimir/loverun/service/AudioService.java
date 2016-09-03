package com.casimir.loverun.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import com.casimir.loverun.Constant.PreferenceString;

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
            sharedPreferences = getSharedPreferences(PreferenceString.configInfo, MODE_PRIVATE);
            boolean alarm = sharedPreferences.getBoolean(PreferenceString.NOTIFICATIONS, true);
            if (alarm) {
                final OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
                    @Override
                    public void onAudioFocusChange(int focusChange) {
                    }
                };
                audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                int result = audioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_ALARM,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    Uri ringTone;
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
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(this, ringTone);
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                Log.i("AudioService", "start to play music");
                                mp.start(); // then start
                            }
                        });
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            audioManager.abandonAudioFocus(afChangeListener);
                            if(mp != null){
                                mp.release();
                            }
                        }
                    });
                }
            }
            //vibrator 3 second
            Boolean vibrate = sharedPreferences.getBoolean(PreferenceString.VIBRATOR, false);
            int permission = getPackageManager().checkPermission(VIBRATOR_SERVICE, getPackageName());
            Timber.i("permission: %s", permission); //some devices not need it and return -1
            if (vibrate) {
                Timber.i("vibrate 3000");
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(3000);
            } else {
                Timber.i("not permission");
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.i("onDestroy");
    }
}
