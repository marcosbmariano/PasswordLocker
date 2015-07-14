package com.example.mark.passwordlocker.helpers;

import android.content.Context;

import com.example.mark.passwordmanager.PasswordUtils;
import com.example.mark.passwordmanager.cipher.PasswordCipher;

/**
 * Created by mark on 3/12/15.
 */
public final class DatabaseKey extends SharedPrefsActor {
    private final String PREFERENCES_NAME = "database_pref";
    private static Context mContext;
    private static ApplicationPassword mApplicationPassword;
    private static DatabaseKey mDatabaseKeyInstance;


    public DatabaseKey(){
        if ( null == mContext){
            throw new NullPointerException(
                    "DatabaseKey must have a valid Context reference before being used!");
        }else{
            mApplicationPassword = ApplicationPassword.getInstance();
        }
    }

    public static void setContext( Context context){
        mContext = context;
    }

    public static DatabaseKey getInstance(){
        if ( null == mDatabaseKeyInstance){
            mDatabaseKeyInstance = new DatabaseKey();
        }
        return mDatabaseKeyInstance;
    }


    private boolean isApplicationLocked(){
        return mApplicationPassword.isApplicationLocked();
    }

    public byte [] getKey(){
        if ( isApplicationLocked() ){
            throw new IllegalStateException("The key cannot be used if the Application is locked!!");
        }
        return mApplicationPassword.getAppKey();
    }

    public byte [] getIv(){
        if ( mApplicationPassword.isApplicationLocked() ){
            throw new IllegalStateException("The Iv cannot be used if the Application is locked!!");
        }
        return mApplicationPassword.getAppIv();

    }

    public byte [] getSalt(){
        if ( mApplicationPassword.isApplicationLocked() ){
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
