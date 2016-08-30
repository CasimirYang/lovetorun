package com.casimir.loverun.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Button;

import com.casimir.loverun.Constant.PreferenceString;
import com.casimir.loverun.R;
import com.casimir.loverun.adapter.CourseTimeLineAdapter;
import com.casimir.loverun.base.BaseActivity;
import com.casimir.loverun.service.TimerService;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class CourseActivity extends BaseActivity {

    enum Status {
        IDLE, RUNNING,DONE
    }

    private CourseTimeLineAdapter courseTimeLineAdapter;
    private List<Long> timeList;
    private boolean fetchFromService = false;
    private Status status;

    public static final int PROGRESS_MESSAGE = 1;
    public static final int FINISH_MESSAGE = 2;
    public static final int REMAIN_TIME = 3;

    @BindView(R.id.RunButton) Button RunButton;
    @BindView(R.id.runView) RecyclerView recyclerView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(fetchFromService){
                if(msg.what == REMAIN_TIME){
                    Log.i("CourseActivity", "receive message from remain time:" + msg.obj);
                    long millisUntilFinished = (long) msg.obj;
                    courseTimeLineAdapter.setRemainTime(millisUntilFinished);
                    courseTimeLineAdapter.notifyDataSetChanged();
                    if(millisUntilFinished == 0){
                        changeRunStatus(Status.DONE);
                    }
                }
            }else{
                if (msg.what == PROGRESS_MESSAGE) {
                    Log.i("CourseActivity", "receive message from progress_message:" + msg.arg1);
                    courseTimeLineAdapter.setRemainTime(-2L);
                    courseTimeLineAdapter.setCurrentProgress(msg.arg1);
                    courseTimeLineAdapter.notifyItemChanged(msg.arg1);
                    recyclerView.smoothScrollToPosition(msg.arg1);
                } else if (msg.what == FINISH_MESSAGE) {
                    changeRunStatus(Status.DONE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("log", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        boolean keep_screen_on = getSharedPreferences(PreferenceString.configInfo, MODE_PRIVATE).
                getBoolean(PreferenceString.KEEP_SCREEN_ON,false);
        if(keep_screen_on){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        status = Status.IDLE;
        Intent intent = getIntent();
        int weekLevel = intent.getIntExtra(PreferenceString.weekLevel, 1);
        int courseLevel = intent.getIntExtra(PreferenceString.courseLevel, 1);
        init(weekLevel, courseLevel);

        courseTimeLineAdapter = new CourseTimeLineAdapter(timeList, this);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(courseTimeLineAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void init(int weekLevel, int courseLevel) {
        SharedPreferences sharedPreferences = getSharedPreferences(PreferenceString.userInfo, MODE_PRIVATE);
        String plan = sharedPreferences.getString(weekLevel + "_" + courseLevel + "_plan", null);
        String[] planList = plan.split(",");
        timeList = new ArrayList<>(planList.length);
        for (String item : planList) {
            timeList.add(Long.parseLong(item) * 60 * 1000);
        }
    }


  @OnClick(R.id.RunButton) public void RunButton() {
      if(status == Status.IDLE){
          changeRunStatus(Status.RUNNING);
          //start to  send message to refresh recycleView Item
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

          //start TimerService
          Intent serviceIntent = new Intent(this, TimerService.class);
          long[] timeArray = new long[timeList.size()];
          for (int i = 0;i< timeList.size();i++) {
              timeArray[i] = timeList.get(i);
          }
          serviceIntent.putExtra("handler", new Messenger(handler));
          serviceIntent.putExtra("timeArray",timeArray);
          startService(serviceIntent);
      }else if(status == Status.DONE){
          stopRun();
          setResult(RESULT_OK);
          finish();
      }
    }

    private void changeRunStatus(Status status){
        this.status = status;
        switch (status){
            case RUNNING:
                RunButton.setText(R.string.run_status_running);
                RunButton.setEnabled(false);
                break;
            case DONE:
                RunButton.setEnabled(true);
                RunButton.setText(R.string.run_status_done);
                RunButton.setBackgroundResource(R.drawable.finish_run_button);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.i("onstart");
        recyclerView.scrollToPosition(courseTimeLineAdapter.getCurrentProgress());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("log", "onRestart");
        fetchFromService = false;
    }


    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && status != Status.IDLE) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("课程尚未完成，确认要退出吗？");
            builder.setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stopRun();
                    onBackPressed();
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

    private void stopRun(){
        Intent stopTimerService = new Intent();
        stopTimerService.setAction("android.intent.action.TIMER_SERVICE");
        stopTimerService.setPackage(getPackageName());
        stopService(stopTimerService);
        Intent stopAudioService = new Intent();
        stopAudioService.setAction("android.intent.action.AUDIO_SERVICE");
        stopAudioService.setPackage(getPackageName());
        stopService(stopAudioService);
        handler.removeMessages(PROGRESS_MESSAGE);
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
    protected void onStop() {
        Log.d("log", "onStop");
        super.onStop();
        fetchFromService = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("log", "ondestroy");
        stopRun();
    }


}
