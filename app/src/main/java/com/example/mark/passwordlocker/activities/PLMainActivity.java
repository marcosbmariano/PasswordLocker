package com.example.mark.passwordlocker.activities;



import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mark.passwordlocker.account.AccountRecord;
import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.adapters.AccountsAdapter;
import com.example.mark.passwordlocker.alerts.NewAccountDialog;
import com.example.mark.passwordlocker.fragments.NewUserPassFrag;
import com.example.mark.passwordlocker.fragments.AppPassEnterFrag;
import com.example.mark.passwordlocker.fragments.RecyclerViewFragment;
import com.example.mark.passwordlocker.helpers.ApplicationPassword;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;
import com.example.mark.passwordlocker.helpers.ApplicationState;
import com.example.mark.passwordlocker.helpers.DatabaseKey;
import com.example.mark.passwordlocker.notifications.NotificationIconManager;
import com.example.mark.passwordlocker.services.MyService;
import com.marcos.autodatabases.utils.DatabaseHelper;


public class PLMainActivity extends AppCompatActivity implements AccountsAdapter.AccountsAdapterUpdate,
        MyService.ServiceCallBack{

    private FloatingActionButton mFloatingButton;
    private Toolbar mToolbar;
    private ApplicationPassword mApplicationPassword;
    private ApplicationState mApplicationState;
    private MyService mService;
    private ServiceConnection mServiceConnection;
    private AppPassEnterFrag mAppPassEnterFrag;
    private RecyclerViewFragment mRecyclerViewFragment;
    private boolean mIsActivityVisible = false;
    private boolean mIsServiceBound;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plmain);
        setupWidgets();
        setupSingletons();
        setFirstFragment();
        setupDatabase();
        launchService();
    }

    private void setupWidgets(){
        mToolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        mFloatingButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NewAccountDialog().show(getSupportFragmentManager(), null);
            }
        });
    }


    private void setupSingletons(){
        DatabaseKey.setContext(this);
        ApplicationPassword.setContext(this);
        ApplicationPreferences.setContext(this);
        DatabaseHelper.setupContext(this);
        mApplicationState = ApplicationState.getInstance();
    }

    private void setupDatabase(){
        DatabaseHelper mDataHelper; //TODO check if those methods should not be static
        mDataHelper = DatabaseHelper.getInstance();
        mDataHelper.addModel(AccountRecord.class);
        mDataHelper.createDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFirstFragment();
        launchNotificationIconManager();
        if( !mIsServiceBound){
            launchService();
        }
        mIsActivityVisible = true;
    }


    @Override
    protected void onPause() {
        mIsActivityVisible = false;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        desconectFromService();
        super.onDestroy();
    }

    @Override
    public void updateActivity() {
        setFirstFragment();
    }

    private void setFirstFragment(){
        if (isApplicationPasswordDefined()){
            if (mApplicationState.isApplicationLocked()){

                swapFragment( getAppPassEnterFrag());
            }else{
                swapFragment( getRecyclerViewFragment());
            }

        }else{
           // hideActionBar(); //fix
            swapFragment( new NewUserPassFrag());
        }
    }

    private AppPassEnterFrag getAppPassEnterFrag(){
        if ( null == mAppPassEnterFrag){
            mAppPassEnterFrag = new AppPassEnterFrag();
        }
        return mAppPassEnterFrag;
    }

    private RecyclerViewFragment getRecyclerViewFragment(){
        if ( null == mRecyclerViewFragment){
            mRecyclerViewFragment = new RecyclerViewFragment();
        }
        return mRecyclerViewFragment;
    }

    private boolean isApplicationPasswordDefined(){
        if ( null == mApplicationPassword){
            mApplicationPassword = ApplicationPassword.getInstance();
        }
        return mApplicationPassword.isPasswordDefined();
    }

    private void swapFragment(Fragment fragment){
        int container = R.id.MainFragContainer;
        getSupportFragmentManager().beginTransaction()
                .replace(container, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    private void desconectFromService(){
        stopService(new Intent(this, MyService.class));
        if (mIsServiceBound){
            unbindService(mServiceConnection);
            mIsServiceBound = false;
        }
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
                    MyService.addObserver(PLMainActivity.this); //TODO why this is static?
                    mIsServiceBound = true;
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mIsServiceBound = false;
                    MyService.deleteObsever(PLMainActivity.this); //TODO why this is static?
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

//    private void hideActionBar(){
//        if(null != mToolbar){
//            mToolbar.setVisibility(View.GONE);
//        }
//    }
//
//    private void hideFloatingButton(){
//        if(null != mFloatingButton){
//            mFloatingButton.setVisibility(View.GONE);
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if ( item.getItemId() == R.id.action_settings){
            startActivity(new Intent(this, MyPreferenceActivity.class));
            return true;
        }
       return super.onOptionsItemSelected(item);
    }

    @Override
    public void callOnBackPressed() { //fix
        onBackPressed();
    }

    private void launchNotificationIconManager(){
        NotificationIconManager.setContext(this);
    }


    @Override
    public boolean isActivityVisible() {
        return mIsActivityVisible;
    }

    @Override
    public void serviceDestroyed() {
        mIsServiceBound = false;
        launchService();
    }

}
