package com.example.mark.passwordlocker.helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 7/23/15.
 */
public class ApplicationState implements ApplicationPreferences.PreferencesSecondsToLockObserver,
        Counter.CounterCallBack{
    private static int CODE_TO_LOCK = -1;
    private static ApplicationPassword mApplicationPassword;
    private static ApplicationPreferences mPreferences;
    private static ApplicationState mInstance;
    private static List<ApplicationStateObserver> mObservers;
    private int mSecondsToLock = 0;
    private Counter mCounter;
    private boolean mIsLockSuspended = false;


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

    private int getSecondsToLockApp(){
        if ( mSecondsToLock == 0){
            mSecondsToLock = getAppPreferences().getSecondsToLockApplication();
        }
        return mSecondsToLock;
    }

    //this method is called by the ApplicationPreferences class when there is a change in
    // the seconds to lock the application
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

    private void setCounterToLockeApplication(){
        int seconds = getSecondsToLockApp();
        if ( seconds >= 30 ){
            mCounter = new Counter(this, seconds);
            mCounter.startCounter();
        }
    }

    private boolean isToLockApplication(){
        Log.e("isToLockApplication", "" + ((getSecondsToLockApp() >= CODE_TO_LOCK) && !mIsLockSuspended));
        return (getSecondsToLockApp() >= CODE_TO_LOCK) && !mIsLockSuspended;
    }

    //this is called by the counter when the define time is expired
    @Override
    public void calledByCounter(Counter counter ) {
        Log.e("CalledByCounter", "CalledByCounter");
        if ( isToLockApplication() && getSecondsToLockApp() == counter.getSeconds() ){
            lockApplication();
            Log.e("CalledByCounter", "lockApplication");
        }
    }

    public void suspendLock(){
        mIsLockSuspended = true;
    }

    public void resumeLock(){
        mIsLockSuspended = false;
        setCounterToLockeApplication();
    }

    public void lockApplication(){
        mApplicationPassword.lockPassword();
        Log.e("lockApplication", "vai tomar no cu");
        updateObservers();
    }

    public void unlockApplication( ){
        if ( mApplicationPassword.isKeyValid() )  {
            mApplicationPassword.unlockPassword();
            setCounterToLockeApplication();
            updateObservers();
        }
    }

    private void updateObservers(){

        if (null != mObservers){
            for ( ApplicationStateObserver observer : mObservers){

                if ( isApplicationLocked()){
                    observer.applicationIsLocked();
                }else{
                    observer.applicationIsUnlocked();
                }
            }
        }
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



    public boolean isApplicationLocked(){
        return mApplicationPassword.isPasswordLocked();
    }


    public interface ApplicationStateObserver {
        void applicationIsLocked();
        void applicationIsUnlocked();
    }
}
