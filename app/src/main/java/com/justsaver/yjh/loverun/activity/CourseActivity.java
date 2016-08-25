package com.justsaver.yjh.loverun.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.justsaver.yjh.loverun.Constant.PreferenceString;
import com.justsaver.yjh.loverun.R;
import com.justsaver.yjh.loverun.adapter.CourseTimeLineAdapter;
import com.justsaver.yjh.loverun.service.TimerService;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity implements View.OnClickListener {

    enum Status {
        IDLE, RUNNING
    }

    private CourseTimeLineAdapter courseTimeLineAdapter;
    private List<Long> timeList;
    private Button runDone;
    private boolean fetchFromService = false;
    private Status status = Status.IDLE;

    public static final int PROGRESS_MESSAGE = 1;
    public static final int FINISH_MESSAGE = 2;
    public static final int REMAIN_TIME = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == PROGRESS_MESSAGE) {
                Log.i("CourseActivity", "receive message from progress_message:" + msg.arg1);
                courseTimeLineAdapter.setRemainTime(-2L);
                courseTimeLineAdapter.setCurrentProgress(msg.arg1);
                courseTimeLineAdapter.notifyItemChanged(msg.arg1);
            } else if (msg.what == FINISH_MESSAGE) {
                runDone.setClickable(true);
            }
            if (fetchFromService) {
                if (msg.what == REMAIN_TIME) {
                    Log.i("CourseActivity", "receive message from remain time:" + msg.obj);
                    long millisUntilFinished = (long) msg.obj;
                    courseTimeLineAdapter.setRemainTime(millisUntilFinished);
                    courseTimeLineAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("log", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        //todo  add to allow user set
        boolean keep_screen_on = getSharedPreferences(PreferenceString.userInfo, MODE_PRIVATE).
                getBoolean(PreferenceString.keep_screen_on,false);
        if(keep_screen_on){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        Button startRun = (Button) findViewById(R.id.startRun);
        runDone = (Button) findViewById(R.id.RunDone);
        startRun.setOnClickListener(this);
        runDone.setOnClickListener(this);
        runDone.setClickable(false);
        Intent intent = getIntent();
        int weekLevel = intent.getIntExtra(PreferenceString.weekLevel, 1);
        int courseLevel = intent.getIntExtra(PreferenceString.courseLevel, 1);
        init(weekLevel, courseLevel);

        courseTimeLineAdapter = new CourseTimeLineAdapter(timeList, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.runView);
        recyclerView.setAdapter(courseTimeLineAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void init(int weekLevel, int courseLevel) {
//        SharedPreferences sharedPreferences = getSharedPreferences(PreferenceString.userInfo, MODE_PRIVATE);
//        String plan = sharedPreferences.getString(weekLevel + "_" + courseLevel + "_plan", null);
//        String[] planList = plan.split(",");
//        timeList = new ArrayList<>(planList.length);
//        for (String item : planList) {
//            timeList.add(Long.parseLong(item));
//        }
        timeList = new ArrayList<>();
    timeList.add(60000L);
    timeList.add(120000L);
    timeList.add(240000L);
    timeList.add(300000L);
    timeList.add(60000L);
    timeList.add(60000L);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startRun:
                startToRun();
                status = Status.RUNNING;
                Intent serviceIntent = new Intent(this, TimerService.class);
                long[] timeArray = new long[timeList.size()];
                for (int i = 0;i< timeList.size();i++) {
                    timeArray[i] = timeList.get(i);
                }
                serviceIntent.putExtra("handler", new Messenger(handler));
                serviceIntent.putExtra("timeArray",timeArray);
                startService(serviceIntent);
                break;
            case R.id.RunDone:
                //todo popup complete info
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }

    private void startToRun() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = timeList.size();
                long runAtTime = 0;
                for (int i = 0; i < size; i++) {
                    Message message = new Message();
                    message.what = PROGRESS_MESSAGE;
                    message.arg1 = i;
                    handler.sendMessageDelayed(message, runAtTime);
                    runAtTime = runAtTime + timeList.get(i);
                    Log.i("send message", "startToRun send message:" + i);
                }
                Message message = new Message();
                message.what = FINISH_MESSAGE;
                handler.sendMessageDelayed(message, runAtTime);
            }
        }).start();
    }


    @Override
    protected void onRestart() {
        fetchFromService = false;
        Log.d("log", "onRestart");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        fetchFromService = true;
        Log.d("log", "onPause");
        super.onPause();
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && status == Status.RUNNING) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("正在跑步中，确认要退出吗？");
            builder.setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent stopTimer = new Intent();
                    stopTimer.setAction("android.intent.action.TIMER_SERVICE");
                    stopTimer.setPackage(getPackageName());
                    stopService(stopTimer);
                    handler.removeMessages(PROGRESS_MESSAGE);
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("log", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("log", "onRestoreInstanceState");
    }

    @Override
    protected void onResume() {
        Log.d("log", "onResume");
        super.onResume();
    }


    @Override
    protected void onStop() {
        Log.d("log", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("log", "ondestroy");
        super.onDestroy();
    }


}
