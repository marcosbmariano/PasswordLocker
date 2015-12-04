package com.mark.passwordlocker.helpers;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mark on 7/23/15.
 *
 * this class is responsible to keep track of the application state ( locked / unlocked) and
 * warn its observers of any state change
 *
 */
public class ApplicationState implements ApplicationPreferences.PreferencesSecondsToLockObserver,
        Counter.CounterCallBack{
    private static ApplicationPassword mApplicationPassword;
    private static ApplicationPreferences mPreferences;
    private static ApplicationState mInstance;
    private static Set<ApplicationStateObserver> mObservers = new HashSet<>();
    private Counter mCounter;
    private boolean mIsLockSuspended = false;


    private ApplicationState(){
        mApplicationPassword = ApplicationPassword.getInstance();
        mPreferences = ApplicationPreferences.getInstance();
        mPreferences.addPreferencesAppLockObserver(this);
    }

    public static ApplicationState getInstance(){
        if ( null == mInstance ){
            mInstance = new ApplicationState();
        }
        return mInstance;
    }

    //this method is called by the ApplicationPreferences class when there is a change in
    //the seconds to lock the application
    @Override
    public void updateSeconds(int seconds) { //todo check if the parameter can be removed
        updateCounter();
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
            updateObservers();
        }
    }

    public boolean isPasswordValid(String password){
        boolean result = mApplicationPassword.isPasswordValid(password);
        setCounterToLockApplication();
        updateObservers();
        return result;
    }

    public void unlockApplication( ){
        if ( mApplicationPassword.isKeyValid() )  {
            Log.e("unlockApplication", "Key is valid");
            mApplicationPassword.unlockPassword();
            setCounterToLockApplication();
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

    public boolean isApplicationLocked(){
        return mApplicationPassword.isPasswordLocked();
    }

    public static void addObserver(ApplicationStateObserver observer){
            mObservers.add(observer);
    }

    public static void deleteObserver(ApplicationStateObserver observer){
        if (null != mObservers ){
            mObservers.remove(observer);
        }
    }

    private ApplicationPreferences getAppPreferences(){
        if (null == mPreferences){
            mPreferences = ApplicationPreferences.getInstance();
        }
        return mPreferences;
    }

    public interface ApplicationStateObserver {
        void applicationIsLocked();
        void applicationIsUnlocked();
    }
}
