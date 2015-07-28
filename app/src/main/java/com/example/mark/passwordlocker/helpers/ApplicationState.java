package com.example.mark.passwordlocker.helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 7/23/15.
 */
public class ApplicationState implements ApplicationPreferences.PreferencesSecondsToLockListener,
        Counter.CounterCallBack{
    private static int CODE_TO_LOCK = -1;
    private static ApplicationPassword mApplicationPassword;
    private static ApplicationPreferences mPreferences;
    private static ApplicationState mInstance;
    private static List<ApplicationStateObserver> mObservers;
    private int mSecondsToLock = 0;
    private Counter mCounter;


    private ApplicationState(){
        mApplicationPassword = ApplicationPassword.getInstance();
        mPreferences = ApplicationPreferences.getInstance();
        mPreferences.addPreferencesAppLockChangeListener(this);
        mObservers = new ArrayList<>();
    }

    public static ApplicationState getInstance(){
        if ( null == mInstance ){
            mInstance = new ApplicationState();
        }
        return mInstance;
    }

    public static void addObserver(ApplicationStateObserver observer){
        if ( !mObservers.contains(observer)){
            mObservers.add(observer);
        }
    }

    public static void deleteObserver(ApplicationStateObserver observer){
        if (null != mObservers && mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }

    private ApplicationPreferences getAppPreferences(){
        if (null == mPreferences){
            mPreferences = ApplicationPreferences.getInstance();
        }
        return mPreferences;
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
        if( isToLockApplication()){
            setCounterToLockeApplication();
        }
    }

    private boolean isToLockApplication(){
        return (getSecondsToLockApp() >= CODE_TO_LOCK);
    }


    @Override
    public void calledByCounter(int seconds ) {
        if ( isToLockApplication() && getSecondsToLockApp() == seconds ){
            lockApplication();
        }
    }

    public void lockApplication(){
        mApplicationPassword.lockPassword();
        updateObservers();
    }

    public void unlockApplication( ){
        if ( mApplicationPassword.isKeyValid() )  {
            mApplicationPassword.unlockPassword();
            setCounterToLockeApplication();
            updateObservers();
            Log.e("ApplicationState", "Unlock Password!! from unlocked");
        }
    }

    private void updateObservers(){
        Log.e("ApplicationState", "updating Listeners!!");
        if (null != mObservers){
            for ( ApplicationStateObserver observer : mObservers){
                if ( isApplicationLocked()){
                    Log.e("ApplicationState", "passwordIsLocked!!");
                    observer.applicationIsLocked();
                }else{
                    Log.e("ApplicationState", "passwordIsUnlocked!!");
                    observer.applicationIsUnlocked();
                }
            }
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
        return mApplicationPassword.isPasswordLocked();
    }


    public interface ApplicationStateObserver {
        void applicationIsLocked();

        void applicationIsUnlocked();

    }
}
