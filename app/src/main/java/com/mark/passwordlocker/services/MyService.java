package com.mark.passwordlocker.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mark.passwordlocker.broadcastReceiver.ScreenLockBroadReceiver;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mark on 7/20/15.
 */
public class MyService extends Service {
    private BroadcastReceiver mReceiver;
    private final IBinder mBinder = new MyBinder();
    private static Set<ServiceCallBack> mServiceObserver = new HashSet<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new ScreenLockBroadReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_ANSWER);
        registerReceiver(mReceiver, filter);
        Log.e("MyService", "Service is created");
    }

    public static void addObserver(ServiceCallBack observer){
        if ( !mServiceObserver.contains(observer)){
            mServiceObserver.add(observer);
        }
    }

    public static void deleteObsever(ServiceCallBack observer){
        if ( null != mServiceObserver && mServiceObserver.contains(observer)){
            mServiceObserver.remove(observer);
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        Log.e("MyReceiver", "ON Destroy");
        warnActivityOfDestruction();
        super.onDestroy();
    }

    private void warnActivityOfDestruction(){
        for ( ServiceCallBack listener : mServiceObserver){
                listener.serviceDestroyed();
        }
    }

    public class MyBinder extends Binder {
         public MyService getService(){
            return MyService.this;
        }
    }

    public interface ServiceCallBack{
        void serviceDestroyed();
    }
}
