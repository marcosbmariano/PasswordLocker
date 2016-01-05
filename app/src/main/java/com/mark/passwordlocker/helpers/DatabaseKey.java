package com.mark.passwordlocker.helpers;

import android.content.Context;

import com.mark.passwordlocker.application.MyApplication;


/**
 * Created by mark on 3/12/15.
 */
public final class DatabaseKey extends SharedPrefsActor {
    private final String PREFERENCES_NAME = "database_pref";
    private static Context mContext;
    private static ApplicationPassword mApplicationPassword;
    private static ApplicationState mApplicationState;


    private DatabaseKey(Context context){
            mContext = context;
            mApplicationPassword = ApplicationPassword.getInstance();
            mApplicationState = ApplicationState.getInstance();
    }

    public static DatabaseKey getInstance(){
        return Holder.sInstance;
    }

    private static class Holder{
        private static DatabaseKey sInstance = new DatabaseKey(MyApplication.getAppContext());
    }


    boolean isApplicationLocked(){
        return mApplicationState.isApplicationLocked();
    }

    public byte [] getKey(){
        if ( isApplicationLocked() ){
            throw new IllegalStateException("The key cannot be used if the Application is locked!!");
        }
        return mApplicationPassword.getAppKey();
    }

    public byte [] getIv(){
        if ( mApplicationState.isApplicationLocked() ){
            throw new IllegalStateException("The Iv cannot be used if the Application is locked!!");
        }
        return mApplicationPassword.getAppIv();

    }

    public byte [] getSalt(){
        if ( mApplicationState.isApplicationLocked() ){
            throw new IllegalStateException("The Salt cannot be used if the Application is locked!!");
        }
        return mApplicationPassword.getAppKey();
    }

    @Override
    protected Context getContext() {
        return mContext;
    }

    @Override
    protected String getPreferencesName() {
        return PREFERENCES_NAME;
    }

}
