package com.example.mark.passwordlocker.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.mark.passwordlocker.AccountRecord;
import com.example.mark.passwordlocker.R;

/**
 * Created by mark on 3/18/15.
 */
public class DeleteAccountAlert extends DialogFragment {
    public static final String ACCOUNT_ID = "account_id";


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Delete this account?")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("Ont Item Click", "Positive");
                        AccountRecord.deleteAccount(getAccountId());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("Ont Item Click", "Negative");

                    }
                });

        return builder.create();
    }

    private long getAccountId(){
        Bundle args = getArguments();
        return args.getLong(ACCOUNT_ID);
    }


}


