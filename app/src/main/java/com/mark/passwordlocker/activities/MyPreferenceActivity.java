package com.mark.passwordlocker.activities;

import android.content.Intent;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import com.mark.passwordlocker.R;
import com.mark.passwordlocker.helpers.ApplicationPreferences;
import com.mark.passwordlocker.helpers.ApplicationState;


public class MyPreferenceActivity extends AppCompatActivity
        implements ApplicationState.ApplicationStateObserver{

    private Prefs1Fragment mFragment;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences_layout);

        addToObservers();
        setupToolBar();
        setupFragments();
    }

    private void addToObservers(){
        ApplicationState.addObserver(this);
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
        }else{
            ApplicationState.addObserver(this);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        ApplicationState.deleteObserver(this);
        super.onPause();
    }

    //this method is called in case the
    @Override
    public void applicationIsLocked() {
        moveToFirstActivity();
    }

    private void moveToFirstActivity(){
        startActivity(new Intent(this, PLMainActivity.class));
    }

    @Override
    public void applicationIsUnlocked() { /* Do nothing */  }

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
