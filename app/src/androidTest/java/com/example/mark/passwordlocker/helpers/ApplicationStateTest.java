package com.example.mark.passwordlocker.helpers;

import android.test.suitebuilder.annotation.LargeTest;

import com.example.mark.passwordlocker.PLMainActivityIntrumentationTest;
import com.example.mark.passwordlocker.activities.PLMainActivity;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 * Created by mark on 8/11/15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class ApplicationStateTest extends PLMainActivityIntrumentationTest {
    private PLMainActivity mActivity;
    private ApplicationState mAppState;
    private ApplicationPassword mAppPassword;
    private ObserverTest mObTest;
    private ApplicationPreferences mAppPreferences;



    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getMainActivity();
        mAppPassword = ApplicationPassword.getInstance();
        mObTest = new ObserverTest();
        ApplicationState.addObserver(mObTest);
        mAppPreferences = ApplicationPreferences.getInstance();
        mAppState = ApplicationState.getInstance();

    }

    public void testAAPreconditions(){
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
        if ( null == mAppState ){
            mAppState = ApplicationState.getInstance();
        }
        mAppState.lockApplication();
        assertTrue(mObTest.isLocked);
        assertTrue(mAppState.isPasswordValid(ApplicationPasswordTest.PASSWORD));
        assertFalse(mObTest.isLocked);
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
