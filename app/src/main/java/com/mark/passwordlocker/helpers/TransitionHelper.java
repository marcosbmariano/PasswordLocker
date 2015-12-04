package com.mark.passwordlocker.helpers;

import android.app.Activity;
import android.view.View;

/**
 * Created by mark on 11/20/15.
 */
public interface TransitionHelper {
    enum ViewState { GONE, VISIBLE}

    void setActivity(Activity activity);
    void setRootLayout(int id);
    void setFirstSceneViewsAndState(View v, ViewState state);
    void setFirstScene();
    void toggleScene();

}
