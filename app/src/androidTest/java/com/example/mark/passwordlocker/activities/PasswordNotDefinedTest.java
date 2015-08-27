package com.example.mark.passwordlocker.activities;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;


import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.helpers.ApplicationPassword;

/**
 * Created by mark on 8/6/15.
 */
public class PasswordNotDefinedTest extends ActivityInstrumentationTestCase2<PLMainActivity> {
    private Button mSave;
    private EditText mEdtPassword;
    private EditText mEdtPasswordConfirmation;
    private EditText mPasswordHint;
    private PLMainActivity mPLMainActivity;
    private ApplicationPassword mApplicationPassword;

    private final String PASSWORD = "password";
    private final String HINT = "hint";


    public PasswordNotDefinedTest(){
        super(PLMainActivity.class);
    }


//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        mPLMainActivity = getActivity();
//        mApplicationPassword = ApplicationPassword.getInstance();
//
//        if ( !mApplicationPassword.isPasswordDefined() ){
//            mSave = (Button)mPLMainActivity.findViewById(R.id.btn_new_user_save);
//            mEdtPassword = (EditText)mPLMainActivity.findViewById(R.id.eT_app_pass_crea_pass);
//            mEdtPasswordConfirmation =
//                    (EditText)mPLMainActivity.findViewById(R.id.eT_app_pass_crea_confirm);
//
//            passwordNotDefinedSetupCheck();
//        }
//
//    }
//
//    private void passwordNotDefinedSetupCheck(){
//        assertNotNull("Button Save",mSave);
//        assertNotNull("pasword confimartion Edit text", mEdtPasswordConfirmation);
//    }
//
//    public void testPasswordManager(){
//        assertNotNull(mApplicationPassword);
//    }
//
//    public void testActivity(){
//        assertNotNull(mPLMainActivity);
//    }




//    @UiThreadTest
//    public void testPasswordInsertion(){
//
//        if ( !mApplicationPassword.isPasswordDefined()){
//            //test password without confirmation
//            mEdtPassword.setText(PASSWORD);
//            mSave.performClick();
//            assertFalse(mApplicationPassword.isPasswordDefined());
//
//            //test password with confirmation
//            mEdtPassword.setText(PASSWORD);
//            mEdtPasswordConfirmation.setText(PASSWORD);
//            setHint();
//
//            mSave.performClick();
//            assertTrue(mApplicationPassword.isPasswordDefined());
//
//            assertTrue(mApplicationPassword.isPasswordValid(PASSWORD));
//        }
//
//        passwordInsertionTest();
//        wrongPasswordTest();
//
//
//    }
//
//    private void setHint(){
//        mPasswordHint = (EditText)mPLMainActivity.findViewById(R.id.eT_app_pass_crea_hint);
//        assertNotNull("password hint not null", mPasswordHint);
//        mPasswordHint.setText(HINT);
//    }
//
//    private void passwordInsertionTest(){
//        assertTrue("password defined?", mApplicationPassword.isPasswordDefined());
//        assertTrue("Hint equals?",HINT.equals(mApplicationPassword.getHint()));
//        assertTrue("Passwod is valid?", mApplicationPassword.isPasswordValid(PASSWORD));
//    }
//
//    private void wrongPasswordTest(){
//        Log.e("inside test", "is password valid " +
//                mApplicationPassword.isPasswordValid("garbage"));
//
//        assertFalse("is false?", mApplicationPassword.isPasswordValid("garbage"));
//        fail();
//    }



}
