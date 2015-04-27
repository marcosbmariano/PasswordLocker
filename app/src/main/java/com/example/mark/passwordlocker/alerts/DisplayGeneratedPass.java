package com.example.mark.passwordlocker.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.activities.PLMainActivity;

/**
 * Created by mark on 1/14/15.
 */
public class DisplayGeneratedPass extends DialogFragment {
    public static final String GENERATED_PASSWORD = "generated_password";


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        TextView dialogTitle = (TextView)inflater.inflate(R.layout.dialog_title, null);
        dialogTitle.setText( new String( getPassword() ) );

        builder.setView(dialogTitle)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

        return builder.create();
    }

    private char [] getPassword(){
        Bundle bundle = getArguments();
        char [] pass = bundle.getCharArray(GENERATED_PASSWORD);
        char [] garbage = {'a'};
        bundle.putCharArray(GENERATED_PASSWORD, garbage);
        return pass;
    }

}
