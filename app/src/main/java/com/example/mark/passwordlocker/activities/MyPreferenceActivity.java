package com.example.mark.passwordlocker.activities;

import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;
import com.example.mark.passwordlocker.helpers.ApplicationState;
import com.example.mark.passwordlocker.services.MyService;



public class MyPreferenceActivity extends AppCompatActivity
        implements MyService.ServiceCallBack, ApplicationState.ApplicationStateObserver{

    private boolean mIsActivityVisible = false;
    private Prefs1Fragment mFragment;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsActivityVisible = true;
        setContentView(R.layout.preferences_layout);

        addToObervers();
        setupToolBar();
        setupFragments();

    }

    private void addToObervers(){
        MyService.addObserver(this);
        ApplicationState.addObserver(this);

    }

    private void setupFragments(){
        mFragment = getFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.preference_container, mFragment,
                        Prefs1Fragment.class.getName())
                .commit();
    }



//
//    @Override
//    public void onBuildHeaders(List<Header> target) {
//        loadHeadersFromResource(R.xml.preferences_header, target);
//    }
//
//    @Override
//    protected boolean isValidFragment(String fragmentName) {
//        return Prefs1Fragment.class.getName().equals(fragmentName);
//    }

    private void setupToolBar(){
        mToolBar = (Toolbar)findViewById(R.id.app_bar);
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
        mIsActivityVisible = true;
        if(ApplicationState.getInstance().isApplicationLocked()){
            onNavigateUp();
        }
        super.onResume();

    }

    @Override
    protected void onPause() {
        mIsActivityVisible = false;
        super.onPause();
    }

    @Override
    public void applicationIsLocked() {
        onNavigateUp();
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

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.

            //PreferenceManager.setDefaultValues(getActivity(),
            // R.xml.preferences, false);

            // Load the preferences from an XML resource
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

    @Override
    public void updateActivity() {
        onNavigateUp();
    }

    @Override
    public boolean isActivityVisible() {
        return mIsActivityVisible;
    }

    @Override
    public void serviceDestroyed() { /*Do nothing*/ }

}
