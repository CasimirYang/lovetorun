package com.casimir.loverun.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.casimir.loverun.Constant.PreferenceString;
import com.casimir.loverun.R;
import com.casimir.loverun.base.BaseActivity;
import com.casimir.loverun.widget.FeedBackDialogFragment;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import timber.log.Timber;

public class SettingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.start_run)
    RelativeLayout runLayout;

    @BindView(R.id.start_rest)
    RelativeLayout restLayout;

    final private static int KEEP_SCREEN_ON_SWITCH = 1;
    final private static int NOTIFICAATION_SWITCH = 2;
    final private static int WIBRATOR_SWITCH = 3;

    final private static int RUN = 3;
    final private static int REST = 4;

    private SharedPreferences sharedPreferences ;
    SwitchCompat keepScreenOnSwitch;
    SwitchCompat musicOnSwitch;
    SwitchCompat vibrator;

    private Handler handler = new SettingHandler(this);

    public static class SettingHandler extends Handler{


        WeakReference<SettingActivity> initActivityWeakReference;
        public SettingHandler(SettingActivity activity) {
            initActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SettingActivity settingActivity = initActivityWeakReference.get();
            if(settingActivity != null){
            SharedPreferences.Editor editor = settingActivity.getSharedPreferences(PreferenceString.configInfo, MODE_PRIVATE).edit();
            Timber.i("%s %s",msg.what,msg.obj);
            switch (msg.what){
                case KEEP_SCREEN_ON_SWITCH:
                    editor.putBoolean(PreferenceString.KEEP_SCREEN_ON, (Boolean) msg.obj);
                    break;
                case NOTIFICAATION_SWITCH:
                    editor.putBoolean(PreferenceString.NOTIFICATIONS, (Boolean) msg.obj);
                    break;
                case WIBRATOR_SWITCH:
                    editor.putBoolean(PreferenceString.VIBRATOR, (Boolean) msg.obj);
                    break;
            }
            editor.apply();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        Timber.i("onCreate");
        sharedPreferences = getSharedPreferences(PreferenceString.configInfo, MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        runLayout.setOnClickListener(this);
        restLayout.setOnClickListener(this);

        findViewById(R.id.suggest).setOnClickListener(this);
        findViewById(R.id.license).setOnClickListener(this);
        findViewById(R.id.check_upgrade).setOnClickListener(this);

        findViewById(R.id.screen_on_switch_context).setOnClickListener(this);
        findViewById(R.id.musicOnSwitch_context).setOnClickListener(this);
        findViewById(R.id.vibrator_switch_context).setOnClickListener(this);

        keepScreenOnSwitch = (SwitchCompat) findViewById(R.id.screen_on_switch);
        keepScreenOnSwitch.setChecked( sharedPreferences.getBoolean(PreferenceString.KEEP_SCREEN_ON,false) );
        keepScreenOnSwitch.setOnCheckedChangeListener(this);

        musicOnSwitch = (SwitchCompat) findViewById(R.id.musicOnSwitch);
        musicOnSwitch.setChecked(sharedPreferences.getBoolean(PreferenceString.NOTIFICATIONS,true));
        musicOnSwitch.setOnCheckedChangeListener(this);

        vibrator = (SwitchCompat) findViewById(R.id.vibrator_switch);
        vibrator.setChecked(sharedPreferences.getBoolean(PreferenceString.VIBRATOR,false));
        vibrator.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if(requestCode == RUN){
                editor.putString(PreferenceString.runTimeRingtone,uri.toString());
            }else if(requestCode == REST){
                editor.putString(PreferenceString.restTimeRingtone,uri.toString());
            }
            editor.apply();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.screen_on_switch_context:
                keepScreenOnSwitch.toggle();
                break;
            case R.id.vibrator_switch_context:
                vibrator.toggle();
                break;
            case R.id.musicOnSwitch_context:
                musicOnSwitch.toggle();
                break;
            case R.id.start_run:
                Intent run = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                run.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                run.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone:");
//                if (sharedPreferences.contains(PreferenceString.runTimeRingtone)) {
//                    Uri ringTone = Uri.parse(sharedPreferences.getString(PreferenceString.runTimeRingtone, ""));
//                    run.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT,ringTone);
//                }
                startActivityForResult(run,3);
                break;
            case R.id.start_rest:
                Intent rest = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                rest.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                rest.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone:");
//                if (sharedPreferences.contains(PreferenceString.restTimeRingtone)) {
//                    Uri ringTone = Uri.parse(sharedPreferences.getString(PreferenceString.restTimeRingtone, ""));
//                    rest.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT,ringTone);
//                }
                startActivityForResult(rest,4);
                break;
            case R.id.license:
                startActivity(new Intent(this,LicenseActivity.class));
                break;
            case R.id.check_upgrade:
                BmobQuery<Version> bmobQuery = new BmobQuery<>();
                bmobQuery.getObject("OHMu777E", new QueryListener<Version>() {
                    @Override
                    public void done(Version object,BmobException e) {
                        if(e==null){
                            int version = object.getVersion();
                            PackageInfo pi = null;
                            try {
                                pi = getPackageManager().getPackageInfo(getPackageName(),0);
                            } catch (PackageManager.NameNotFoundException e1) {
                                e1.printStackTrace();
                            }
                            if(pi.versionCode == version){
                                Toast.makeText(SettingActivity.this,"当前版本已经是最新版",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SettingActivity.this,"有新版本,赶紧去应用市场下载吧",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SettingActivity.this,"无法连接",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.suggest:
                showEditDialog();
                break;
        }
    }

    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        FeedBackDialogFragment feedBackDialogFragment = FeedBackDialogFragment.newInstance();
        feedBackDialogFragment.show(fm, "feedBackDialogFragment");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.i("onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.i("onStop");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Message message = handler.obtainMessage();
        message.obj = isChecked;
        switch (buttonView.getId()){
            case R.id.screen_on_switch:
                message.what = KEEP_SCREEN_ON_SWITCH;
                break;
            case R.id.musicOnSwitch:
                message.what = NOTIFICAATION_SWITCH;
                break;
            case R.id.vibrator_switch:
                message.what = WIBRATOR_SWITCH;
                break;
        }
        handler.sendMessage(message);
    }

    public class Version extends BmobObject {
        private Integer version;

        public Integer getVersion() {
            return version;
        }

        public void setVersion(Integer version) {
            this.version = version;
        }
    }


}
