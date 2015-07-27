package com.example.mark.passwordlocker.helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 7/23/15.
 */
public class ApplicationState implements ApplicationPreferences.PreferencesSecondsToLockListener{
    private static int CODE_TO_LOCK = -1;
    private static ApplicationPassword mApplicationPassword;
    private static ApplicationPreferences mPreferences;
    private static boolean mIsAppLocked;
    private static byte [] mKey;
    private static List<ApplicationStateObserver> mObservers;
    private int mSecondsToLock = 0;
    private Counter mCounter;





    public void addListener(ApplicationStateObserver listener){
        if ( null == mObservers){
            mObservers = new ArrayList<>();
        }
        mObservers.add(listener);
    }


    private void updateListeners(){
        Log.e("Application Password", "updating Listeners!!");
        if (null != mObservers){
            for ( ApplicationStateObserver observer : mObservers){
                if ( isApplicationLocked()){
                    observer.applicationIsLocked();
                    Log.e("Application Password", "passwordIsLocked!!");
                }else{
                    observer.applicationIsUnlocked();
                    Log.e("Application Password", "passwordIsUnlocked!!");
                }
            }
        }
    }


    private ApplicationPreferences getAppPreferences(){
        if (null == mPreferences){
            mPreferences = ApplicationPreferences.getInstance();
            mPreferences.addPreferencesAppLockChangeListener(this);
        }
        return mPreferences;
    }

    private ApplicationPassword getApplicationPassword(){ //TODO should I keep?
        if ( null == mApplicationPassword){
            mApplicationPassword = ApplicationPassword.getInstance();
        }
        return mApplicationPassword;
    }

    private int getSecondsToLockApp(){
        if ( mSecondsToLock == 0){
            mSecondsToLock = getAppPreferences().getSecondsToLockApplication();
        }
        return mSecondsToLock;
    }

    @Override
    public void updateSeconds(int seconds) {
        mSecondsToLock = seconds;
        updateCounter();
    }


    private void updateCounter(){
        if( isToLockPassword()){
            setCounterToLockeApplication();
        }
    }

    private boolean isToLockPassword(){
        return (getSecondsToLockApp() >= CODE_TO_LOCK);
    }


    @Override
    public void calledByCounter(int seconds ) {
        if ( isToLockPassword() && getSecondsToLockApp() == seconds ){
            lockPassword();

        }
    }

    public void lockPassword(){
        mIsAppLocked = true;
        mApplicationPassword.lockPassword();

        updateListeners();
    }



    public void unlockPassword( ){
        if ( null != mKey )  {
            mIsAppLocked = false;
            setCounterToLockeApplication();
            updateListeners();
            Log.e("Application Password", "Unlock Password!! from unlocked");
        }

    }





    private void setCounterToLockeApplication(){
        int seconds = getSecondsToLockApp();
        if ( seconds >= 30 ){
            mCounter = new Counter(this);
            mCounter.startCounter(seconds);
        }
    }


    public boolean isApplicationLocked(){
        return mIsAppLocked;
    }



    public interface ApplicationStateObserver {
        void applicationIsLocked();

        void applicationIsUnlocked();

    }
}
