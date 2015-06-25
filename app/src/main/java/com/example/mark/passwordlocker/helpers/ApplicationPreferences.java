package com.example.mark.passwordlocker.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mark.passwordlocker.R;

/**
 * Created by mark on 3/18/15.
 */
public class ApplicationPreferences {
    private static Context mContext;
    private static ApplicationPreferences mIntance;
    private static SharedPreferences mApllicationsPreferences;

    private ApplicationPreferences(){
        if (null == mContext){
            throw new NullPointerException(
                    "ApplicationPreferences must have a valid context reference, use setContext.");
        }
        setPreferencesDefaultValues();
        setupPreferences();
    }

    private void setupPreferences(){
        mApllicationsPreferences = getSharedPreferences();
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
        String key = mContext.getResources().getString(R.string.pref_password_length);
        return Integer.valueOf(getSharedPreferences().getString(key,"0"));
    }



}
