package com.mark.passwordlocker.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.mark.passwordlocker.account.AccountRecord;
import com.mark.passwordlocker.broadcastReceiver.ScreenLockBroadReceiver;

import com.marcos.autodatabases.utils.DatabaseHelper;

/**
 * Created by mark on 11/23/15.
 */
public class MyApplication extends Application {
    private static MyApplication sIntance;

    @Override
    public void onCreate() {
        super.onCreate();
        sIntance = this;
        setupSingletons();
        setupDatabase();

    }


    private void setupSingletons(){
        DatabaseHelper.setupContext(this);
    }

    private void setupDatabase(){

        DatabaseHelper mDataHelper; //TODO check if those methods should not be static
        mDataHelper = DatabaseHelper.getInstance();
        mDataHelper.addModel(AccountRecord.class);
        mDataHelper.createDatabase();
    }

    public static Context getAppContext(){
        return sIntance.getApplicationContext();
    }

}
