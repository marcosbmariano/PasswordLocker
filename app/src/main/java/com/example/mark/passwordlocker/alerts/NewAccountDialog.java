package com.example.mark.passwordlocker.alerts;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mark.passwordlocker.AccountRecord;
import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.fragments.PassCreationFrag;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;
import com.example.mark.passwordlocker.helpers.DatabaseKey;
import com.example.mark.passwordmanager.RawData;
import com.example.mark.passwordmanager.generator.PasswordGenerator;

/**
 * Created by mark on 1/14/15.
 */


//TODO class Reviewd!!!!!!!

public class NewAccountDialog extends DialogFragment
        implements View.OnClickListener, PasswordGenerator.PasswordGeneratorListener{

    private EditText mETAccountName;
    private Button mBtnSavePassword;
    private Button mBtnGeneratePassword;
    private Button mBtnCancel;
    private PassCreationFrag mPassCreationFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_pass_account, container);
        setupWidgets(view);
        setupFragment();

        return view;
    }

    private void setupWidgets(View v){
        mBtnCancel = (Button)v.findViewById(R.id.btn_new_pass_cancel);
        mBtnCancel.setOnClickListener(this);
        mETAccountName = (EditText)v.findViewById(R.id.et_new_account_account);
        mBtnSavePassword = (Button)v.findViewById(R.id.btn_new_pass_acc_save);
        mBtnSavePassword.setOnClickListener(this);
        mBtnGeneratePassword = (Button)v.findViewById(R.id.btn_new_pass_acc_generate);
        mBtnGeneratePassword.setOnClickListener(this);
    }


    private void setupFragment(){
        mPassCreationFrag = new PassCreationFrag();
        getChildFragmentManager().beginTransaction()
                .add(R.id.new_account_container, mPassCreationFrag, "")
                .commit();
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
                dismiss();
                break;
        }
    }

    private void handleNewAccount(){
        RawData account;
        RawData password;

        if ( !mPassCreationFrag.isPasswordConfirmationValid() ) {
            Toast.makeText(getActivity(), "Password Invalid!", Toast.LENGTH_LONG).show();

        }else if( (mETAccountName.getText().length() == 0) ){
            Toast.makeText(getActivity(), "Account Invalid!", Toast.LENGTH_LONG).show();

        }else{
            password = mPassCreationFrag.getRawPassword();
            account = new RawData(mETAccountName.getText().toString());
            dismiss();
            saveNewAccount(account, password);
        }

    }

    private void saveNewAccount(RawData account, RawData password){
        DatabaseKey.setContext(getActivity());
        AccountRecord newAccount = new AccountRecord(account, password);
        newAccount.save();
    }

    private void generatePassword(){
        PasswordGenerator passwordGenerator = new PasswordGenerator(this);
        ApplicationPreferences pref = ApplicationPreferences.getInstance();
        passwordGenerator.generatePassword( pref.getGeneratedPasswordLength() );
    }

    @Override
    public void passwordGeneratorCallBack(RawData password) {
        mPassCreationFrag.setPassword(password);
        displayGeneratedPassword(password);
    }

    private void displayGeneratedPassword(RawData password){
        DisplayGeneratedPass alert = new DisplayGeneratedPass();
        alert = setArguments(alert, password.getDataCharArray());
        alert.show(getActivity().getSupportFragmentManager(),"");
    }

    private DisplayGeneratedPass setArguments( DisplayGeneratedPass frag, char [] arguments){
        Bundle args = new Bundle();
        args.putCharArray(
                DisplayGeneratedPass.GENERATED_PASSWORD, arguments);
        frag.setArguments(args);

        return frag;
    }
}
