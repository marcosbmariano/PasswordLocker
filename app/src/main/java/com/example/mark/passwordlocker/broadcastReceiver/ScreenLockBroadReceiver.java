package com.example.mark.passwordlocker.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mark.passwordlocker.helpers.ApplicationPassword;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;
import com.example.mark.passwordlocker.helpers.ApplicationState;
import com.example.mark.passwordlocker.services.MyService;

/**
 * Created by mark on 7/20/15.
 */
public class ScreenLockBroadReceiver extends BroadcastReceiver {
    private ApplicationState mApplicationState;

    @Override
    public void onReceive(Context context, Intent intent) {

        mApplicationState = ApplicationState.getInstance();

        if ( intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            mApplicationState.lockApplication();

        }else if ( intent.getAction().equals(Intent.ACTION_SCREEN_ON)  ){
            if (ApplicationPreferences.getInstance().isToUnlockApplicationOnScreenOn()) {
                mApplicationState.unlockApplication();
            }
        }

    }
}
