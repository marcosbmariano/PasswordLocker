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
import com.example.mark.passwordlocker.helpers.ApplicationPassword;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 7/20/15.
 */
public class MyService extends Service implements ApplicationPassword.ApplicationPasswordListener {
    private BroadcastReceiver mReceiver;
    private ApplicationPassword mAppPassword;
    private final IBinder mBinder = new MyBinder();
    private static List<ServiceCallBack> mServiceListener;
    private static ApplicationPreferences mAppPreferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_ANSWER);
        mReceiver = new ScreenLockBroadReceiver();
        registerReceiver(mReceiver, filter);
        mAppPassword = ApplicationPassword.getInstance();
        mAppPassword.addListener(this);

        Log.e("OnCreate", "Service is created!!");
        //http://stackoverflow.com/questions/19899438/how-to-get-broadcast-for-screen-lock-in-android
    }

    public static void addListener(ServiceCallBack listener){
        if (null == mServiceListener){
            mServiceListener = new ArrayList<>();
        }
        mServiceListener.add(listener);
        Log.e("MyService", "a listener was added!!");
    }



    @Override
    public void onDestroy() {
        Log.e("OnDestroy", "it is destroyed!!");
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }


    @Override
    public void passwordIsLocked() {
        Log.e("MyService", "passwordIsLocked()!!!!!!");
        for ( ServiceCallBack listener : mServiceListener){
            if ( listener.isActivityVisible()){
                Log.e("MyService", "call to update !");
                listener.updateActivity();
            }
        }
    }

    @Override
    public void passwordIsUnlocked() {
        Log.e("MyService", "PasswordIsUnlocked()!!!!!!");

            for ( ServiceCallBack listener : mServiceListener){
                if ( listener.isActivityVisible()){
                    Log.e("MyService", "call to update !");
                    listener.updateActivity();
                }
            }

    }

    private ApplicationPreferences getApplicationPreferences(){
        if ( null == mAppPreferences){
            mAppPreferences = ApplicationPreferences.getInstance();
        }
        return mAppPreferences;
    }


    public class MyBinder extends Binder {
         public MyService getService(){
            return MyService.this;
        }
    }

    public interface ServiceCallBack{
        void updateActivity();
        boolean isActivityVisible();
    }
}
