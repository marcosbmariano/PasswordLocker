package com.example.mark.passwordlocker;

import com.example.mark.passwordmanager.RawData;

/**
 * Created by mark on 3/17/15.
 */
public class Account {
    private final RawData mPassword;
    private final RawData mAccount;

    public Account(RawData account, RawData password){
        if (account.isEmpty() || password.isEmpty()){
            throw new IllegalArgumentException("Account and password cannot be empty");
        }
        mAccount = account;
        mPassword = password;
    }

    public byte [] getPassword(){
        return mPassword.getDataByteArray();
    }

    public byte [] getAccount(){
        return mAccount.getDataByteArray();
    }

}
