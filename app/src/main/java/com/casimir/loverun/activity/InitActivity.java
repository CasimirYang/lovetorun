package com.casimir.loverun.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.casimir.loverun.Constant.PreferenceString;
import com.casimir.loverun.R;
import com.casimir.loverun.base.BaseActivity;

import timber.log.Timber;

public class InitActivity extends BaseActivity {

    public static final int INIT_DONE = 1;

    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == INIT_DONE){
                startActivity(new Intent(InitActivity.this,MainActivity.class));
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        if(ifFirstRun()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long i1 = SystemClock.currentThreadTimeMillis();
                    loadData();
                    long i2 = SystemClock.currentThreadTimeMillis();
                    Timber.i("spent time: %d",i2-i1);
                    Message message = Message.obtain();
                    message.what = INIT_DONE;
                    handler.sendMessage(message);
                }
            }).start();
        }else{
           startActivity(new Intent(this,MainActivity.class));
        }

    }

    public boolean ifFirstRun() {
        boolean isFirstRun = getSharedPreferences(PreferenceString.configInfo, MODE_PRIVATE).
                getBoolean(PreferenceString.isFirstRun, true);
        if (isFirstRun){
            getSharedPreferences(PreferenceString.configInfo, MODE_PRIVATE)
                    .edit()
                    .putBoolean(PreferenceString.isFirstRun, false)
                    .apply();
        }
        return isFirstRun;
    }


    private void loadData(){
        SharedPreferences.Editor editor = getSharedPreferences(PreferenceString.userInfo, MODE_PRIVATE).edit();
        editor.putString("1_0_text","3个原则：适度、坚持、休息");
        editor.putString("2_0_text","建立基础");
        editor.putString("3_0_text","增加跑步的时间");
        editor.putString("4_0_text","恢复期");
        editor.putString("5_0_text","注意慢跑节奏");
        editor.putString("6_0_text","增加训练量");
        editor.putString("7_0_text","训练过了一半");
        editor.putString("8_0_text","轻松的恢复周");
        editor.putString("9_0_text","回到训练中");
        editor.putString("10_0_text","漫长的一周");
        editor.putString("11_0_text","树立信心");
        editor.putString("12_0_text","轻松的一周");
        editor.putString("13_0_text","最后一周,新的开始");


        editor.putString("1_1_plan","1,2,1,2,1,2,1,2,1,2,1,2,1,2,1,2");
        editor.putString("1_2_plan","1,2,1,2,1,2,1,2,1,2,1,2");
        editor.putString("1_3_plan","1,2,1,2,1,2,1,2,1,2,1,2,1,2");
        editor.putString("1_1_text","跑步1分钟。行走2分钟。共做6次");
        editor.putString("1_2_text","跑步1分钟。行走2分钟。共做8次");
        editor.putString("1_3_text","跑步1分钟。行走2分钟。共做7次");

        editor.putString("2_1_plan","2,2,2,2,2,2,2,2,2,2,2,2,2,2");
        editor.putString("2_2_plan","1,2,1,2,1,2,1,2,1,2,1,2,1,2");
        editor.putString("2_3_plan","2,2,2,2,2,2,2,2,2,2,2,2");
        editor.putString("2_1_text","跑步2分钟。行走2分钟。共做7次");
        editor.putString("2_2_text","跑步1分钟。行走2分钟。共做7次");
        editor.putString("2_3_text","跑步2分钟。行走2分钟。共做6次");

        editor.putString("3_1_plan","3,2,3,2,3,2,3,2,3,2,3,2,3,2");
        editor.putString("3_2_plan","2,2,2,2,2,2,2,2,2,2,2,2");
        editor.putString("3_3_plan","3,2,3,2,3,2,3,2,3,2,3,2");
        editor.putString("3_1_text","跑步3分钟。行走2分钟。共做7次");
        editor.putString("3_2_text","跑步2分钟。行走2分钟。共做6次");
        editor.putString("3_3_text","跑步3分钟。行走2分钟。共做6次");

        editor.putString("4_1_plan","3,2,3,2,3,2,3,2,3,2,3,2");
        editor.putString("4_2_plan","2,2,2,2,2,2,2,2,2,2");
        editor.putString("4_3_plan","2,3,2,3,2,3,2,3,2,3,2,3");
        editor.putString("4_1_text","跑步3分钟。行走2分钟。共做6次");
        editor.putString("4_2_text","跑步2分钟。行走2分钟。共做5次");
        editor.putString("4_3_text","跑步2分钟。行走3分钟。共做6次");

        editor.putString("5_1_plan","3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1");
        editor.putString("5_2_plan","2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1");
        editor.putString("5_3_plan","3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1");
        editor.putString("5_1_text","跑步3分钟。行走1分钟。共做9次");
        editor.putString("5_2_text","跑步2分钟。行走1分钟。共做8次");
        editor.putString("5_3_text","跑步3分钟。行走1分钟。共做8次");

        editor.putString("6_1_plan","5,1,5,1,5,1,5,1,5,1,5,1,5,1");
        editor.putString("6_2_plan","3,1,3,1,3,1,3,1,3,1,3,1,3,1");
        editor.putString("6_3_plan","3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1");
        editor.putString("6_1_text","跑步5分钟。行走1分钟。共做7次");
        editor.putString("6_2_text","跑步3分钟。行走1分钟。共做7次");
        editor.putString("6_3_text","跑步3分钟。行走1分钟。共做10次");

        editor.putString("7_1_plan","10,1,10,1,10,1,10,1");
        editor.putString("7_2_plan","4,1,4,1,4,1,4,1,4,1,4,1");
        editor.putString("7_3_plan","5,1,5,1,5,1,5,1,5,1,5,1,5,1");
        editor.putString("7_1_text","跑步10分钟。行走1分钟。共做4次");
        editor.putString("7_2_text","跑步4分钟。行走1分钟。共做6次");
        editor.putString("7_3_text","跑步5分钟。行走1分钟。共做7次");

        editor.putString("8_1_plan","10,1,10,1,10,1,10,1");
        editor.putString("8_2_plan","3,1,3,1,3,1,3,1,3,1,3,1,3,1");
        editor.putString("8_3_plan","5,1,5,1,5,1,5,1,5,1,5,1");
        editor.putString("8_1_text","跑步10分钟。行走1分钟。共做4次");
        editor.putString("8_2_text","跑步3分钟。行走1分钟。共做7次");
        editor.putString("8_3_text","跑步5分钟。行走1分钟。共做6次");

        editor.putString("9_1_plan","10,1,15,1,20,1,10,1");
        editor.putString("9_2_plan","5,1,5,1,5,1,5,1,5,1,5,1");
        editor.putString("9_3_plan","10,1,10,1,10,1,10,1");
        editor.putString("9_1_text","增强训练 挑战一下！");
        editor.putString("9_2_text","跑步5分钟。行走1分钟。共做6次");
        editor.putString("9_3_text","跑步10分钟。行走1分钟。共做4次");

        editor.putString("10_1_plan","10,1,20,1,15,1");
        editor.putString("10_2_plan","10,1,10,1,10,1,10,1");
        editor.putString("10_3_plan","20,1,15,10,1");
        editor.putString("10_1_text","增强日 10,20,15间断跑");
        editor.putString("10_2_text","跑步10分钟。行走1分钟。共做4次");
        editor.putString("10_3_text","增强日 20,15,10间断跑");

        editor.putString("11_1_plan","40,1");
        editor.putString("11_2_plan","10,1,10,1,10,1,10,1");
        editor.putString("11_3_plan","20,1,15,1,10,1");
        editor.putString("11_1_text","挑战日 目标40分钟!");
        editor.putString("11_2_text","跑步10分钟。行走1分钟。共做4次");
        editor.putString("11_3_text","20,15,10间断跑");

        editor.putString("12_1_plan","10,1,10,1,10,1,10,1");
        editor.putString("12_2_plan","4,1,4,1,4,1,4,1,4,1,4,1");
        editor.putString("12_3_plan","5,1,5,1,5,1,5,1,5,1,5,1,5,1");
        editor.putString("12_1_text","跑步10分钟。行走1分钟。共做4次");
        editor.putString("12_2_text","跑步4分钟。行走1分钟。共做6次");
        editor.putString("12_3_text","跑步5分钟。行走1分钟。共做7次");

        editor.putString("13_1_plan","40,1");
        editor.putString("13_2_plan","10,1,10,1,10,1");
        editor.putString("13_3_plan","40,1");
        editor.putString("13_1_text","跑步40分钟,行走1分钟");
        editor.putString("13_2_text","跑步10分钟,行走1分钟。共做3次");
        editor.putString("13_3_text","跟着感觉跑！");

        editor.apply();
    }
}
