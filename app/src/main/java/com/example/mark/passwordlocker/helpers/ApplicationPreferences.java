package com.example.mark.passwordlocker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.mark.passwordlocker.R;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by mark on 3/18/15.
 */
public class ApplicationPreferences implements SharedPreferences.OnSharedPreferenceChangeListener{


    private final String TIME_TO_LOCK_KEY = "SECONDS_TO_LOCK";
    private final String SHORT_CUT_NOTIFICATION_KEY = "SHOW_NOTIFICATION";
    private static Context mContext;
    private static ApplicationPreferences mIntance;
    private static SharedPreferences mApplicationsPreferences;
    private Set<PreferencesSecondsToLockObserver> mLockObservers;
    private PreferencesNotificationDisplayListener mDisplayNotificationListener;


    private ApplicationPreferences(){
        if (null == mContext){
            throw new NullPointerException(
                    "ApplicationPreferences must have a valid context reference, use setContext.");
        }
        setPreferencesDefaultValues();
        setupPreferences();
    }

    private void setupPreferences(){
        mApplicationsPreferences = getSharedPreferences();
    }

    public static ApplicationPreferences getInstance(){
        if ( null == mIntance){
            mIntance = new ApplicationPreferences();
        }
        return mIntance;
    }

    public static void setContext(Context context){
        mContext = context;
    }

    private SharedPreferences getSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    private void setPreferencesDefaultValues(){
        PreferenceManager.setDefaultValues(mContext, R.xml.preferences,false);
    }

    public int getGeneratedPasswordLength(){
        String key = getResourceString(R.string.pref_password_length_key);
        return Integer.valueOf(getSharedPreferences().getString(key,"0"));
    }

    public boolean isNotificationDisplayable(){
        String key = getResourceString(R.string.pref_show_notification_key);
        return Boolean.valueOf(getSharedPreferences().getBoolean(key, false));
    }

    public int getClipBoardSeconds(){
        String key = mContext.getResources().
                getString(R.string.preferences_clipboard_seconds_key);
        return Integer.valueOf(getSharedPreferences().getString(key, "60"));
    }

    public int getSecondsToLockApplication(){
        return Integer.valueOf(getSharedPreferences().getString(TIME_TO_LOCK_KEY, "60"));
    }

    public int getSecondsToHidePassword(){
        String key = getResourceString(R.string.pref_time_to_hide_Key);
        return Integer.valueOf(getSharedPreferences().getString(key, "60"));
    }

    private String getResourceString( int rStringId){
        return mContext.getResources().
                getString(rStringId);
    }

    public boolean isToUnlockApplicationOnScreenOn(){
        return getSecondsToLockApplication() == -2;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        switch (key){
            case TIME_TO_LOCK_KEY:
                updateLockPreferencesListener();
                break;

            case SHORT_CUT_NOTIFICATION_KEY:
                updateNotificationDisplayListener();
                break;

            default:
        }
    }

    public void addPreferencesAppLockObserver(
            PreferencesSecondsToLockObserver observer){
        if(null == mLockObservers){
            mLockObservers = new HashSet<>();
        }
        mLockObservers.add(observer);

    }
    private void updateLockPreferencesListener(){
        if ( null == mLockObservers){
            throw new IllegalStateException("PreferencesSecondsToLockObserver is null");
        }
        Iterator<PreferencesSecondsToLockObserver> observers = mLockObservers.iterator();
        while( observers.hasNext()){
            observers.next().updateSeconds(getSecondsToLockApplication());
        }

    }

    public void addPreferencesNotificationDisplayListener(
            PreferencesNotificationDisplayListener listener){
        mDisplayNotificationListener = listener;
    }

    public void updateNotificationDisplayListener(){
        mDisplayNotificationListener.updateIsToShowNotification(isNotificationDisplayable());
    }

    public interface PreferencesNotificationDisplayListener{
        void updateIsToShowNotification(boolean showNotification);
    }

    public interface PreferencesSecondsToLockObserver {
        void updateSeconds(int seconds);
    }
}
