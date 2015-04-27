package com.example.mark.passwordlocker.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mark.passwordmanager.PasswordUtils;
import com.example.mark.passwordmanager.cipher.PasswordCipher;

/**
 * Created by mark on 3/12/15.
 */
public abstract class SharedPrefsActor { // TODO ensure that a called to the context is safe

    abstract protected Context getContext();
    abstract protected String getPreferencesName();


    protected void saveDataToSharedPref(String dataName, String data){
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putString(dataName, data);
        editor.commit();
    }

    protected boolean hasDataOnSharedPref(String dataName){
        SharedPreferences pref = getSharedPref();
        return pref.contains(dataName);
    }

    protected String getStringFromPreferences(String prefDataName){

        SharedPreferences pref = getSharedPref();
        String result =  pref.getString(prefDataName, null);
        if (null == result){
            throw new NullPointerException("Data not found on preferences");
        }
        return result;
    }

    protected SharedPreferences getSharedPref(){
        return getContext().getSharedPreferences(getPreferencesName(),Context.MODE_PRIVATE);
    }

}
