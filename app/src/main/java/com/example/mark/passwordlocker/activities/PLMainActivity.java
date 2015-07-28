package com.example.mark.passwordlocker.activities;



import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.example.mark.passwordlocker.AccountRecord;
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


public class PLMainActivity extends ActionBarActivity  implements AccountsAdapter.AccountsAdapterUpdate,
        MyService.ServiceCallBack{ //TODO reviewed!!!

    private DatabaseHelper mDataHelper = null;
    private ApplicationPassword mApplicationPassword;
    private ApplicationState mApplicationState;
    private MyService mService;
    private ServiceConnection mServiceConnection;
    private boolean  mIsActivityVisible = false;
    private boolean isServiceBound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plmain);

        setupSingletons();
        setFirstFragment();
        setupDatabase();
        launchService();
        mIsActivityVisible = true;
        Log.e("Main activity", "on Create");
    }


    private void setupSingletons(){
        DatabaseKey.setContext(this);
        ApplicationPassword.setContext(this);
        ApplicationPreferences.setContext(this);
        DatabaseHelper.setupContext(this);
        mApplicationState = ApplicationState.getInstance();
    }

    private void setupDatabase(){
        mDataHelper = DatabaseHelper.getInstance();
        mDataHelper.addModel(AccountRecord.class);
        mDataHelper.createDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFirstFragment();
        launchNotificationIconManager();
        mIsActivityVisible = true;
    }


    @Override
    protected void onPause() {
        mIsActivityVisible = false;
        Log.e("inside mainActivity", "on Pause" );
        super.onPause();
    }

    @Override
    protected void onStop() {
        desconectFromService();
        super.onStop();
    }

    private void desconectFromService(){
        if ( isServiceBound){
            unbindService(mServiceConnection);
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
                    mService.addObserver(PLMainActivity.this);
                    isServiceBound = true;
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBound = false;
                    mService.deleteObsever(PLMainActivity.this);
                }
            };
        }
        return mServiceConnection;
    }


    private void setFirstFragment(){

        if (isApplicationPasswordDefined()){

            if (mApplicationState.isApplicationLocked()){
                swapFragment(R.id.MainFragContainer, new AppPassEnterFrag());
            }else{
                swapFragment(R.id.MainFragContainer, new RecyclerViewFragment());
            }

        }else{
            hideActionBar();
            swapFragment(R.id.MainFragContainer, new NewUserPassFrag());
        }
    }


    boolean isApplicationPasswordDefined(){
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

    @Override
    public void callOnBackPressed() {
        onBackPressed();
    }

    private void launchNotificationIconManager(){
        NotificationIconManager.setContext(this);
    }

    @Override
    public void updateActivity() {
        setFirstFragment();
    }

    @Override
    public boolean isActivityVisible() {
        return mIsActivityVisible;
    }

}
