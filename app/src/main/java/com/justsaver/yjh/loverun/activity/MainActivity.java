package com.justsaver.yjh.loverun.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.justsaver.yjh.loverun.Constant.PreferenceString;
import com.justsaver.yjh.loverun.R;
import com.justsaver.yjh.loverun.adapter.WeekCardAdapter;
import com.justsaver.yjh.loverun.data.WeekCard;
import com.justsaver.yjh.loverun.fragment.WeekCardFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private long pressTime = 0;
    public static final int START_RUN = 1;
    private int weekLevel;
    private int courseLevel;

@BindView(R.id.ViewPager)  ViewPager viewPager;
@BindView(R.id.toolbar) Toolbar toolbar;
@BindView(R.id.setting) AppCompatImageView setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setting.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(ifFirstRun()){
            loadData();
        }

        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageMargin(200); //todo change to from DP
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return 13;
            }

            @Override
            public Fragment getItem(int position) {
                Timber.i("getItem %d",position);
                return WeekCardFragment.newInstance(position);
            }
        });
        //viewPager.setCurrentItem();

//        materialViewPager.getPagerTitleStrip().setViewPager(materialViewPager.getViewPager());
//        materialViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
//            @Override
//            public HeaderDesign getHeaderDesign(int page) {
//                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.wallpaper_512, null);
//                return HeaderDesign.fromColorResAndDrawable(R.color.blue,drawable);
//            }
//        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == START_RUN &&  resultCode == RESULT_OK){
            SharedPreferences.Editor editor = getSharedPreferences(PreferenceString.userInfo, Context.MODE_PRIVATE).edit();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getDefault());
            String currentDateAndTime = sdf.format(new Date());
            editor.putString(weekLevel+"_"+courseLevel,currentDateAndTime);
            Log.i("save level",weekLevel+"_"+courseLevel);
            if(courseLevel == 3){
                weekLevel = weekLevel+1;
                courseLevel = 1;
            }else{
                courseLevel = courseLevel+1;
            }
            editor.putInt(PreferenceString.weekLevel,weekLevel);
            editor.putInt(PreferenceString.courseLevel,courseLevel);
            editor.apply();
            if(weekLevel > 13){
                //todo  popup message: finish all course
            }
//            adapter.LevelUp(weekLevel,courseLevel);
//            if(courseLevel == 1){
//                adapter.notifyItemChanged(weekLevel-2);
//                adapter.notifyItemChanged(weekLevel-1);
//            }else {
//                adapter.notifyItemChanged(weekLevel-1);
//            }
        }
    }

    public boolean ifFirstRun() {
        boolean isFirstRun = getSharedPreferences(PreferenceString.userInfo, MODE_PRIVATE).
                getBoolean(PreferenceString.isFirstRun, true);
        if (isFirstRun){
            getSharedPreferences(PreferenceString.userInfo, MODE_PRIVATE)
                    .edit()
                    .putBoolean(PreferenceString.isFirstRun, false)
                    .apply();
        }
        return isFirstRun;
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            if(SystemClock.elapsedRealtime() - pressTime < 3000){
//                return super.onKeyDown(keyCode, event);
//            }else {
//                Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
//                pressTime = SystemClock.elapsedRealtime();
//                return true;
//            }
//        }else{
//            return super.onKeyDown(keyCode, event);
//        }
//    }

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
        editor.putString("1_1_text","跑步1分钟。行走2分钟。共做6次。");
        editor.putString("1_2_text","跑步1分钟。行走2分钟。共做8次。");
        editor.putString("1_3_text","跑步1分钟。行走2分钟。共做7次。");

        editor.putString("2_1_plan","2,2,2,2,2,2,2,2,2,2,2,2,2,2");
        editor.putString("2_2_plan","1,2,1,2,1,2,1,2,1,2,1,2,1,2");
        editor.putString("2_3_plan","2,2,2,2,2,2,2,2,2,2,2,2");
        editor.putString("2_1_text","跑步2分钟。行走2分钟。共做7次。");
        editor.putString("2_2_text","跑步1分钟。行走2分钟。共做7次。");
        editor.putString("2_3_text","跑步2分钟。行走2分钟。共做6次。");

        editor.putString("3_1_plan","3,2,3,2,3,2,3,2,3,2,3,2,3,2");
        editor.putString("3_2_plan","2,2,2,2,2,2,2,2,2,2,2,2");
        editor.putString("3_3_plan",",2,3,2,3,2,3,2,3,2,3,2");
        editor.putString("3_1_text","跑步3分钟。行走2分钟。共做7次。");
        editor.putString("3_2_text","跑步2分钟。行走2分钟。共做6次。");
        editor.putString("3_3_text","跑步3分钟。行走2分钟。共做6次。");

        editor.putString("4_1_plan","3,2,3,2,3,2,3,2,3,2,3,2");
        editor.putString("4_2_plan","2,2,2,2,2,2,2,2,2,2");
        editor.putString("4_3_plan","2,3,2,3,2,3,2,3,2,3,2,3");
        editor.putString("4_1_text","跑步3分钟。行走2分钟。共做6次。");
        editor.putString("4_2_text","跑步2分钟。行走2分钟。共做5次。");
        editor.putString("4_3_text","跑步2分钟。行走3分钟。共做6次。");

        editor.putString("5_1_plan","3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1");
        editor.putString("5_2_plan","2,1,2,1,2,1,2,1,2,1,2,1,2,1,2,1");
        editor.putString("5_3_plan","3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1");
        editor.putString("5_1_text","跑步3分钟。行走1分钟。共做9次。");
        editor.putString("5_2_text","跑步2分钟。行走1分钟。共做8次。");
        editor.putString("5_3_text","跑步3分钟。行走1分钟。共做8次。");

        editor.putString("6_1_plan","5,1,5,1,5,1,5,1,5,1,5,1,5,1");
        editor.putString("6_2_plan","3,1,3,1,3,1,3,1,3,1,3,1,3,1");
        editor.putString("6_3_plan","3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1,3,1");
        editor.putString("6_1_text","跑步5分钟。行走1分钟。共做7次。");
        editor.putString("6_2_text","跑步3分钟。行走1分钟。共做7次。");
        editor.putString("6_3_text","跑步3分钟。行走1分钟。共做10次。");

        editor.putString("7_1_plan","10,1,10,1,10,1,10,1");
        editor.putString("7_2_plan","4,1,4,1,4,1,4,1,4,1,4,1");
        editor.putString("7_3_plan","5,1,5,1,5,1,5,1,5,1,5,1,5,1");
        editor.putString("7_1_text","跑步10分钟。行走1分钟。共做4次。");
        editor.putString("7_2_text","跑步4分钟。行走1分钟。共做6次。");
        editor.putString("7_3_text","跑步5分钟。行走1分钟。共做7次。");

        //todo -------------------------------
        editor.putString("8_1_plan","");
        editor.putString("8_2_plan","");
        editor.putString("8_3_plan","");
        editor.putString("8_1_text","跑步10分钟。行走1分钟。共做4次。");
        editor.putString("8_2_text","跑步4分钟。行走1分钟。共做6次。");
        editor.putString("8_3_text","跑步5分钟。行走1分钟。共做7次。");

        editor.putString("9_1_plan","");
        editor.putString("9_2_plan","");
        editor.putString("9_3_plan","");
        editor.putString("9_1_text","跑步10分钟。行走1分钟。共做4次。");
        editor.putString("9_2_text","跑步4分钟。行走1分钟。共做6次。");
        editor.putString("9_3_text","跑步5分钟。行走1分钟。共做7次。");

        editor.putString("10_1_plan","");
        editor.putString("10_2_plan","");
        editor.putString("10_3_plan","");
        editor.putString("10_1_text","跑步10分钟。行走1分钟。共做4次。");
        editor.putString("10_2_text","跑步4分钟。行走1分钟。共做6次。");
        editor.putString("10_3_text","跑步5分钟。行走1分钟。共做7次。");

        editor.putString("11_1_plan","");
        editor.putString("11_2_plan","");
        editor.putString("11_3_plan","");
        editor.putString("11_1_text","跑步10分钟。行走1分钟。共做4次。");
        editor.putString("11_2_text","跑步4分钟。行走1分钟。共做6次。");
        editor.putString("11_3_text","跑步5分钟。行走1分钟。共做7次。");

        editor.putString("12_1_plan","");
        editor.putString("12_2_plan","");
        editor.putString("12_3_plan","");
        editor.putString("12_1_text","跑步10分钟。行走1分钟。共做4次。");
        editor.putString("12_2_text","跑步4分钟。行走1分钟。共做6次。");
        editor.putString("12_3_text","跑步5分钟。行走1分钟。共做7次。");

        editor.putString("13_1_plan","");
        editor.putString("13_2_plan","");
        editor.putString("13_3_plan","");
        editor.putString("13_1_text","跑步10分钟。行走1分钟。共做4次。");
        editor.putString("13_2_text","跑步4分钟。行走1分钟。共做6次。");
        editor.putString("13_3_text","跑步5分钟。行走1分钟。共做7次。");

        editor.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting:
               Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
