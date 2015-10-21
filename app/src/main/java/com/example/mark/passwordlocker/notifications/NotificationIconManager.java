package com.example.mark.passwordlocker.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.activities.PLMainActivity;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;

/**
 * Created by mark on 7/23/15.
 */
public class NotificationIconManager implements
        ApplicationPreferences.PreferencesNotificationDisplayListener{
    private static Context mContext;
    private final int mId = 76597607;
    private static NotificationManager mNotificationManager;
    private static Notification mNotification;



    public static void setContext( Context context){
        mContext = context;
        new NotificationIconManager();
    }

    private NotificationIconManager(){
        if (null == mContext){
            throw new NullPointerException(
                    "NotificationIconShortcut must have a valid Context reference, use setContext.");
        }

        mNotificationManager = getmNotificationManager();
        //mAppPreferences = ApplicationPreferences.getInstance();
        ApplicationPreferences.addPreferencesNotificationDisplayListener(this);
        ifDisplayableBuildNotification();
    }


    private void ifDisplayableBuildNotification(){

        if (ApplicationPreferences.getInstance().isNotificationDisplayable()){
            displayShorcut();
        }
    }

    private void displayShorcut(){
        buildNotification();
        mNotificationManager.notify(mId, mNotification);
    }

    private void buildNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("A macacada")
                .setContentTitle("title")
                .setContentIntent(getPendingIntent());

        mNotification = builder.build();
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
    }

    private NotificationManager getmNotificationManager(){
        return (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private PendingIntent getPendingIntent(){
        Intent notificationIntent = new Intent(mContext, PLMainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT );
        return PendingIntent.getActivity(mContext, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void updateIsToShowNotification(boolean showNotification) {
            if ( !showNotification){
                mNotificationManager.cancel(mId);
            } else{
                displayShorcut();
            }
    }
}
