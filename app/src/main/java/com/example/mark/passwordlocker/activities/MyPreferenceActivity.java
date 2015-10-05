package com.example.mark.passwordlocker.activities;

import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;
import com.example.mark.passwordlocker.helpers.ApplicationState;
import com.example.mark.passwordlocker.services.MyService;

import java.util.List;

public class MyPreferenceActivity extends PreferenceActivity
        implements MyService.ServiceCallBack, ApplicationState.ApplicationStateObserver{
    private boolean mIsActivityVisible = false;
    private Prefs1Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsActivityVisible = true;
        MyService.addObserver(this);
        setContentView(R.layout.preferences_layout);

        mFragment = getFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.preference_container, mFragment,
                        Prefs1Fragment.class.getName())
                .commit();

        ApplicationState.addObserver(this);

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
        // Inflate the menu; this adds items to the action bar if it is present.
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
