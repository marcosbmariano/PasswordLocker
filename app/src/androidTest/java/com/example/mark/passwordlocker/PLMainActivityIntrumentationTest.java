package com.example.mark.passwordlocker;

import android.test.ActivityInstrumentationTestCase2;


import com.example.mark.passwordlocker.activities.PLMainActivity;

/**
 * Created by mark on 9/16/15.
 */
public class PLMainActivityIntrumentationTest extends
        ActivityInstrumentationTestCase2<PLMainActivity> {
    private static PLMainActivity mainActivity;


    public PLMainActivityIntrumentationTest(){
        super(PLMainActivity.class);

    }

    protected PLMainActivity getMainActivity(){
        if ( null == mainActivity){
            mainActivity = getActivity();
        }
        return mainActivity;
    }
}
