package com.casimir.loverun.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.casimir.loverun.Constant.PreferenceString;
import com.casimir.loverun.R;
import com.casimir.loverun.base.BaseActivity;
import com.casimir.loverun.fragment.WeekCardFragment;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private long pressTime = 0;
    public static final int START_RUN = 101;
    private int weekLevel;
    private int courseLevel;

@BindView(R.id.ViewPager)  ViewPager viewPager;
@BindView(R.id.toolbar) Toolbar toolbar;
@BindView(R.id.setting) AppCompatImageView setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("onCreate");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setting.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences sharedPreferences = getSharedPreferences(PreferenceString.userInfo, Context.MODE_PRIVATE);
        weekLevel = sharedPreferences.getInt(PreferenceString.weekLevel,1);
        courseLevel = sharedPreferences.getInt(PreferenceString.courseLevel,1);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageMargin(dip2px(this,50));
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

            @Override
            public CharSequence getPageTitle(int position) {
                return String.valueOf(position+1);
            }

        });
        viewPager.setCurrentItem(weekLevel-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
        Timber.i("onActivityResult %d,%d ",requestCode,resultCode);
        viewPager.refreshDrawableState();

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


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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
