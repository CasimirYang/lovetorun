package com.justsaver.yjh.loverun.service;

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

import com.justsaver.yjh.loverun.activity.CourseActivity;

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


            //alarm
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            long expire = 0;
            for(int i=0; i<timeArray.length; i++){
               Intent audioIntent = new Intent("android.intent.action.AUDIO_SERVICE");
                audioIntent.setPackage(getPackageName());
                audioIntent.putExtra("item",i);
                PendingIntent pendingIntent = PendingIntent.getService(this,i,
                        audioIntent,PendingIntent.FLAG_ONE_SHOT);

                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime()+expire,pendingIntent);
                expire = expire + timeArray[i];
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("TimerService","cancel countDownTimer and alarm");
        Intent audioIntent = new Intent();
        audioIntent.setAction("android.intent.action.AUDIO_SERVICE");
        audioIntent.setPackage(getPackageName());
        for(int i=0; i<timeArray.length; i++){
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(this,i,
                    audioIntent,PendingIntent.FLAG_NO_CREATE);
            if(pendingIntent != null){
                alarmManager.cancel(pendingIntent);
            }
        }
        countDownTimer.cancel();
        super.onDestroy();
    }

}
