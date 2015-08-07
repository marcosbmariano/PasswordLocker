package com.example.mark.passwordlocker.fragments;



import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by mark on 1/21/15.
 */
public class BaseFragment extends Fragment {


    public void hideActionBar(boolean hide){

        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();

        if ( actionBar != null){
            if (hide){
                actionBar.hide();
            }else{
                actionBar.show();
            }
        }
    }

}
