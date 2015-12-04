package com.mark.passwordlocker.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mark.passwordlocker.R;
import com.mark.passwordlocker.alerts.NeedHelpTutorialDialog;

/**
 * Created by mark on 1/13/15.
 */
public class NeedHelpFrag extends Fragment implements View.OnClickListener{
    private TextView mTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        mTextView = (TextView) inflater.inflate(R.layout.need_help_layout, container, false);
        mTextView.setOnClickListener(this);

        return mTextView;

    }

    @Override
    public void onClick(View v) {
        new NeedHelpTutorialDialog().show(getFragmentManager(), null);

    }
}
