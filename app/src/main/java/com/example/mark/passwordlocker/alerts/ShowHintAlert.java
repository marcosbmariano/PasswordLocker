package com.example.mark.passwordlocker.alerts;

import android.app.AlertDialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.helpers.ApplicationPassword;

/**
 * Created by mark on 2/4/15.
 */
public class ShowHintAlert extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence hint = getHint();


        builder.setTitle("Your Hint")
                .setMessage(hint)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    private String getHint(){
        return ApplicationPassword.getInstance().getHint();
    }
}
