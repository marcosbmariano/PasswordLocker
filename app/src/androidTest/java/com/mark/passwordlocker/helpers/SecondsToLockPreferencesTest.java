package com.mark.passwordlocker.helpers;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.LargeTest;

import com.mark.passwordlocker.MyPreferencesActivityInstrumentationTest;
import com.mark.passwordlocker.R;
import com.mark.passwordlocker.activities.MyPreferenceActivity;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 * Created by mark on 8/31/15.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class SecondsToLockPreferencesTest extends MyPreferencesActivityInstrumentationTest {
    private ApplicationPassword mAppPassword;
    private ApplicationState mAppState;
    private MyPreferenceActivity mActivity;
    private ListPreference mSecondsToLock;
    private PreferenceFragment mFragment;
    private SharedPreferences mSharedPreferences;
    private ApplicationPreferences mAppPreferences;
    private CharSequence [] mEntryValues;



    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getMyPreferenceActivity();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        mAppPreferences = ApplicationPreferences.getInstance();
        mAppState = ApplicationState.getInstance();
        mAppState.isPasswordValid(ApplicationPasswordTest.PASSWORD);
        mAppPassword = ApplicationPassword.getInstance();
        mFragment = mActivity.getFragment();
        mSecondsToLock = getListPreference();
        mEntryValues = mSecondsToLock.getEntryValues();
    }


    public void testAssertPreconditions() {
        assertNotNull("mActivity", mActivity);
        assertNotNull("mAppState", mAppState);
        assertNotNull("mAppPassword", mAppPassword);
        assertNotNull("mSharedPreferences", mSharedPreferences);
        assertNotNull("mFragment", mFragment);
        assertNotNull("mSecondsToLock", mSecondsToLock);
        assertNotNull("mAppPreferences", mAppPreferences);

    }

    public void testCorrectNumberOfEntries(){
        assertTrue("mEntryValues", mEntryValues.length == 5);
    }

    @UiThreadTest
    public void testASecondsToLock(){

        String secondsValue;
        int seconds;
        assertNotNull(mSecondsToLock);

        CharSequence [] entryValues = mSecondsToLock.getEntryValues();

        for(CharSequence value : entryValues){
            mAppState.isPasswordValid(ApplicationPasswordTest.PASSWORD);

            secondsValue = value.toString();
            int secondsValueInt = Integer.valueOf(secondsValue).intValue();

            mSecondsToLock.setValue(secondsValue);

            //the method onSharePreferencesChanged is not called if the value
            //of ListPreference is set Programmatically
            updateSharePreferences();

            //check if the value set on preferences activity is the value got
            //by appPreferences
            seconds = mAppPreferences.getSecondsToLockApplication();
            assertEquals(seconds,secondsValueInt );

            if ( seconds >= 30){
                assertEquals(seconds,mAppState.getCounterSeconds());
                assertTrue(mAppState.secondsToLockMatchCounterSeconds());
                assertTrue("is to lock", mAppState.isToLockApplication());

                testLockApplication(seconds);
                testUnlockApplicationFalse(seconds);

            } else if( seconds == -1){
                assertTrue("is to lock, -1 ", mAppState.isToLockApplication());
                assertFalse(mAppState.secondsToLockMatchCounterSeconds());
                testLockApplication(seconds);
                testUnlockApplicationFalse(seconds);

            } else if( seconds == -2){
                assertFalse("is to lock, -2 ", mAppState.isToLockApplication());
                assertFalse(mAppState.secondsToLockMatchCounterSeconds());
                testLockApplication(seconds);
                testUnlockApplicationTrue(seconds);
            }
        }
    }

    private void testLockApplication(int seconds){
        mAppState.lockApplication();
        assertTrue("mAppState.isApplicationLocked() sec "
                + seconds, mAppState.isApplicationLocked());
    }
    // application is supposed to remain locked until user opens with the password
    private void testUnlockApplicationFalse(int seconds){
        assertFalse(mAppPassword.isKeyValid());
        mAppState.unlockApplication();
        assertTrue("mAppState.isApplicationLocked() after locked sec " + seconds
                ,mAppState.isApplicationLocked());
    }
    //application is supposed to unlock without the need to reenter the password
    private void testUnlockApplicationTrue(int seconds){
        assertTrue(mAppPassword.isKeyValid());
        mAppState.unlockApplication();
        assertFalse("mAppState.isApplicationLocked() after locked sec " + seconds
                , mAppState.isApplicationLocked());
    }


    @UiThreadTest
    public void testSuspendAndResumeLock(){
        int seconds;
        mSecondsToLock.setValue(mEntryValues[0].toString());
        updateSharePreferences();
        seconds = mAppPreferences.getSecondsToLockApplication();
        assertTrue("is to lock1", mAppState.isToLockApplication());
        assertEquals("30 seconds", seconds, mAppState.getCounterSeconds());

        mAppState.suspendLock();
        assertFalse("is to lock2", mAppState.isToLockApplication());

        mAppState.resumeLock();
        assertTrue("is to lock3", mAppState.isToLockApplication());

        assertEquals(seconds,mAppState.getCounterSeconds());
        assertTrue(mAppState.secondsToLockMatchCounterSeconds());
        assertTrue("is to lock4", mAppState.isToLockApplication());

    }


    private void updateSharePreferences(){
        mAppPreferences.onSharedPreferenceChanged(mSharedPreferences,
                mSecondsToLock.getKey());
    }

    private ListPreference getListPreference(){
        return (ListPreference)mFragment
                .findPreference(getResourceString(R.string.pref_time_to_lock_key));
    }

    private String getResourceString( int rStringId){
        return mActivity.getResources().
                getString(rStringId);
    }

}
































//