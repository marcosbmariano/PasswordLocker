package com.mark.passwordlocker.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.mark.passwordlocker.application.MyApplication;

/**
 * Created by mark on 7/23/15.
 *
 * this class is responsible to keep track of the application state ( locked / unlocked) and
 * warn its observers of any state change
 *
 */
public class ApplicationState implements /* ApplicationPreferences.PreferencesSecondsToLockObserver,*/
        Counter.CounterCallBack{


    public static final String APPLICATION_IS_LOCKED =
            "com.mark.passwordlocker.helpers.ApplicationState.APPLICATION_IS_LOCKED";
    public static final String APPLICATION_IS_UNLOCKED =
            "com.mark.passwordlocker.helpers.ApplicationState.APPLICATION_IS_UNLOCKED";

    private static ApplicationPassword mApplicationPassword;
    private static ApplicationPreferences mPreferences;

    //private static Set<ApplicationStateObserver> mObservers = new HashSet<>();
    private Counter mCounter;
    private boolean mIsLockSuspended = false;
    private MyBroadcastReceiver mReceiver;
    private static ApplicationState sInstance = new ApplicationState();

    public static ApplicationState getInstance(){
        return sInstance;
    }

    private ApplicationState(){
        mApplicationPassword = ApplicationPassword.getInstance();
        mPreferences = ApplicationPreferences.getInstance();
        setupBroadcastReceiver();
    }

    private void setupBroadcastReceiver(){
        mReceiver = new MyBroadcastReceiver();
        LocalBroadcastManager.getInstance(MyApplication.getAppContext())
                .registerReceiver(mReceiver,
                        new IntentFilter(ApplicationPreferences.UPDATE_SECONDS_TO_LOCK));
    }

    private void unregisterBroadcastReceiver(){
        if( null != mReceiver){
            LocalBroadcastManager.getInstance(MyApplication.getAppContext())
                    .unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private void updateCounter(){ //remove?
        setCounterToLockApplication();
    }

    private int getSecondsToLockApp(){
        return getAppPreferences().getSecondsToLockApplication();
    }

    private void setCounterToLockApplication(){
        if ( getSecondsToLockApp() >= 30 ){

            if ( null != mCounter){
                mCounter.deactivateCounter();
            }
            mCounter = new Counter(this, getSecondsToLockApp());
            mCounter.startCounter();
        }
    }

    boolean isToLockApplication(){
        return getSecondsToLockApp() > -2 && !mIsLockSuspended;
    }

    boolean secondsToLockMatchCounterSeconds(){
        return getSecondsToLockApp() == getCounterSeconds();
    }

    int getCounterSeconds(){
        if( null == mCounter ){
            return Integer.MIN_VALUE;
        }
        return mCounter.getSeconds();
    }

    //this is called by the counter when the define time is expired
    @Override
    public void calledByCounter(Counter counter ) {
         lockApplication();
    }

    public void suspendLock(){
        mIsLockSuspended = true;
    }

    public void resumeLock(){
        mIsLockSuspended = false;
        setCounterToLockApplication();
    }

    public void lockApplication(){
        if ( isToLockApplication() && secondsToLockMatchCounterSeconds() ) {
            mApplicationPassword.lockPassword();
            broadcastAppIsLocked(true);
        }
    }

    public boolean isPasswordValid(String password){ //TODO fix this, this should be static and not related if the app will lock or unlock
        boolean result = mApplicationPassword.isPasswordValid(password);
        setCounterToLockApplication();
        //updateObservers();
        broadcastAppIsLocked(!result);
        return result;
    }

    public void unlockApplication( ){
        if ( mApplicationPassword.isKeyValid() )  {
            Log.e("unlockApplication", "Key is valid");
            mApplicationPassword.unlockApplication();
            setCounterToLockApplication();
            broadcastAppIsLocked(false);
        }
    }
    //this method sends a local broadcast, true if the app is locked
    //and false if the app is unlocked
    private void broadcastAppIsLocked(boolean isLocked){
        Intent intent = new Intent();
        if( isLocked){
            intent.setAction(APPLICATION_IS_LOCKED);
        }else{
            intent.setAction(APPLICATION_IS_UNLOCKED);
        }
        LocalBroadcastManager.getInstance(MyApplication.getAppContext())
                .sendBroadcast(intent);
    }


    public boolean isApplicationLocked(){
        return mApplicationPassword.isPasswordLocked();
    }


    private ApplicationPreferences getAppPreferences(){
        if (null == mPreferences){
            mPreferences = ApplicationPreferences.getInstance();
        }
        return mPreferences;
    }

    private class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ApplicationPreferences.UPDATE_SECONDS_TO_LOCK) ){
                Log.e("Application state", "MyBrodcastReceiver");
                updateCounter();
            }
        }
    }

}
