package com.example.mark.passwordlocker.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mark.passwordlocker.helpers.ApplicationPassword;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;
import com.example.mark.passwordlocker.services.MyService;

/**
 * Created by mark on 7/20/15.
 */
public class ScreenLockBroadReceiver extends BroadcastReceiver {
    private ApplicationPassword mApplicationPassword;

    @Override
    public void onReceive(Context context, Intent intent) {

        mApplicationPassword = ApplicationPassword.getInstance();

        if ( intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Log.e("Broadcastreceiver", "lock application");
            mApplicationPassword.lockPassword();
        }else if ( intent.getAction().equals(Intent.ACTION_SCREEN_ON)  ){ //TODO add an option for the user
            Log.e("Broadcastreceiver", "unlock application");
            if (ApplicationPreferences.getInstance().isToUnlockApplicationOnScreenOn()) {
                mApplicationPassword.unlockPassword();                       //TODO to open the application or not
            }
        }



    }
}
