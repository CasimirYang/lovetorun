package com.justsaver.yjh.loverun.activity;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.justsaver.yjh.loverun.Constant.PreferenceString;
import com.justsaver.yjh.loverun.R;
import com.justsaver.yjh.loverun.widget.FeedBackDialogFragment;
import com.maxleap.GetCallback;
import com.maxleap.MLDataManager;
import com.maxleap.MLObject;
import com.maxleap.MLQueryManager;
import com.maxleap.MaxLeap;
import com.maxleap.exception.MLException;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.List;
import java.util.logging.LogRecord;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    final private static int ORIENTATION_SWITCH = 1;
    final private static int NOTIFICAATION_SWITCH = 2;

    final private static int RUN = 3;
    final private static int REST = 4;

    private SharedPreferences sharedPreferences ;
    SwitchCompat orientation_switch;
    SwitchCompat notifications_coursing;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
           switch (msg.what){
               case ORIENTATION_SWITCH:
                   editor.putBoolean(PreferenceString.ORIENTATION, (Boolean) msg.obj);
                   break;
               case NOTIFICAATION_SWITCH:
                   editor.putBoolean(PreferenceString.NOTIFICATIONS, (Boolean) msg.obj);
                   break;
           }
            editor.apply();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        sharedPreferences = getSharedPreferences(PreferenceString.configInfo, MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.start_run).setOnClickListener(this);
        findViewById(R.id.start_rest).setOnClickListener(this);
        findViewById(R.id.suggest).setOnClickListener(this);
        findViewById(R.id.license).setOnClickListener(this);
        findViewById(R.id.check_upgrade).setOnClickListener(this);


        findViewById(R.id.orientation_switch_context).setOnClickListener(this);
        findViewById(R.id.notifications_coursing_context).setOnClickListener(this);

        orientation_switch = (SwitchCompat) findViewById(R.id.orientation_switch);
        orientation_switch.setChecked( sharedPreferences.getBoolean(PreferenceString.ORIENTATION,true) );
        orientation_switch.setOnCheckedChangeListener(this);

        notifications_coursing = (SwitchCompat) findViewById(R.id.notifications_coursing);
        notifications_coursing.setChecked(sharedPreferences.getBoolean(PreferenceString.NOTIFICATIONS,true));
        notifications_coursing.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
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
            case R.id.orientation_switch_context:
                orientation_switch.toggle();
                break;
            case R.id.notifications_coursing_context:
                notifications_coursing.toggle();
                break;
            case R.id.start_run:
                Intent run = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                run.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone:");
                startActivityForResult(run,0);
                break;
            case R.id.start_rest:
                Intent rest = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                rest.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select ringtone:");
                startActivityForResult(rest,1);
                break;
            case R.id.license:
                startActivity(new Intent(this,LicenseActivity.class));
                break;
            case R.id.check_upgrade:
                String objId = "57bb37aa01e3b30007d4dcfd";
                MLQueryManager.getInBackground("version",objId, new GetCallback<MLObject>() {
                    @Override
                    public void done(MLObject object, MLException e) {
                        if(e != null){
                            Toast.makeText(SettingActivity.this,"无法连接",Toast.LENGTH_SHORT).show();
                        }else{
                            int version = object.getInt("version");
                            PackageInfo pi = null;
                            try {
                                pi = getPackageManager().getPackageInfo(getPackageName(),0);
                            } catch (PackageManager.NameNotFoundException e1) {
                                e1.printStackTrace();
                            }
                            if(pi.versionCode == version){
                                Toast.makeText(SettingActivity.this,"当前版本已经是最新版",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SettingActivity.this,"有新版本",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                break;
            case R.id.suggest:
                showEditDialog();
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Message message = handler.obtainMessage();
        message.obj = isChecked;
        switch (buttonView.getId()){
            case R.id.orientation_switch:
                message.what = ORIENTATION_SWITCH;
                break;
            case R.id.notifications_coursing:
                message.what = NOTIFICAATION_SWITCH;
                break;
        }
        handler.sendMessage(message);
    }

    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        FeedBackDialogFragment feedBackDialogFragment = FeedBackDialogFragment.newInstance(null,null);
        feedBackDialogFragment.show(fm, "feedBackDialogFragment");
    }

}
