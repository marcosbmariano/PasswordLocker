package com.mark.passwordlocker.helpers;

import android.test.suitebuilder.annotation.LargeTest;

import com.mark.passwordlocker.PLMainActivityIntrumentationTest;
import com.mark.passwordlocker.activities.PLMainActivity;

/**
 * Created by mark on 8/6/15.
 */
@LargeTest
public class ApplicationPasswordTest extends PLMainActivityIntrumentationTest {
    private ApplicationPassword mApplicationPassword;
    private PLMainActivity mMainActivity;
    public static final String PASSWORD = "password";


    //TODO test a gigantic password

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getMainActivity();
        mApplicationPassword = ApplicationPassword.getInstance();
    }

    public void testPreconditions(){
        assertNotNull("mMainActivity",mMainActivity);
        assertNotNull("mAplicationPassword",mApplicationPassword);
    }

    public void testEncryptionDecryptionValidPassword(){
        //this method returns a encrypted boolean true
        String booleanEncrypted = mApplicationPassword.encryptBoolean(PASSWORD);
        assertTrue(Boolean.valueOf(mApplicationPassword.decrypt(PASSWORD, booleanEncrypted)));
    }

    public void testEncryptionDecryptionNotValidPassword(){
        //this method returns a encrypted boolean true
        String booleanEncrypted = mApplicationPassword.encryptBoolean(PASSWORD);

        //try a invalid another the password
        String newPassword = "sdfaetfss";
        assertFalse(Boolean.valueOf(mApplicationPassword.decrypt(newPassword, booleanEncrypted)));

        //try a empty another the password
        newPassword = "";
        assertFalse( Boolean.valueOf(mApplicationPassword.decrypt(newPassword, booleanEncrypted)));

        //try a null another the password
        newPassword = null;
        //noinspection ConstantConditions
        assertFalse( Boolean.valueOf(mApplicationPassword.decrypt(newPassword, booleanEncrypted)));

    }

    public void testIsPasswordValida(){
        assertTrue(mApplicationPassword.isPasswordValid(PASSWORD));
    }


    public void testIsKeyValid(){
        mApplicationPassword.isPasswordValid(PASSWORD);
        assertTrue(mApplicationPassword.isKeyValid());
    }

    public void testIsPasswordUnlocked(){
        mApplicationPassword.isPasswordValid(PASSWORD);
        assertFalse(mApplicationPassword.isPasswordLocked());
    }

    public void testIsKeyInvalidAndLocked(){
        mApplicationPassword.isPasswordValid("garbage");
        assertFalse("isKeyValid", mApplicationPassword.isKeyValid());
        assertTrue("isPasswordLocked()", mApplicationPassword.isPasswordLocked());
    }

    public void testLockPassword(){
        mApplicationPassword.isPasswordValid(PASSWORD);
        mApplicationPassword.lockPassword();
        assertTrue(mApplicationPassword.isPasswordLocked());
    }

    public void testDeleteKey(){
        mApplicationPassword.isPasswordValid(PASSWORD);
        assertTrue(mApplicationPassword.isKeyValid());
        mApplicationPassword.deleteKey();
        assertFalse(mApplicationPassword.isKeyValid());
    }

    public void testGetAppKeyWithPasswordLocked(){
        mApplicationPassword.isPasswordValid(PASSWORD);
        mApplicationPassword.lockPassword();

        try{
            mApplicationPassword.getAppKey();
            fail("IllegalStateException was supposed to be thrown");
        }catch(IllegalStateException e){
            //Do nothing
        }
    }



}
