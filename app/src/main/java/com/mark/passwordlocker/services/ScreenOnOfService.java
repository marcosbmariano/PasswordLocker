package com.mark.passwordlocker.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mark.passwordlocker.broadcastReceiver.ScreenLockBroadReceiver;

/**
 * Created by mark on 1/4/16.
 * this service is bounded tho the second activity and is used
 * to implement a broadcastReceiver that tracks if the screen is on or off
 * when the second activity is destroyed this service is killed
 */
public class ScreenOnOfService extends Service {
    private BroadcastReceiver mReceiver;
    private final IBinder mBinder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerScreenStateReciver();
    }

    private void registerScreenStateReciver(){
        mReceiver = new ScreenLockBroadReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_ANSWER);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        unregisterScreenStateRevicer();
        super.onDestroy();
    }

    private void unregisterScreenStateRevicer(){
        if( null != mReceiver){
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    public class MyBinder extends Binder {
       public ScreenOnOfService getService(){
            return ScreenOnOfService.this;
        }
    }


}
