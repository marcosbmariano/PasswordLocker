package com.mark.passwordlocker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mark.passwordlocker.R;
import com.mark.passwordlocker.alerts.ShowHintAlert;
import com.mark.passwordlocker.helpers.ApplicationPassword;

/**
 * Created by mark on 1/13/15.
 */
public class AppPassEnterFrag extends BaseFragment implements View.OnClickListener{ //TODO reviewed

    private EditText mPassword;
    private Button mBtnShowHint;
    private static ResultPassEnter mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.app_enter_pass_frag, container, false);

        setupWidgtes(v);

        setFloatingButtonVisible(false);
        setAppBarVisibible(false);

        return v;
    }

    private void setupWidgtes(View v){
        mBtnShowHint = (Button)v.findViewById(R.id.btn_enter_pass_show_hint);
        mBtnShowHint.setVisibility(View.GONE);
        mBtnShowHint.setOnClickListener(this);
        mPassword = (EditText)v.findViewById(R.id.et_app_enter_pass_pass);

        v.findViewById(R.id.bt_app_enter_pass_enter).setOnClickListener(this);
    }

    public void setObserver(ResultPassEnter observer){
        mActivity = observer;
    }


    @Override
    public void onClick(View v) {
        int index = v.getId();

        switch (index){
            case R.id.bt_app_enter_pass_enter:
                checkPassword();
                break;

            case R.id.btn_enter_pass_show_hint:
                 new ShowHintAlert().show(
                         getActivity().getSupportFragmentManager(), null);
                break;

            default:
                //do nothing
        }
    }

    private void checkPassword(){
        if ( isPasswordValid() ){
            hideKeyBoard();
            mActivity.updateFromPasswordCheck();
        }else{
            Toast.makeText(getActivity(), "Password Not Valid!" , Toast.LENGTH_LONG).show();
            ifHasHintShowBtn();
        }
    }


    private void hideKeyBoard(){
       View view = getActivity().getCurrentFocus();//TODO fix this, not ok
        if( null != view) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean isPasswordValid(){
        boolean result = false;
        String password = mPassword.getText().toString();
        mPassword.setText("");

        if (!password.isEmpty()){
            result = ApplicationPassword.getInstance().unlockApplication(password);
        }
        return result;
    }

    private void ifHasHintShowBtn(){
        if ( hasHint()){
            mBtnShowHint.setVisibility(View.VISIBLE);
        }
    }
    private boolean hasHint(){
        return ApplicationPassword.getInstance().hasHint();
    }


    public interface ResultPassEnter{
        void updateFromPasswordCheck();
    }

}
