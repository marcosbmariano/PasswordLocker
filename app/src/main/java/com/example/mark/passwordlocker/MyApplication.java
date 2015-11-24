package com.example.mark.passwordlocker;

import android.app.Application;
import com.example.mark.passwordlocker.account.AccountRecord;
import com.example.mark.passwordlocker.helpers.ApplicationPassword;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;
import com.example.mark.passwordlocker.helpers.DatabaseKey;
import com.marcos.autodatabases.utils.DatabaseHelper;

/**
 * Created by mark on 11/23/15.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setupSingletons();
        setupDatabase();
    }

    private void setupSingletons(){
        DatabaseKey.setContext(this);
        ApplicationPassword.setContext(this);
        ApplicationPreferences.setContext(this);
        DatabaseHelper.setupContext(this);
    }

    private void setupDatabase(){
        DatabaseHelper mDataHelper; //TODO check if those methods should not be static
        mDataHelper = DatabaseHelper.getInstance();
        mDataHelper.addModel(AccountRecord.class);
        mDataHelper.createDatabase();
    }
}
