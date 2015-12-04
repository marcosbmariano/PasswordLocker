package com.mark.passwordlocker.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mark.passwordlocker.helpers.ApplicationPreferences;
import com.mark.passwordlocker.helpers.ApplicationState;


/**
 * Created by mark on 7/20/15.
 * The ScreenLockBroadReceiver handles the device screen on and off
 * when any off these Intents are activated by the system
 * this receiver lock or unlock the singleton application state
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
