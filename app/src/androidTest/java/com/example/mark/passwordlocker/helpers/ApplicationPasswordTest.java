package com.example.mark.passwordlocker.helpers;

import android.test.suitebuilder.annotation.LargeTest;

import com.example.mark.passwordlocker.PLMainActivityIntrumentationTest;
import com.example.mark.passwordlocker.activities.PLMainActivity;

/**
 * Created by mark on 8/6/15.
 */
@LargeTest
public class ApplicationPasswordTest extends PLMainActivityIntrumentationTest {
    private ApplicationPassword mAplicationPassword;
    private PLMainActivity mMainActivity;
    public static final String PASSWORD = "password";


    //TODO test a gigantic password

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getMainActivity();
        mAplicationPassword = ApplicationPassword.getInstance();
    }

    public void testPreconditions(){
        assertNotNull("mMainActivity",mMainActivity);
        assertNotNull("mAplicationPassword",mAplicationPassword);
    }

    public void testEncryptionDecryptionValidPassword(){
        //this method returns a encrypted boolean true
        String booleanEncrypted = mAplicationPassword.encryptBoolean(PASSWORD);
        assertTrue(Boolean.valueOf(mAplicationPassword.decrypt(PASSWORD, booleanEncrypted)));
    }

    public void testEncryptionDecryptionNotValidPassword(){
        //this method returns a encrypted boolean true
        String booleanEncrypted = mAplicationPassword.encryptBoolean(PASSWORD);

        //try a invalid another the password
        String newPassword = "sdfaetfss";
        assertFalse(Boolean.valueOf(mAplicationPassword.decrypt(newPassword, booleanEncrypted)));

        //try a empty another the password
        newPassword = "";
        assertFalse( Boolean.valueOf(mAplicationPassword.decrypt(newPassword, booleanEncrypted)));

        //try a null another the password
        newPassword = null;
        assertFalse( Boolean.valueOf(mAplicationPassword.decrypt(newPassword, booleanEncrypted)));

    }

    public void testIsPasswordValida(){
        assertTrue(mAplicationPassword.isPasswordValid(PASSWORD));
    }


    public void testIsKeyValid(){
        mAplicationPassword.isPasswordValid(PASSWORD);
        assertTrue(mAplicationPassword.isKeyValid());
    }

    public void testIsPasswordUnlocked(){
        mAplicationPassword.isPasswordValid(PASSWORD);
        assertFalse(mAplicationPassword.isPasswordLocked());
    }

    public void testIsKeyInvalidAndLocked(){
        mAplicationPassword.isPasswordValid("garbage");
        assertFalse("isKeyValid", mAplicationPassword.isKeyValid());
        assertTrue("isPasswordLocked()",mAplicationPassword.isPasswordLocked());
    }

    public void testLockPassword(){
        mAplicationPassword.isPasswordValid(PASSWORD);
        mAplicationPassword.lockPassword();
        assertTrue(mAplicationPassword.isPasswordLocked());
    }

    public void testDeleteKey(){
        mAplicationPassword.isPasswordValid(PASSWORD);
        assertTrue(mAplicationPassword.isKeyValid());
        mAplicationPassword.deleteKey();
        assertFalse(mAplicationPassword.isKeyValid());
    }

    public void testGetAppKeyWithPasswordLocked(){
        mAplicationPassword.isPasswordValid(PASSWORD);
        mAplicationPassword.lockPassword();

        try{
            mAplicationPassword.getAppKey();
            fail("IllegalStateException was supposed to be thrown");
        }catch(IllegalStateException e){
            //Do nothing
        }
    }



}
