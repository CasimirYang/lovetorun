package com.casimir.loverun.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.casimir.loverun.activity.CourseActivity;

import timber.log.Timber;

public class TimerService extends Service {

    private CountDownTimer countDownTimer;
    private long[] timeArray;

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TimerService","onStartCommand");
        if (intent != null) {
            final Messenger messenger = (Messenger) intent.getExtras().get("handler");
            timeArray = intent.getLongArrayExtra("timeArray");
            //do running progress
            long timeSum =0;
            for(long item : timeArray){
                timeSum = timeSum + item;
            }
            countDownTimer = new CountDownTimer(timeSum, 1000) {
                long millisUntilFinished;

                public void onTick(long millis) {
                    sendMessage(millis);
                }

                @Override
                public void onFinish() {
                    sendMessage(0L);
                }

                private void sendMessage(long millis){
                    Message msg = Message.obtain();
                    millisUntilFinished = millis;
                    msg.what = CourseActivity.REMAIN_TIME;
                    msg.obj = millisUntilFinished;
                    int currentProgress = 0;
                        long temp = 0;
                        for(int i=timeArray.length-1; i>=0; i--){
                            temp = temp + timeArray[i];
                            if(millisUntilFinished < temp){
                                currentProgress = i;
                                break;
                            }
                        }
                    msg.arg1 = currentProgress;
                    if(currentProgress > CourseActivity.currentProgress){
                        sentAlarm(currentProgress);
                        msg.arg2 = 1;
                    }else {
                        msg.arg2 = 0; //not refresh
                    }
                    try {
                        messenger.send(msg);
                        Log.i("TimerService", "send message:" + millisUntilFinished);
                    } catch (RemoteException e) {
                        Log.i(getClass().getName(), "Exception sending message", e);
                        e.printStackTrace();
                    }
                }
            };
            countDownTimer.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void sentAlarm(int currentProgress) {
        Intent audioIntent = new Intent("android.intent.action.AUDIO_SERVICE");
        audioIntent.setPackage(getPackageName());
        audioIntent.putExtra("item",currentProgress);
        startService(audioIntent);
    }

    @Override
    public void onDestroy() {
        Log.i("TimerService","cancel countDownTimer and alarm");
        countDownTimer.cancel();
        super.onDestroy();
    }

}
