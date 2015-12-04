package com.mark.passwordlocker.activities;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mark.passwordlocker.R;
import com.mark.passwordlocker.adapters.AccountsAdapter;
import com.mark.passwordlocker.fragments.NewAccountFragment;
import com.mark.passwordlocker.helpers.ApplicationState;
import com.mark.passwordlocker.helpers.TransitionSingleton;
import com.mark.passwordlocker.services.MyService;

public class SecondActivity extends AppCompatActivity implements AccountsAdapter.AccountsAdapterUpdate,
        MyService.ServiceCallBack, ApplicationState.ApplicationStateObserver{

    private Service mService; //TODO check this
    private ServiceConnection mServiceConnection;
    private TransitionSingleton mTransitionHelper;
    private FloatingActionButton mFloatingButton;
    private Toolbar mToolbar;
    private boolean mIsServiceBound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        setupWidgets();
        setupTransitionHelper();
    }

    private void setupWidgets(){
        mToolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_lock);

        mFloatingButton = (FloatingActionButton) findViewById(R.id.fab);

    }

    public void onClickFab(View v){
        mTransitionHelper.toggleScene();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.add_account_layout, new NewAccountFragment())
                .commit();
    }

    private void setupTransitionHelper(){
        if ( null == mTransitionHelper){
            mTransitionHelper.setActivity(this);
            mTransitionHelper = TransitionSingleton.getInstance();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupWidgets();
        if(ApplicationState.getInstance().isApplicationLocked()){
            setFirstActivity();
        }else {
            ApplicationState.addObserver(this);
            MyService.addObserver(this);
            if( !mIsServiceBound){
                launchService();
            }
            mTransitionHelper.startScene();
        }
    }

    @Override
    protected void onPause() {
        ApplicationState.deleteObserver(this);
        MyService.deleteObsever(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        desconnectFromService();
        super.onDestroy();
    }

    private void desconnectFromService(){
        stopService(new Intent(this, MyService.class));
        if (mIsServiceBound){
            unbindService(mServiceConnection);
            mIsServiceBound = false;
        }
    }

    //this method is called from the accounts adapter when the
    //user decides to copy to the clipboard the account password
    @Override
    public void copyToClipboardPressed() {
        finish();
    }
    //this methods is callback from the application state singleton
    @Override
    public void applicationIsLocked() {
        startActivity(new Intent(this, PLMainActivity.class));
    }
    //this methods is callback from the application state singleton
    @Override
    public void applicationIsUnlocked() {
        //Do nothing
    }

    private void setFirstActivity(){
        startActivity(new Intent(this, PLMainActivity.class));
    }

    //this method is a callback from the Service
    //and is called in case of the service is
    //destroyed before time
    @Override
    public void serviceDestroyed() {
        mIsServiceBound = false;
        launchService();
    }

    private void launchService(){
        Intent i = new Intent(this, MyService.class);
        bindService(i, getServiceConnection(), Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection getServiceConnection(){
        if ( null == mServiceConnection){
            mServiceConnection = new ServiceConnection() {

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MyService.MyBinder binder = (MyService.MyBinder) service;
                    mService = binder.getService();
                    MyService.addObserver(SecondActivity.this);
                    mIsServiceBound = true;
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    MyService.deleteObsever(SecondActivity.this);
                    mIsServiceBound = false;
                }
            };
        }
        return mServiceConnection;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plmain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, MyPreferenceActivity.class));
                return true;
            case android.R.id.home:
                ApplicationState.deleteObserver(this);
                ApplicationState.getInstance().lockApplication();
                System.exit(1);

            default:
                return super.onOptionsItemSelected(item);
        }
    }





}
