package com.mark.passwordlocker.counter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mark.passwordlocker.application.MyApplication;
import com.mark.passwordlocker.helpers.ApplicationPassword;
import com.mark.passwordlocker.helpers.ApplicationPreferences;

/**
 * Created by mark on 1/6/16.
 */
public class LockPasswordTask {

    private int mSeconds;
    private static LockPasswordTask sInstance = new LockPasswordTask();
    private SecondsToLockReceiver mReceiver;
    private Runnable mRunnable;

    private LockPasswordTask(){
        setupBroadcastReceiver();
        getRunnable();
    }

    public static LockPasswordTask getInstance(){
        return sInstance;
    }

    public void startTimer(){
        mSeconds = ApplicationPreferences.getInstance().getSecondsToLockApplication();
        if ( mSeconds >= 30){
            TaskManager.getInstance().insertTask(mRunnable, mSeconds);
        }
    }

    int getSeconds(){
        return mSeconds;
    }

    private void updateTimer(){
        suspendTimer();
        startTimer();
    }

    public void suspendTimer(){
        TaskManager.getInstance().removeTask(mRunnable);
    }

    public void resumeTimer(){
        startTimer();
    }

    private Runnable getRunnable(){
        if( null != mRunnable){
            return mRunnable;
        }
        mRunnable = new Runnable() {
            @Override
            public void run() {
                ApplicationPassword.getInstance().lockPassword();
            }
        };
        return mRunnable;
    }

    private void setupBroadcastReceiver(){
        mReceiver = new SecondsToLockReceiver();
        LocalBroadcastManager.getInstance(MyApplication.getAppContext())
                .registerReceiver(mReceiver,
                        new IntentFilter(ApplicationPreferences.SECONDS_TO_LOCK));
    }

    private static class SecondsToLockReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if( intent.getAction().equals(ApplicationPreferences.SECONDS_TO_LOCK)){
                LockPasswordTask.getInstance().updateTimer();
                Log.e("SecondsToLockReceiver", " Handling " + ApplicationPreferences.SECONDS_TO_LOCK);
            }
        }
    }
}
