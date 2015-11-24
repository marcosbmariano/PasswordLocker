package com.example.mark.passwordlocker.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.example.mark.passwordlocker.account.AccountRecord;
import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.account.AccountSensitiveData;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;
import com.example.mark.passwordlocker.helpers.ApplicationState;
import com.example.mark.passwordlocker.helpers.TransitionSingleton;
import com.example.mark.passwordmanager.RawData;
import com.example.mark.passwordmanager.generator.PasswordGenerator;

/**
 * Created by mark on 1/14/15.
 */



public class NewAccountFragment extends Fragment
        implements View.OnClickListener, PasswordGenerator.PasswordGeneratorListener{

    private EditText mETAccountName;
    private PassCreationFrag mPassCreationFrag;
    private ApplicationState mApplicationState;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_pass_account, container, false);
        setupWidgets(view);
        setupFragment();
        mApplicationState = ApplicationState.getInstance();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mApplicationState.suspendLock();
    }

    private void setupWidgets(View v){
        mETAccountName = (EditText)v.findViewById(R.id.et_new_account_account);
        v.findViewById(R.id.btn_new_pass_cancel).setOnClickListener(this);
        v.findViewById(R.id.btn_new_pass_acc_save).setOnClickListener(this);
        v.findViewById(R.id.btn_new_pass_acc_generate).setOnClickListener(this);
    }

    private void setupFragment(){
        mPassCreationFrag = new PassCreationFrag();
        getChildFragmentManager().beginTransaction()
                .add(R.id.new_account_container, mPassCreationFrag, "")
                .commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        mApplicationState.resumeLock();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_new_pass_acc_save:
                handleNewAccount();
                break;

            case R.id.btn_new_pass_acc_generate:
                generatePassword();
                break;

            case R.id.btn_new_pass_cancel:
                hideScreen();
                break;
        }
    }

    private void handleNewAccount(){

        if ( !mPassCreationFrag.isPasswordConfirmationValid() ) {
            Toast.makeText(getActivity(), "Password Invalid!", Toast.LENGTH_LONG).show();

        }else if( (mETAccountName.getText().length() == 0) ){
            Toast.makeText(getActivity(), "Account Invalid!", Toast.LENGTH_LONG).show();

        }else{
            AccountSensitiveData accountSensitiveData = //TODO modify this
                    new AccountSensitiveData(new RawData(mETAccountName.getText().toString())
                            ,mPassCreationFrag.getRawPassword());
            hideScreen();
            saveNewAccount(accountSensitiveData);
        }

    }

    public void hideScreen(){
        InputMethodManager imm =
                (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        TransitionSingleton.getInstance().toggleScene();
    }

    private void saveNewAccount(AccountSensitiveData account){
        AccountRecord newAccount = new AccountRecord(account);
        newAccount.save();
    }

    private void generatePassword(){
        PasswordGenerator passwordGenerator = new PasswordGenerator(this);
        ApplicationPreferences pref = ApplicationPreferences.getInstance();
        passwordGenerator.generatePassword(pref.getGeneratedPasswordLength());
    }

    @Override
    public void passwordGeneratorCallBack(RawData password) {
        mPassCreationFrag.setPassword(password);
    }



}
