package com.mark.passwordlocker.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mark.passwordlocker.R;
import com.mark.passwordlocker.adapters.AccountsAdapter;
import com.mark.passwordlocker.application.MyApplication;
import com.mark.passwordlocker.counter.LockPasswordTask;
import com.mark.passwordlocker.counter.TaskManager;
import com.mark.passwordlocker.fragments.NewAccountFragment;
import com.mark.passwordlocker.helpers.ApplicationPassword;
import com.mark.passwordlocker.helpers.TransitionSingleton;
import com.mark.passwordlocker.services.ScreenOnOfService;

import java.util.concurrent.locks.Lock;

public class SecondActivity extends AppCompatActivity implements AccountsAdapter.AccountsAdapterUpdate {


    private ServiceConnection mConnection;
    private boolean mBound;
    private TransitionSingleton mTransitionHelper;
    private FloatingActionButton mFloatingButton;
    private Toolbar mToolbar;
    private AppLockBrReceiver mAppLockBrReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        registerAppLockBrReceiver();
        setupWidgets();
        setupTransitionHelper();
        bindService();
    }

    private void bindService(){
        mConnection = getServiceConnection();
        Intent intent = new Intent(this, ScreenOnOfService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
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
        LockPasswordTask.getInstance().startTimer();
        if(ApplicationPassword.getInstance().isPasswordLocked()){
            gotoFirstActivity();
        }else {
            if( null == mAppLockBrReceiver){
                registerAppLockBrReceiver();
            }
            mTransitionHelper.startScene();
        }
    }

    @Override
    protected void onPause() {
        unregisterAppLockBrReceiver();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mBound){
            unbindService(mConnection);
        }
        super.onDestroy();
    }

    @Override
    public void copyToClipboardPressed() {
        finish();
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
                unregisterAppLockBrReceiver();
                ApplicationPassword.getInstance().lockPassword(); //TODO substitute this by password lock
                System.exit(1);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void gotoFirstActivity(){
        Intent intent = new Intent(this, PLMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void registerAppLockBrReceiver(){
        mAppLockBrReceiver = new AppLockBrReceiver();
        LocalBroadcastManager.getInstance(MyApplication.getAppContext())
                .registerReceiver(mAppLockBrReceiver,
                        new IntentFilter(ApplicationPassword.APPLICATION_IS_LOCKED));
    }

    private void unregisterAppLockBrReceiver(){
        if( null != mAppLockBrReceiver){
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mAppLockBrReceiver);
            mAppLockBrReceiver = null;
        }
    }

    public ServiceConnection getServiceConnection(){
        return new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBound = true;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBound = false;
            }
        };
    }

    private class AppLockBrReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ApplicationPassword.APPLICATION_IS_LOCKED)){
                gotoFirstActivity();
            }
        }
    }

}
