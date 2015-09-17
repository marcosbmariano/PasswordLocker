package com.example.mark.passwordlocker.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mark.passwordlocker.broadcastReceiver.ScreenLockBroadReceiver;
import com.example.mark.passwordlocker.helpers.ApplicationState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 7/20/15.
 */
public class MyService extends Service implements ApplicationState.ApplicationStateObserver {
    private BroadcastReceiver mReceiver;
    private final IBinder mBinder = new MyBinder();
    private static List<ServiceCallBack> mServiceObserver;

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
        ApplicationState.addObserver(this);
        mServiceObserver = new ArrayList<>();
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
        ApplicationState.deleteObserver(this);
        Log.e("MyReceiver", "ON Destroy");
        warnActivityOfDestruction();
        super.onDestroy();
    }

    private void warnActivityOfDestruction(){
        for ( ServiceCallBack listener : mServiceObserver){
            if ( listener.isActivityVisible()){
                listener.serviceDestroyed();
            }
        }
    }

    @Override
    public void applicationIsLocked() {
        updateObservers();
    }

    @Override
    public void applicationIsUnlocked() {
        updateObservers();
    }

    private void updateObservers(){
        for ( ServiceCallBack listener : mServiceObserver){
            if ( listener.isActivityVisible()){
                listener.updateActivity();
            }
        }
    }

    public class MyBinder extends Binder {
         public MyService getService(){
            return MyService.this;
        }
    }

    public interface ServiceCallBack{
        void updateActivity();
        boolean isActivityVisible();
        void serviceDestroyed();
    }
}
