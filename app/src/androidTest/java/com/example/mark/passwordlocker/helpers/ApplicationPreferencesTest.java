package com.example.mark.passwordlocker.helpers;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.activities.MyPreferenceActivity;

/**
 * Created by mark on 8/11/15.
 */
public class ApplicationPreferencesTest extends
        ActivityInstrumentationTestCase2<MyPreferenceActivity> {

    private MyPreferenceActivity mActivity;
    private ApplicationPreferences mAppPreferences;
    private ListPreference mClipBoardSeconds;
    private ListPreference mPasswordLength;
    private ListPreference mSecondsToLock;
    private SharedPreferences mSharedPreferences;
    private PreferenceFragment mFragment;


    public ApplicationPreferencesTest(){
        super(MyPreferenceActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mAppPreferences = ApplicationPreferences.getInstance();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mFragment = mActivity.getFragment();
        mClipBoardSeconds = getListPreference(R.string.preferences_clipboard_seconds_key);
        mPasswordLength = getListPreference(R.string.pref_password_length_key);
        mSecondsToLock = getListPreference(R.string.pref_time_to_lock_key);

    }




    public void testActivityNotNull(){
        assertNotNull(mActivity);
    }

    public void testPrefNotNull(){
        assertNotNull(mAppPreferences);
    }

    public void testSharedPrefNotNull(){
        assertNotNull(mSharedPreferences);
    }

    public void testClipBoardSeconds(){
        String secondsValue;
        int seconds;

        assertNotNull("mClipBoardSeconds not null",mClipBoardSeconds);

        CharSequence [] entryValues = mClipBoardSeconds.getEntryValues();
        for ( CharSequence value : entryValues){
            secondsValue = value.toString();
            mClipBoardSeconds.setValue(secondsValue);
            seconds = mAppPreferences.getClipBoardSeconds();
            assertEquals(seconds, Integer.valueOf(secondsValue).intValue());
        }

    }

    public void testPasswordLength(){
        String lenghtValue;
        int lenght;
        assertNotNull("Password Not Null", mPasswordLength);

        CharSequence [] entryValues = mPasswordLength.getEntryValues();

        for ( CharSequence value: entryValues){
            lenghtValue = value.toString();
            mPasswordLength.setValue(lenghtValue);
            lenght = mAppPreferences.getGeneratedPasswordLength();
            assertEquals(lenght, Integer.valueOf(lenghtValue).intValue());
        }

    }

    public void testSecondsToLock(){
        String secondsValue;
        int seconds;
        assertNotNull(mSecondsToLock);

        CharSequence [] entryValues = mSecondsToLock.getEntryValues();

        for(CharSequence value : entryValues){
            secondsValue = value.toString();
            mSecondsToLock.setValue(secondsValue);
            seconds = mAppPreferences.getSecondsToLockApplication();
            assertEquals(seconds, Integer.valueOf(secondsValue).intValue());
        }
    }

    private ListPreference getListPreference(int resource){
        return (ListPreference)mFragment
                .findPreference(getResourceString(resource));
    }

    private String getResourceString( int rStringId){
        return mActivity.getResources().
                getString(rStringId);
    }

}
