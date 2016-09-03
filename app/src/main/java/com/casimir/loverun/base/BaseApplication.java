package com.casimir.loverun.base;

import android.app.Application;
import android.util.Log;

import com.casimir.loverun.BuildConfig;

import butterknife.ButterKnife;
import timber.log.Timber;


public class BaseApplication extends Application {

  //  long sessionTimeOut = 1000*60*60;

    @Override
    public void onCreate() {
        super.onCreate();

      //  MobclickAgent.setSessionContinueMillis(sessionTimeOut);

     //   MaxLeap.initialize(this, "57ca6cabb0d85000071fbef9", "WXFIcUFyS2ZUYUFndmN4aFNOTl8wQQ", MaxLeap.REGION_CN);

        ButterKnife.setDebug(BuildConfig.DEBUG);

    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }
}
