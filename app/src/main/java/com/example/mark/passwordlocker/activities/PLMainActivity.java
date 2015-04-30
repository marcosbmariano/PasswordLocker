package com.example.mark.passwordlocker.activities;



import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;

import com.example.mark.passwordlocker.AccountRecord;
import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.alerts.NewAccountDialog;
import com.example.mark.passwordlocker.fragments.NewUserPassFrag;
import com.example.mark.passwordlocker.fragments.AppPassEnterFrag;
import com.example.mark.passwordlocker.helpers.ApplicationPassword;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;
import com.marcos.autodatabases.utils.DatabaseHelper;


public class PLMainActivity extends ActionBarActivity  { //TODO reviewed!!!

    private DatabaseHelper mDataHelper = null;
    private ApplicationPassword mApplicationPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plmain);

        setupSingletons();

        setFirstFragment();

        setupDatabase();

    }

    private void setupSingletons(){
        ApplicationPassword.setContext(this);
        ApplicationPreferences.setContext(this);
        DatabaseHelper.setupContext(this);
    }

    private void setupDatabase(){
        mDataHelper = DatabaseHelper.getInstance();
        mDataHelper.addModel(AccountRecord.class);
        mDataHelper.createDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //every time the app resume, ask the user for the password to open the app again
        setFirstFragment();
    }

    private void setFirstFragment(){

        if (isApplicationPasswordDefined()){
            swapFragment(R.id.MainFragContainer, new AppPassEnterFrag());

        }else{
            hideActionBar();
            swapFragment(R.id.MainFragContainer, new NewUserPassFrag());
        }
    }


    private boolean isApplicationPasswordDefined(){
        if ( null == mApplicationPassword){
            mApplicationPassword = ApplicationPassword.getInstance();
        }
        return mApplicationPassword.isPasswordDefined();
    }

    private void swapFragment(int container, Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(container, fragment,fragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plmain, menu);
        return true;
    }

    private void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if ( actionBar != null){
                actionBar.hide();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.action_add_account:
                new NewAccountDialog().show(getSupportFragmentManager(), null);

                return true;
            case R.id.action_settings:
                Intent intent  = new Intent(this, PreferencesActivity.class);
                startActivity(intent);

                return true;
        }
       return super.onOptionsItemSelected(item);
    }


}
