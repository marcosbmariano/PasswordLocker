package com.mark.passwordlocker.account;

import android.test.FlakyTest;
import android.test.suitebuilder.annotation.LargeTest;

import com.mark.passwordlocker.PLMainActivityIntrumentationTest;
import com.mark.passwordlocker.helpers.ApplicationPasswordTest;
import com.mark.passwordlocker.helpers.ApplicationState;
import com.example.mark.passwordmanager.PasswordUtils;
import com.example.mark.passwordmanager.cipher.PasswordCipher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mark on 8/18/15.
 *
 */

@LargeTest
public class AccountRecordTest extends PLMainActivityIntrumentationTest {
    private List<AccountRecord> mRecordsList;
    private AccountRecord mAccountRecordNotSaved;
    private final String GOOGLE = "google" ;
    private final String YAHOO = "yahoo";
    private final String FACEBOOK = "facebook";
    private final String LINKEDIN = "linkedin";
    private final String ZYNGA = "Zynga";
    private final String PASSWORD = "123456BC";
    private final String EMPTY_STRING = "";


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getMainActivity();
        ApplicationState.getInstance().isPasswordValid(ApplicationPasswordTest.PASSWORD);
        mRecordsList = new ArrayList<>();
        addAccount(GOOGLE, PASSWORD);
        addAccount(YAHOO, PASSWORD);
        addAccount(FACEBOOK, PASSWORD);
        addAccount(LINKEDIN, PASSWORD);

        //the account record was not saved
        mAccountRecordNotSaved = new AccountRecord(ZYNGA, PASSWORD);
    }
    private void addAccount(String account, String password){
        mRecordsList.add(new AccountRecord(account, password));
    }

    public void testInsertion(){

        for( AccountRecord record: mRecordsList){
            record.save();
        }

        List<AccountRecord> records = AccountRecord.getAllAccounts();

        for ( AccountRecord record: records){
            assertEquals(PASSWORD, record.getAccountPassword());
            assertTrue(checkAccount(record.getAccount()));
        }
    }

    private boolean checkAccount(String account){
        switch (account){
            case GOOGLE:
            case YAHOO:
            case FACEBOOK:
            case LINKEDIN:
                return true;

            default:
                return false;
        }
    }

    public void testInsertionAndDeletion(){
        long id = mAccountRecordNotSaved.getId();
        //id has to be 0 before data is saved
        assertEquals(0, id);

        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccountRecordNotSaved.save();
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        //mAccountRecordNotSaved.save();
        id = mAccountRecordNotSaved.getId();
        assertTrue( id > 0);
    }

    public void testNewAccountInvalidVValues(){

        try{
            new AccountRecord("", "1234");
            fail("should fail, account cannot be empty");
        } catch (IllegalArgumentException e){
            //do nothing
        }

        try{
            new AccountRecord("xxxxx", "");
            fail("should fail, password cannot be empty");
        } catch (IllegalArgumentException e){
            //do nothing
        }
    }

    public void testIsAccountOnDataBase(){
        assertFalse(mAccountRecordNotSaved.isAccountOnDatabase());

        mAccountRecordNotSaved.save();
        assertTrue(mAccountRecordNotSaved.isAccountOnDatabase());

        mAccountRecordNotSaved.deleteAccount();
        assertFalse(mAccountRecordNotSaved.isAccountOnDatabase());
    }


    public void testGetPasswordAndGetAccountNotSaved(){
        unlockApplication();

        //not saved yet
        assertTrue(EMPTY_STRING.equals(mAccountRecordNotSaved.getAccountPassword()));
        assertTrue(EMPTY_STRING.equals(mAccountRecordNotSaved.getAccount()));

        mAccountRecordNotSaved.save();
        assertTrue(PASSWORD.equals(mAccountRecordNotSaved.getAccountPassword()));
        assertTrue(ZYNGA.equals(mAccountRecordNotSaved.getAccount()));
    }

    public void testGetAccountAndPasswordAfterDeletion(){
        //should be empty
        mAccountRecordNotSaved.save();
        mAccountRecordNotSaved.deleteAccount();
        assertTrue(EMPTY_STRING.equals(mAccountRecordNotSaved.getAccountPassword()));
        assertTrue(EMPTY_STRING.equals(mAccountRecordNotSaved.getAccount()));
    }

    public void testGetAccountAndPasswordAppLocked(){
        //should throw a exception
        lockApplication();
        try{
            mAccountRecordNotSaved.getAccountPassword();
            fail("should fail");
        }catch (IllegalStateException e){
            // do nothing
        }

        try{
            mAccountRecordNotSaved.getAccount();
            fail("should fail");
        }catch (IllegalStateException e){
            // do nothing
        }
    }


    // if account is not saved, all account data should be deleted
    public void testDeleteAccountNotYetSaved(){
        unlockApplication();
        mAccountRecordNotSaved.deleteAccount();
        assertEquals(0, mAccountRecordNotSaved.getId());
        assertTrue("get account", EMPTY_STRING
                .equals(mAccountRecordNotSaved.getAccount()));
        assertTrue("get password", EMPTY_STRING
                .equals(mAccountRecordNotSaved.getAccountPassword()));

    }

    public void testDeletionAppLocked(){

        unlockApplication();
        mAccountRecordNotSaved.save();
        lockApplication();
        try {
            mAccountRecordNotSaved.deleteAccount();
            fail("should throw exception");
        } catch (IllegalStateException e){
            //do nothing
        }
        unlockApplication();
        assertTrue(mAccountRecordNotSaved.isAccountOnDatabase());
    }

    public void testGetSaltAndIVNotSavedExceptions(){

        unlockApplication();
        try{
            mAccountRecordNotSaved.getAccountIv();
            fail("getAccountIV Should fail");
        } catch (IllegalStateException e){
            //Do nothing
        }

        try{
            mAccountRecordNotSaved.getAccountSalt();
            fail("getAccountSalt Should fail");
        } catch (IllegalStateException e){
            //Do nothing
        }
    }

    public void testGetSaltAndIVLockAPPExceptions(){

        lockApplication();
        try{
            mAccountRecordNotSaved.getAccountIv();
            fail("getAccountIV Should fail");
        } catch (IllegalStateException e){
            //Do nothing
        }

        try{
            mAccountRecordNotSaved.getAccountSalt();
            fail("getAccountSalt Should fail");
        } catch (IllegalStateException e){
            //Do nothing
        }
    }


    @FlakyTest
    public void testGetAllAccountsAndDeleteAll(){

        List<AccountRecord> mAccountsRecord;
        Iterator<AccountRecord> accounts = mRecordsList.iterator();

        unlockApplication();
        while (accounts.hasNext()){
            accounts.next().save();
        }

        mAccountsRecord = AccountRecord.getAllAccounts();
        int size = mAccountsRecord.size();
        assertTrue( size > 0);

        AccountRecord.deleteAllAccounts();
        mAccountsRecord = AccountRecord.getAllAccounts();
        size = mAccountsRecord.size();
        assertTrue( size == 0);
    }

    private void unlockApplication(){
        ApplicationState.getInstance().isPasswordValid(ApplicationPasswordTest.PASSWORD);
    }

    private void lockApplication(){
        ApplicationState.getInstance().lockApplication();
    }

    public void testEncryptDecryptData(){
        unlockApplication();
        mAccountRecordNotSaved.save();
        String data = "Message123456789&@$#%^!*()";
        byte [] dataInBytes = PasswordUtils.stringToBytes(data);
        byte [] salt = PasswordCipher.generateRandomSalt();
        byte [] key = PasswordCipher.generateRandomKey();
        byte [] iv = PasswordCipher.generateRandomIv();

        String encryptedData = mAccountRecordNotSaved.encrypt(dataInBytes, salt, key, iv );
        String result = mAccountRecordNotSaved
                .getDecryptedValueAsString(encryptedData, salt, key, iv);

        assertEquals(data, result);

        //using another key
        byte [] invalidKey = PasswordCipher.generateRandomKey();

        try{
            mAccountRecordNotSaved
                    .getDecryptedValueAsString(encryptedData, salt, invalidKey, iv);
            fail("should throw NullPointerExceprion");
        } catch (NullPointerException e){
            //do Nothing
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void testEncryptDecryptInvalidInfo(){
        unlockApplication();
        mAccountRecordNotSaved.save();
        byte [] data = PasswordUtils.stringToBytes("garbage");
        byte [] salt = PasswordCipher.generateRandomSalt();
        byte [] key = PasswordCipher.generateRandomKey();
        byte [] iv = PasswordCipher.generateRandomIv();
        byte [] emptyBytes = new byte[0];
        byte [] nullBytes = null;

        //nullData
        tryData( nullBytes, salt, key, iv);
        tryData( data, nullBytes, key, iv);
        tryData( data, salt, nullBytes, iv);
        tryData( data, salt, key, nullBytes);

        //emptyData
        tryData( emptyBytes, salt, key, iv);
        tryData( data, emptyBytes, key, iv);
        tryData( data, salt, emptyBytes, iv);
        tryData( data, salt, key, emptyBytes);


    }

    private void tryData(byte[] data, byte[] salt, byte[] key, byte[] iv){
        try{
            mAccountRecordNotSaved.encrypt(data, salt, key, iv );
            fail("should throw NullPointerExceprion");
        } catch (NullPointerException e){
            //do Nothing
        }
    }

    public void testObservers(){
        MockDatabaseObserver observer = new MockDatabaseObserver();
        AccountRecord.addObservers(observer);

        unlockApplication();
        assertFalse(observer.wasModified());

        mAccountRecordNotSaved.save();
        assertTrue(observer.wasModified());

        assertFalse(observer.wasModified());

        mAccountRecordNotSaved.deleteAccount();
        assertTrue(observer.wasModified());

        assertFalse(observer.wasModified());
    }


    @Override
    protected void tearDown() throws Exception {
        mRecordsList = null;
        mAccountRecordNotSaved = null;
        AccountRecord.deleteAllAccounts();
        lockApplication();
        super.tearDown();
    }

    class MockDatabaseObserver implements AccountRecord.DatabaseObserver{
        private boolean wasModified = false;
        @Override
        public void notifyDataChanged() {
            wasModified = true;
        }

        public boolean wasModified(){
            boolean result = wasModified;
            wasModified = false;
            return result;
        }
    }

}
