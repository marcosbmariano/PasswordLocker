package com.example.mark.passwordlocker.helpers;



import android.test.suitebuilder.annotation.LargeTest;

import com.example.mark.passwordlocker.PLMainActivityIntrumentationTest;
import com.example.mark.passwordlocker.activities.PLMainActivity;



import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import static com.example.mark.passwordlocker.helpers.ApplicationPasswordTest.PASSWORD;
/**
 * Created by marcos on 8/27/15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class LockUnlockTest extends PLMainActivityIntrumentationTest {
    private ApplicationPassword mAppPassword;
    private DatabaseKey mDatabaseKey;
    private ApplicationState mAppState;
    private PLMainActivity mainActivity;

    @Override
    protected void setUp() throws Exception {
        mainActivity = getMainActivity();
        mAppPassword = ApplicationPassword.getInstance();
        mDatabaseKey = DatabaseKey.getInstance();
        mAppState = ApplicationState.getInstance();
        mAppState.lockApplication();
        super.setUp();
    }

    public void test1Preconditions(){
        assertNotNull("mainActivity", mainActivity);
        assertNotNull("mAppPassword",mAppPassword);
        assertNotNull("mDatabaseKey", mDatabaseKey);
        assertNotNull("mAppState", mAppState);
    }
    public void test2ItsLocked(){
        assertTrue("mAppPassword.isPasswordLocked()",mAppPassword.isPasswordLocked());
        assertTrue("mDatabaseKey.isApplicationLocked()",mDatabaseKey.isApplicationLocked());
        assertTrue("mAppState.isApplicationLocked()",mAppState.isApplicationLocked());
    }

    public void test3ApplicationUnlockByPassword(){
        assertTrue("mAppState.isPasswordValid", mAppState.isPasswordValid(PASSWORD));
        assertFalse("mDatabaseKey.isApplicationLocked", mDatabaseKey.isApplicationLocked());
        assertFalse("mAppState.isApplicationLocked", mAppState.isApplicationLocked());
        assertFalse("mAppPassword.isPasswordLocked()",mAppPassword.isPasswordLocked());
        assertTrue("mAppPassword.isKeyValid()",mAppPassword.isKeyValid());

    }

    public void test4ApplicationNotUnlockByPassword(){
        assertFalse("mAppState.isPasswordValid", mAppState.isPasswordValid("garbage"));
        assertTrue("mDatabaseKey.isApplicationLocked", mDatabaseKey.isApplicationLocked());
        assertTrue("mAppState.isApplicationLocked", mAppState.isApplicationLocked());
        assertTrue("mAppPassword.isPasswordLocked()", mAppPassword.isPasswordLocked());
    }

    public void test5ApplicationNotUnlockByPassword2(){
        assertFalse("mAppState.isPasswordValid", mAppState.isPasswordValid(""));
        assertTrue("mDatabaseKey.isApplicationLocked", mDatabaseKey.isApplicationLocked());
        assertTrue("mAppState.isApplicationLocked", mAppState.isApplicationLocked());
        assertTrue("mAppPassword.isPasswordLocked()",mAppPassword.isPasswordLocked());
    }

    public void test6ApplicationNotUnlockByPassword3(){
        String password = null;
        //noinspection ConstantConditions
        assertFalse("mAppState.isPasswordValid", mAppState.isPasswordValid(password));
        assertTrue("mDatabaseKey.isApplicationLocked", mDatabaseKey.isApplicationLocked());
        assertTrue("mAppState.isApplicationLocked", mAppState.isApplicationLocked());
        assertTrue("mAppPassword.isPasswordLocked()",mAppPassword.isPasswordLocked());
    }

    public void test7LockPassword(){
        mAppState.isPasswordValid(PASSWORD);
        mAppPassword.lockPassword();
        assertTrue("mAppState.isApplicationLocked()", mAppState.isApplicationLocked());
        assertTrue("mDatabaseKey.isApplicationLocked()",mDatabaseKey.isApplicationLocked());
    }

    public void test8LockApplication(){
        mAppState.isPasswordValid(PASSWORD);
        mAppState.lockApplication();
        assertTrue("mAppPassword.isPasswordLocked()", mAppPassword.isPasswordLocked());
        assertTrue("mDatabaseKey.isApplicationLocked()",mDatabaseKey.isApplicationLocked());
    }


}
