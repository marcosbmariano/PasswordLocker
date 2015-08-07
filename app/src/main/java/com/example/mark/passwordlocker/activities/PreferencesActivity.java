package com.example.mark.passwordlocker.activities;

import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;
import com.example.mark.passwordlocker.helpers.ApplicationState;
import com.example.mark.passwordlocker.services.MyService;

public class PreferencesActivity extends ActionBarActivity implements MyService.ServiceCallBack{
    private boolean mIsActivityVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsActivityVisible = true;
        MyService.addObserver(this);
        setContentView(R.layout.preferences_layout);
        getFragmentManager().beginTransaction()
                .replace(R.id.preference_container, new Prefs1Fragment())
                .commit();
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
    public void serviceDestroyed() {

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

}
