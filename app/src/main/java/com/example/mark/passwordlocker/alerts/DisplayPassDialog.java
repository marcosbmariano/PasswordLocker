package com.example.mark.passwordlocker.alerts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.activities.PLMainActivity;

import java.util.concurrent.TimeUnit;

/**
 * Created by mark on 1/14/15.
 */
public class DisplayPassDialog extends DialogFragment {
    public static final String BUNDLE_PASSWORD = "bundlePassword";
    private TextView mDialogTitle;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final String password = getPassword();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        mDialogTitle = (TextView)inflater.inflate(R.layout.dialog_title, null);
        mDialogTitle.setText(password);

        builder.setView(mDialogTitle)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //do nothing, just to close the dialog
                    }
                })
                .setNegativeButton(R.string.copy, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        copyToClipboard(password);
                    }
                });

        return builder.create();
    }

    private String getPassword(){
        Bundle bundle = getArguments();
        String pass = bundle.getString(BUNDLE_PASSWORD);

        //these lines erase the password from bundle that is returned by getArguments
        char [] garbage = {'a'};
        bundle.putCharArray(BUNDLE_PASSWORD,garbage);

        return pass;
    }

    private void copyToClipboard(String password) {

        ClipboardManager manager = (ClipboardManager) getActivity().
                getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip( ClipData.newPlainText("", password ));

        startCounter();

    }

    //this is a counter to erase the password from the clipBoard
    private void startCounter(){

        final Activity activity = getActivity();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    TimeUnit.MINUTES.sleep(getMinutes());
                    cleanClipBoard();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //get the minutes set in the preferences
            private int getMinutes(){
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
                String key = getResources().getString(R.string.preferences_clipboard_minutes_key);
                return Integer.valueOf(pref.getString(key,"0"));
            }

            private void cleanClipBoard(){
                ClipboardManager manager = (ClipboardManager) activity.
                        getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText("",""));
            }
        };

        new Thread(runnable).start();

    }


}


