package com.example.mark.passwordlocker;

import com.example.mark.passwordmanager.RawData;

/**
 * Created by mark on 3/17/15.
 */
public class AccountSensitiveData {
    private final RawData mPassword;
    private final RawData mAccount;
    //private final RawData mSalt;
    //private final RawData mIv;

    public AccountSensitiveData(RawData account, RawData password){//}, RawData salt, RawData iv){
        if (account.isEmpty() || password.isEmpty()){
            throw new IllegalArgumentException("Account and password cannot be empty");
        }
        mAccount = account;
        mPassword = password;
        //mSalt = salt;
        //mIv = iv;
    }

    public byte [] getPassword(){
        return mPassword.getDataByteArray();
    }

    public byte [] getAccount(){
        return mAccount.getDataByteArray();
    }

//    public byte [] getmIv() {
//        return mIv.getDataByteArray();
//    }
//
//    public byte [] getmSalt() {
//        return mSalt.getDataByteArray();
//    }

}
