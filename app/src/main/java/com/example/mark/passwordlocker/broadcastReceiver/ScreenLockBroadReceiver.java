package com.example.mark.passwordlocker.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mark.passwordlocker.helpers.ApplicationPreferences;
import com.example.mark.passwordlocker.helpers.ApplicationState;


/**
 * Created by mark on 7/20/15.
 */
public class ScreenLockBroadReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        ApplicationState applicationState = ApplicationState.getInstance();

        if ( intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            applicationState.lockApplication();


        }else if ( intent.getAction().equals(Intent.ACTION_SCREEN_ON)  ){
            if (ApplicationPreferences.getInstance().isToUnlockApplicationOnScreenOn()) {
                applicationState.unlockApplication();

            }
        }

    }
}
