package com.example.mark.passwordlocker.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import com.example.mark.passwordlocker.account.AccountRecord;
import com.example.mark.passwordlocker.R;

/**
 * Created by mark on 3/18/15.
 */
public class DeleteAccountAlert extends DialogFragment {
    public static final String ACCOUNT_ID = "account_id";
    public static final String ACCOUNT = "account";
    private Bundle mArgs;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mArgs = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Delete " + getAccount() + "?")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AccountRecord.deleteAccount(getAccountId());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                            }
                });

        return builder.create();
    }

    private long getAccountId(){
        return mArgs.getLong(ACCOUNT_ID);
    }

    private String getAccount(){
        return mArgs.getString(ACCOUNT);
    }


}


