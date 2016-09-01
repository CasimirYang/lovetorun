package com.casimir.loverun.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.artitk.licensefragment.ScrollViewLicenseFragment;
import com.artitk.licensefragment.model.License;
import com.artitk.licensefragment.model.LicenseType;
import com.casimir.loverun.R;
import com.casimir.loverun.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class LicenseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.license);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<License> customLicenses = new ArrayList<>();
        customLicenses.add(new License(this, "Circle-Progress-View", LicenseType.MIT_LICENSE, "2015", "jakob-grabner"));
        customLicenses.add(new License(this, "hugo", LicenseType.APACHE_LICENSE_20, "2013", "Jake Wharton"));
        customLicenses.add(new License(this, "timber", LicenseType.APACHE_LICENSE_20, "2013", "Jake Wharton"));
        customLicenses.add(new License(this, "butterknife", LicenseType.APACHE_LICENSE_20, "2013", "Jake Wharton"));


        ScrollViewLicenseFragment fragment = ScrollViewLicenseFragment.newInstance();
        fragment.addCustomLicense(customLicenses);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frameContainer,fragment);
        transaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
