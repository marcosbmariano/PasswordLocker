package com.mark.passwordlocker.helpers;

import android.app.Activity;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.mark.passwordlocker.R;


/**
 * Created by mark on 11/19/15.
 */
public class TransitionSingleton {
    private static Activity mActiviy;
    private ViewGroup mRootLayout;
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private ViewGroup mFragContainer;

    private int mTransitionTime;
    private static TransitionSingleton mInstance;
    private boolean isFirstScene;

    private TransitionSingleton(){
        mRootLayout = (ViewGroup) mActiviy.findViewById(R.id.main_container);
        mToolbar = (Toolbar) mActiviy.findViewById(R.id.app_bar);
        mFab = (FloatingActionButton)mActiviy.findViewById(R.id.fab);
        mFragContainer = (FrameLayout) mActiviy.findViewById(R.id.add_account_layout);
        mTransitionTime = 100;
    }
    public static void setActivity(Activity activity){
        mActiviy = activity;
    }

    public static TransitionSingleton getInstance(){
        if ( null == mInstance ){
            mInstance = new TransitionSingleton();
        }
        return mInstance;
    }

    public void startScene(){
        isFirstScene = true;
        mFragContainer.setVisibility(View.GONE);
        mToolbar.setVisibility(View.VISIBLE);
        mFab.setVisibility(View.VISIBLE);

    }

    private void runTransition(){
        if (Build.VERSION.SDK_INT >= 21 ){
            AutoTransition transition = new AutoTransition();
            transition.setDuration(mTransitionTime);
            TransitionManager.beginDelayedTransition(mRootLayout, transition);
        }
    }

    public void toggleScene(){
        if( isFirstScene){
            setSecondScene();
            isFirstScene = false;
        }else{
            startScene();
            isFirstScene = true;
        }
    }

    private void setSecondScene(){
        runTransition();
        mToolbar.setVisibility(View.GONE);
        mFab.setVisibility(View.GONE);
        mFragContainer.setVisibility(View.VISIBLE);
    }

}
