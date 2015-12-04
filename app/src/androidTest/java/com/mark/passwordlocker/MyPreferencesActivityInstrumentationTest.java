package com.mark.passwordlocker;

import android.test.ActivityInstrumentationTestCase2;

import com.mark.passwordlocker.activities.MyPreferenceActivity;

/**
 * Created by mark on 9/16/15.
 */
public class MyPreferencesActivityInstrumentationTest extends
        ActivityInstrumentationTestCase2<MyPreferenceActivity> {
    private static MyPreferenceActivity myPreferenceActivity;

    public MyPreferencesActivityInstrumentationTest(){
        super(MyPreferenceActivity.class);
    }

    protected MyPreferenceActivity getMyPreferenceActivity(){
        if (null == myPreferenceActivity){
            myPreferenceActivity = getActivity();
        }
        return  myPreferenceActivity;
    }
}
