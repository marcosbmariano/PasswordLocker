package com.example.mark.passwordlocker.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by mark on 3/12/15.
 */
abstract class SharedPrefsActor { // TODO ensure that a called to the context is safe, check this class
    //TODO it seems weird

    abstract protected Context getContext();
    abstract protected String getPreferencesName();


    protected void saveDataToSharedPref(String dataName, String data){
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putString(dataName, data);
        editor.commit();
    }

//    protected void deleteDataFromSharedPref(String key){
//        SharedPreferences.Editor editor = getSharedPref().edit();
//        editor.remove(key);
//        editor.commit();
//    }

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
