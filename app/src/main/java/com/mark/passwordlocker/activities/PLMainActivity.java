package com.mark.passwordlocker.activities;






import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import com.mark.passwordlocker.R;
import com.mark.passwordlocker.fragments.NewUserPassFrag;
import com.mark.passwordlocker.fragments.AppPassEnterFrag;
import com.mark.passwordlocker.helpers.ApplicationPassword;
import com.mark.passwordlocker.helpers.ApplicationState;
import com.mark.passwordlocker.notifications.NotificationIconManager;



public class PLMainActivity extends AppCompatActivity implements
        AppPassEnterFrag.ResultPassEnter {

    private ApplicationPassword mApplicationPassword;
    private ApplicationState mApplicationState;
    private AppPassEnterFrag mAppPassEnterFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        getAppPassEnterFrag();
    }


    private void setupSingletonsReferences(){
        mApplicationState = ApplicationState.getInstance();
        mApplicationPassword = ApplicationPassword.getInstance();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setupSingletonsReferences();
        setFirstFragment();
        launchNotificationIconManager();
    }

    private void setFirstFragment() {
        //if the user has set a app password
        if (isApplicationPasswordDefined()) {
            //if the application is locked, user will enter the app password
            if (mApplicationState.isApplicationLocked()) {
                swapFragment(getAppPassEnterFrag());

            } else {
                //if the application is unlocked, go straight to the account info
                startActivity(new Intent(this, SecondActivity.class));
            }
        } else {
            //if user never set a password
            swapFragment(new NewUserPassFrag());
        }
    }

    private void hideKeyBoard(){
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private AppPassEnterFrag getAppPassEnterFrag(){
        if ( null == mAppPassEnterFrag){
            mAppPassEnterFrag = new AppPassEnterFrag();
            mAppPassEnterFrag.setObserver(this);
        }
        return mAppPassEnterFrag;
    }

    private boolean isApplicationPasswordDefined(){
        if ( null == mApplicationPassword){
            mApplicationPassword = ApplicationPassword.getInstance();
        }
        return mApplicationPassword.isPasswordDefined();
    }

    private void swapFragment(Fragment fragment){
        int container = R.id.MainFragContainer;
        getSupportFragmentManager().beginTransaction()
                .replace(container, fragment, fragment.getClass().getSimpleName())
                .commit();
    }

    //launch icon so user will be able to launch the app
    private void launchNotificationIconManager(){
        NotificationIconManager.setContext(this);
    }

    @Override
    public void updateFromPasswordCheck() {
        setFirstFragment();
    }



}
