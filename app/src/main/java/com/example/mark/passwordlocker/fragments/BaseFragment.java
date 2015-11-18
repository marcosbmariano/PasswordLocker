package com.example.mark.passwordlocker.fragments;



import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.mark.passwordlocker.R;

/**
 * Created by mark on 1/21/15.
 */
public class BaseFragment extends Fragment {
    private Toolbar mAppbar;
    private FloatingActionButton mFloatingButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWidgets();
    }

    private void setupWidgets(){
        mFloatingButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        mAppbar = (Toolbar) getActivity().findViewById(R.id.app_bar);
    }

    protected void setAppBarVisibible(boolean visibible) {
        if( null != mAppbar){
            if( visibible){
                mAppbar.setVisibility(View.VISIBLE);
            }else{
                mAppbar.setVisibility(View.GONE);
            }
        }
    }

    protected void setFloatingButtonVisible(boolean visible){
        if(null != mFloatingButton){
            if( visible){
                mFloatingButton.setVisibility(View.VISIBLE);
            }else{
                mFloatingButton.setVisibility(View.GONE);
            }
        }
    }
}
