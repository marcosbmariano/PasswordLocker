package com.example.mark.passwordlocker.helpers;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.mark.passwordlocker.activities.PLMainActivity;

/**
 * Created by mark on 8/6/15.
 */
public class ApplicationPasswordTest extends ActivityInstrumentationTestCase2<PLMainActivity> {
    private ApplicationPassword mAplicationPassword;
    private PLMainActivity mMainActivity;
    public ApplicationPasswordTest() {
        super(PLMainActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
        ApplicationPassword.setContext(mMainActivity);
        mAplicationPassword = ApplicationPassword.getInstance();
    }

    public void testActivityNotNull(){
        assertNotNull(mMainActivity);
    }

    public void testApplicationPAsswordNotNull(){
        assertNotNull(mAplicationPassword);
    }

    public void testEncryptrion(){
        String password = "masycyae7";
        String booleanEncrypted = mAplicationPassword.encryptBoolean(password);
        Log.e("testEncryption", "" + booleanEncrypted);

        Log.e("testEncryption", "" +  mAplicationPassword.decryptBoolean(password));
    }
}
