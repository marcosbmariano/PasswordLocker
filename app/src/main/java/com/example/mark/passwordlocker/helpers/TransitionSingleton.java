package com.example.mark.passwordlocker.helpers;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.mark.passwordlocker.R;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by mark on 11/19/15.
 */
public class TransitionSingleton implements TransitionHelper {
    private Activity mActiviy;
    private ViewGroup mRootLayout;
    private HashMap<View, ViewState> mFirstScene;
    private int transitionTime;
    private static TransitionSingleton mInstance;

    private TransitionSingleton(){
        mFirstScene = new HashMap<>();
        transitionTime = 100;
    }

    public static TransitionSingleton getInstance(){
        if ( null == mInstance ){
            mInstance = new TransitionSingleton();
        }
        return mInstance;
    }

    private void checkPreConditions(){
        if ( mActiviy == null || mRootLayout == null){
            throw new IllegalStateException("Set a reference to the activity and root " +
                    "layout before call a transition! ");
        }
    }

    public void setActivity(Activity activity){
        mActiviy = activity;
    }

    public void setRootLayout(int id){
        mRootLayout = (ViewGroup) mActiviy.findViewById(id);
    }

    @Override
    public void setFirstSceneViewsAndState(View v, ViewState state) {
        mFirstScene.put(v, state);
    }

    public void setFirstSceneViewsAndState(int viewId, ViewState state ){
        setFirstSceneViewsAndState(mActiviy.findViewById(viewId), state);
    }

    public void setFirstScene(){
        checkPreConditions();
        Set<View> views = mFirstScene.keySet();
        for( View v : views){
            if ( mFirstScene.get(v).equals(ViewState.GONE)){
                v.setVisibility(View.GONE);
            }else{
                v.setVisibility(View.VISIBLE);
            }
        }
        runTransition();
    }

    private void runTransition(){
        checkPreConditions();
        if (Build.VERSION.SDK_INT >= 21 ){
            AutoTransition transition = new AutoTransition();
            transition.setDuration(transitionTime);
            TransitionManager.beginDelayedTransition(mRootLayout, transition);
        }
    }

    public void toggleScene(){
        checkPreConditions();
        Set<View> views = mFirstScene.keySet();
        //runTransition();
        for( View v : views){
            if ( v.getVisibility() == View.GONE){
                v.setVisibility(View.VISIBLE);
            }else{
                v.setVisibility(View.GONE);
            }
        }

    }


}
