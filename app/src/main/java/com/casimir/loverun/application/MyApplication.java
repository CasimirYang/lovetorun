package com.casimir.loverun.application;

import android.app.Application;
import android.util.Log;

import com.casimir.loverun.BuildConfig;
import com.maxleap.MaxLeap;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by yjh on 16/8/23.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MaxLeap.initialize(this, "57bb20a601e3b30007d4d265", "ZmxxRWUxQWNmaElrMUw5OUt3TlJKQQ", MaxLeap.REGION_CN);

        ButterKnife.setDebug(BuildConfig.DEBUG);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
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
