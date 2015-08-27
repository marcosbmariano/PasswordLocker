package com.example.mark.passwordlocker.activities;


import android.test.ActivityInstrumentationTestCase2;

import android.test.UiThreadTest;
import android.util.Log;
import android.widget.EditText;

import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.helpers.ApplicationPassword;

/**
 * Created by mark on 5/8/15.
 */

public class PasswordDefinedTest extends ActivityInstrumentationTestCase2<PLMainActivity> {
    private PLMainActivity mPLMainActivity;
    private ApplicationPassword mApplicationPassword;
    private final String PASSWORD = "password";
    private final String HINT = "hint";


    public PasswordDefinedTest() {
        super(PLMainActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mPLMainActivity = getActivity();
        mApplicationPassword = ApplicationPassword.getInstance();
    }


//    public void testActivity(){
//        assertNotNull(mPLMainActivity);
//    }
//
//    public void testPasswordManager(){
//        assertNotNull(mApplicationPassword);
//    }


//    public void testPassword(){
//        if ( mApplicationPassword.isPasswordDefined()){
//            assertTrue(mApplicationPassword.isPasswordDefined());
//            assertTrue(HINT.equals(mApplicationPassword.getHint()));
//            assertTrue( mApplicationPassword.isPasswordValid(PASSWORD));
//        }

//
//        Log.e("inside test", "is password valid "+
//                mApplicationPassword.isPasswordValid("garbage"));
        //assertFalse("is false?", mApplicationPassword.isPasswordValid("garbage"));
//ￎﾐLHﾝ!￘ￏ￮ￌￃQﾘK

   // }

//    public void testWrongPassword(){
//        if ( mApplicationPassword.isPasswordDefined()){
//            Log.e("inside test", "is password valid "+
//                    mApplicationPassword.isPasswordValid("garbage"));
//
//            assertFalse("is false?", mApplicationPassword.isPasswordValid("garbage"));
//        }



   // }


}














































//
