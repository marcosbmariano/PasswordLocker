package com.mark.passwordlocker.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import com.mark.passwordlocker.R;
import com.mark.passwordlocker.helpers.ApplicationPreferences;
import com.mark.passwordlocker.helpers.ApplicationState;


public class MyPreferenceActivity extends AppCompatActivity {

    private Prefs1Fragment mFragment;
    private Toolbar mToolBar;
    private MyBroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_layout);
        registerReceiver();
        setupToolBar();
        setupFragments();
    }

    private void registerReceiver(){
        mReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(ApplicationState.APPLICATION_IS_UNLOCKED);
        filter.addAction(ApplicationState.APPLICATION_IS_LOCKED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
    }


    private void setupFragments(){
        mFragment = getFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.preference_container, mFragment,
                        Prefs1Fragment.class.getName())
                .commit();
    }

    private void setupToolBar(){
        mToolBar = (Toolbar)findViewById(R.id.app_bar_preferences);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public Prefs1Fragment getFragment(){
        if ( null == mFragment){
            mFragment = new Prefs1Fragment();
        }
        return mFragment;
    }

    @Override
    protected void onResume() {
        if(ApplicationState.getInstance().isApplicationLocked()){
            moveToFirstActivity();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if( null != mReceiver){
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }

    private void moveToFirstActivity(){
        startActivity(new Intent(this, PLMainActivity.class));
    }

    private class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            if( intent.getAction().equals(ApplicationState.APPLICATION_IS_LOCKED)){
                moveToFirstActivity();
                Log.e("Inside MyPreferencesActivity", "APP is locked");
            }
            if (intent.getAction().equals(ApplicationState.APPLICATION_IS_UNLOCKED)){
                Log.e("Inside MyPreferencesActivity", "APP is unlocked");
            }
        }
    }


    /**
     * This fragment shows the preferences for the first header.
     */
    public static class Prefs1Fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onResume() {
            getPreferenceManager().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(ApplicationPreferences.getInstance());
            super.onResume();
        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(ApplicationPreferences.getInstance());
            super.onPause();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_preferences, menu);
        return true;
    }

}
