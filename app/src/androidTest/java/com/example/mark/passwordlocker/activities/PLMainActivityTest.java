package com.example.mark.passwordlocker.activities;

import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;

import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;

import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.fragments.AppPassEnterFrag;
import com.example.mark.passwordlocker.fragments.NewUserPassFrag;
import com.example.mark.passwordlocker.helpers.ApplicationPassword;

/**
 * Created by mark on 5/8/15.
 */
public class PLMainActivityTest extends ActivityInstrumentationTestCase2<PLMainActivity> {
    private PLMainActivity mPLMainActivity;
    private Button mSave;
    private EditText mEdtPassword;
    private EditText mEdtPasswordConfirmation;
    private EditText mPasswordHint;
    private ApplicationPassword mApplicationPassword;


    public PLMainActivityTest() {
        super(PLMainActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mPLMainActivity = getActivity();
        mSave = (Button)mPLMainActivity.findViewById(R.id.btn_new_user_save);
        mEdtPassword = (EditText)mPLMainActivity.findViewById(R.id.eT_app_pass_crea_pass);
        mEdtPasswordConfirmation =
                (EditText)mPLMainActivity.findViewById(R.id.eT_app_pass_crea_confirm);
        mApplicationPassword = ApplicationPassword.getInstance();

        if (mApplicationPassword.isPasswordDefined()){
            mApplicationPassword.deleteApplicationPassword();
        }

        mPasswordHint = (EditText)mPLMainActivity.findViewById(R.id.eT_app_pass_crea_hint);

    }


    public void testActivity(){
        assertNotNull(mPLMainActivity);
    }

    public void testButtonSave(){
        assertNotNull(mSave);
    }

    public void testEtPassword(){
        assertNotNull(mEdtPassword);
    }

    public void testEtPasswordConfirmation(){
        assertNotNull( mEdtPasswordConfirmation);
    }

    public void testPasswordManager(){
        assertNotNull(mApplicationPassword);
    }

    public void testPasswordHint(){
        assertNotNull( mPasswordHint);
    }



    @UiThreadTest
    public void testSetPassword(){
        String hint = "HINT";
        String password = "NewPassword";

        //test password without confirmation
        mEdtPassword.setText(password);
        mSave.performClick();
        assertFalse(mApplicationPassword.isPasswordDefined());

        Fragment fragment = mPLMainActivity.getSupportFragmentManager()
                .findFragmentByTag(AppPassEnterFrag.class.getSimpleName());

       // assertNotNull("FirstFrag", fragment);

        fragment = mPLMainActivity.getSupportFragmentManager()
                .findFragmentByTag(NewUserPassFrag.class.getSimpleName());

        //assertNull("Second frag", fragment);




        //test password with confirmation
        mEdtPassword.setText(password);
        mEdtPasswordConfirmation.setText(password);
        mPasswordHint.setText(hint);

        mSave.performClick();
        assertTrue(mApplicationPassword.isPasswordDefined());
        assertTrue(hint.equals(mApplicationPassword.getHint()));

    }

}
