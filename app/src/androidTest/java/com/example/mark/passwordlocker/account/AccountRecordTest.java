package com.example.mark.passwordlocker.account;

import android.test.suitebuilder.annotation.LargeTest;

import com.example.mark.passwordmanager.RawData;

import junit.framework.TestCase;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 8/18/15.
 *
 */

@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountRecordTest extends TestCase {
    private List<AccountRecord> mRecordsList;
    private AccountRecord mAccountRecordNotSaved;
    private final String GOOGLE = "google" ;
    private final String YAHOO = "yahoo";
    private final String FACEBOOK = "facebook";
    private final String LINKEDIN = "linkedin";
    private final String PASSWORD = "123456BC";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRecordsList = new ArrayList<>();
        addAccount(GOOGLE, PASSWORD);
        addAccount(YAHOO, PASSWORD);
        addAccount(FACEBOOK, PASSWORD);
        addAccount(LINKEDIN, PASSWORD);

        //the account record was not saved
        mAccountRecordNotSaved = new AccountRecord("Zynga", PASSWORD);

    }
    private void addAccount(String account, String password){
        mRecordsList.add(new AccountRecord(new AccountSensitiveData(
                new RawData(account), new RawData(password))));

    }

    public void testGetId(){
        long id = mAccountRecordNotSaved.getId();
    }


    // nothing should happen if the AccountRecord is not savedYet
    public void testDeleteAccountNotYetSaved(){
        mAccountRecordNotSaved.deleteAccount();
    }

    public void testDeleteAccount(){
       // mAccountRecordNotSaved.save();

    }



    //deleteAccount(){
    //not savedYet



    //deleteAccount( long id)
    //invalid id
    // out of bounds id
    // negative id




    //addListener()
    // add a null

    //notifyListener(()
    //add a listener and check if is notified


    //encrypt
    //any of each as null value
    // invalid value for any of each


    //getAccountPassword()
    //not save yet
    //app locked

    //getAccount()
    //check if account matches
    //check if was not saved yet
    //check if the app is locked

    //getDecryptedValue()
    //any of each argument as invalid, null and empty
    // check if matches

    //getDecryptedValueAsString()

    //getAccountIV()
    //check if is not saved

    //getAccountSalt(){
    //check if is not saved

    public void testEncryptMetaData(){


        //byte []





    }
    //encryptMetaData()
    // null data
    //enpty data
    //valida data

    //getDecryptedMetadata
    //emptyString
    //null string
    //wrong string





    //getAccountSalt(()


    @Override
    protected void tearDown() throws Exception {
        mRecordsList = null;
        mAccountRecordNotSaved = null;
        super.tearDown();
    }
}
