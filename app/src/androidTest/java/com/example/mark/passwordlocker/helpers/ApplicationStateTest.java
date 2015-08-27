package com.example.mark.passwordlocker.helpers;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.mark.passwordlocker.activities.PLMainActivity;

/**
 * Created by mark on 8/11/15.
 */
public class ApplicationStateTest extends ActivityInstrumentationTestCase2<PLMainActivity> {
    private PLMainActivity mActivity;
    private ApplicationState mAppState;
    private ApplicationPassword mAppPassword;
    private ObserverTest mObTest;
    private ApplicationPreferences mAppPreferences;

    public ApplicationStateTest(){
        super(PLMainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mAppState = ApplicationState.getInstance();
        mAppPassword = ApplicationPassword.getInstance();
        mObTest = new ObserverTest();
        ApplicationState.addObserver(mObTest);
        mAppPreferences = ApplicationPreferences.getInstance();

    }

    public void testPreconditions(){
        assertNotNull("mActivity",mActivity);
        assertNotNull("mAppState",mAppState);
        assertNotNull("mAppState",mAppState);
        assertNotNull("mAppPreferences",mAppPreferences);
    }

    public void testAppLock(){
        assertTrue(mAppState.isApplicationLocked());
        mAppState.isPasswordValid(ApplicationPasswordTest.PASSWORD);
        assertFalse("It is supposed to be unlocked",mAppState.isApplicationLocked());
        mAppState.lockApplication();
        assertTrue("It is supposed to be locked", mAppState.isApplicationLocked());
    }


    public void testObserver(){
        mAppState.lockApplication();
        assertTrue(mObTest.isLocked);
        assertTrue(mAppState.isPasswordValid(ApplicationPasswordTest.PASSWORD));
        assertFalse(mObTest.isLocked);
    }

    public void testSuspendLockAndIsToLock(){

        //mAppState.suspendLock();
        //assertFalse(mAppState.isToLockApplication());
    }

    public void testIsToLock(){

    }
    
    class ObserverTest implements ApplicationState.ApplicationStateObserver {
        public boolean isLocked;

        @Override
        public void applicationIsLocked() {
            isLocked = true;
        }

        @Override
        public void applicationIsUnlocked() {
            isLocked = false;
        }
    }

}
