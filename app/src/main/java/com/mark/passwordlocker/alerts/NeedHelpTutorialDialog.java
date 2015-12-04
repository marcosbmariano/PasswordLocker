package com.mark.passwordlocker.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import android.widget.ScrollView;

import com.mark.passwordlocker.R;

/**
 * Created by mark on 1/14/15.
 */
public class NeedHelpTutorialDialog extends DialogFragment{


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ScrollView mScrollView = (ScrollView)inflater.inflate(R.layout.need_help_tutorial, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(mScrollView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}
