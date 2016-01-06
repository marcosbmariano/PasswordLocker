package com.mark.passwordlocker.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.mark.passwordlocker.R;
import com.mark.passwordlocker.application.MyApplication;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by mark on 3/18/15.
 */
public class ApplicationPreferences implements SharedPreferences.OnSharedPreferenceChangeListener{


    public static final String SECONDS_TO_LOCK = "com.mark.passwordlocker.helpers.SECONDS_TO_LOCK";

    private static final String TIME_TO_LOCK_KEY = "SECONDS_TO_LOCK";
    private static Context mContext;
    private static PreferencesNotificationDisplayListener mDisplayNotificationListener;


    private ApplicationPreferences(Context context){
        mContext = context;
        setPreferencesDefaultValues();
    }


    public static ApplicationPreferences getInstance(){
        return Holder.sInstance;
    }

    private static class Holder{
        private static ApplicationPreferences sInstance =
                new ApplicationPreferences(MyApplication.getAppContext());
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
        return getSharedPreferences().getBoolean(key, false);
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
        final String SHORT_CUT_NOTIFICATION_KEY = "SHOW_NOTIFICATION";

        switch (key){
            case TIME_TO_LOCK_KEY:
                broadcastSecondsToLock();
                break;

            case SHORT_CUT_NOTIFICATION_KEY:
                updateNotificationDisplayListener();
                break;

            default:
        }
    }

    private void broadcastSecondsToLock(){ //TODO fix this
        Intent localIntent = new Intent();
        localIntent.setAction(SECONDS_TO_LOCK);
        //localIntent.putExtra(SECONDS_TO_LOCK, seconds);
        //localIntent.addCategory(Intent.CATEGORY_DEFAULT); TODO why category DEFAULT
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(localIntent);
    }

    public static void addPreferencesNotificationDisplayListener(
            PreferencesNotificationDisplayListener listener){
        mDisplayNotificationListener = listener;
    }

    private void updateNotificationDisplayListener(){
        mDisplayNotificationListener.updateIsToShowNotification(isNotificationDisplayable());
    }

    public interface PreferencesNotificationDisplayListener{
        void updateIsToShowNotification(boolean showNotification);
    }

}
