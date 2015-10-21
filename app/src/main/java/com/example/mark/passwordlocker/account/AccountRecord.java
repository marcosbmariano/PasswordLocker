package com.example.mark.passwordlocker.account;


import android.util.Log;

import com.example.mark.passwordlocker.helpers.ApplicationState;
import com.example.mark.passwordlocker.helpers.DatabaseKey;
import com.example.mark.passwordmanager.PasswordUtils;
import com.example.mark.passwordmanager.RawData;
import com.example.mark.passwordmanager.cipher.PasswordCipher;
import com.marcos.autodatabases.annotations.Column;
import com.marcos.autodatabases.annotations.Table;
import com.marcos.autodatabases.models.Model;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.crypto.BadPaddingException;

/**
 * Created by mark on 3/11/15.
 */


//TODO must be tested

@Table(name = "account_record")
public final class AccountRecord extends Model {

    private AccountSensitiveData mAccountSensitiveData;
    private static final HashSet<DatabaseObserver> mObservers = new HashSet<>();
    private static DatabaseKey mDatabaseKey;

    @Column(name = "ciphedPassword", notNull = true)
    private String mCipherPassword;

    @Column(name= "ciphedAccount", notNull = true)
    private String mCipherAccount;

    @Column(name = "ciphedSalt", notNull = true)
    private String mCipherSalt;

    @Column(name = "chipedIv", notNull = true)
    private String mCipherIv;

    private String mTempAccount;
    private byte [] mTempIv;
    private byte [] mTempSalt;
    private boolean isOnDatabase;



    public AccountRecord(){
    //this is necessary for the database automation
        isOnDatabase = true;
    }

    public AccountRecord(AccountSensitiveData account){
        mAccountSensitiveData = account;
    }

    AccountRecord (String account, String password){
        mAccountSensitiveData = new AccountSensitiveData(
                new RawData(account), new RawData(password));
    }


    public static List<AccountRecord> getAllAccounts(){
        List<Model> models = Model.getModels(AccountRecord.class);
        List<AccountRecord> accounts = new ArrayList<>(models.size());

        for(Model model :models){
            accounts.add((AccountRecord)model);
        }
        return accounts;
    }


    //TODO implement DeleteAllAcounts, even if its locked, so user can delete all of
    //its accounts remotely
    public static void deleteAllAccounts(){ //TODO implement this on Model class
        List<AccountRecord> accountsList = getAllAccounts();
        Iterator <AccountRecord>accounts = accountsList.iterator();
        while( accounts.hasNext()){
            accounts.next().deleteAccount();
        }
    }

    boolean isAccountOnDatabase(){
        return isOnDatabase;
    }

    private DatabaseKey getDatabaseKey(){
        if ( null == mDatabaseKey){
            mDatabaseKey = DatabaseKey.getInstance();
        }
        return mDatabaseKey;
    }

    public void deleteAccount(){
        thrownExceptionIfLocked();

        if (isAccountOnDatabase()){
            delete();
            notifyObservers();
        }
        cleanRecord();
    }

    private void cleanRecord(){
        mAccountSensitiveData = null;
        mCipherPassword = null;
        mCipherAccount = null;
        mCipherSalt = null;
        mCipherIv = null;
        mTempAccount = null;
        mTempIv = null;
        mTempSalt = null;
        isOnDatabase = false;
    }

    public static void deleteAccount(long id){
        delete(AccountRecord.class, id);
        notifyObservers();
    }

    private static void notifyObservers(){
        for ( DatabaseObserver list : mObservers){
            list.notifyDataChanged();
        }
    }

    public String getAccountPassword(){
        thrownExceptionIfLocked();

        if (isAccountOnDatabase() ) {
            return getDecryptedValueAsString(mCipherPassword,
                    getAccountSalt(), getDatabaseKey().getKey(), getAccountIv());
        }
        return "";
    }

    public String getAccount(){
        thrownExceptionIfLocked();

        if ( isAccountOnDatabase() ){
            if ( null == mTempAccount){
                mTempAccount = getDecryptedValueAsString(mCipherAccount,
                        getAccountSalt(), getDatabaseKey().getKey(), getAccountIv());
            }
            return mTempAccount;
        }
        return "";
    }

    private void thrownExceptionIfLocked(){
        if( isApplicationLocked() ){
            throw new IllegalStateException("No information about accounts can be retrieved" +
                    " if the application is locked!");
        }
    }

    private boolean isApplicationLocked(){
        return ApplicationState.getInstance().isApplicationLocked();
    }

    public void save(){

        byte [] iv = PasswordCipher.generateRandomIv();
        byte [] salt = PasswordCipher.generateRandomSalt();

        // encrypt the account using a randomly created iv and salt
        mCipherAccount = encrypt( mAccountSensitiveData.getAccount(), salt,
                getDatabaseKey().getKey(), iv);
        mCipherPassword = encrypt( mAccountSensitiveData.getPassword(), salt,
                getDatabaseKey().getKey() , iv);

        //encrypt the salt and iv used using the application password,
        // key and iv through databaseKey
        mCipherSalt = encryptMetaData(salt);
        mCipherIv = encryptMetaData(iv);

        super.save();
        isOnDatabase = true;
        notifyObservers();
    }

    //this is used to encrypt the salt and Iv that are unique for each account
    String encryptMetaData( byte [] data){
        return encrypt(data, getDatabaseKey().getSalt(),
                getDatabaseKey().getKey(), getDatabaseKey().getIv());
    }

    //none of the arguments should not be null, must be checked before encrypt being called
    String encrypt(byte [] data, byte [] salt, byte [] key, byte [] iv){
        String result;

        checkData(data, salt, key, iv);

        try {
            result = PasswordUtils.byteToString(
                    PasswordCipher.encryptWithSalt(data, salt, key, iv));
        } catch (InvalidKeyException e) {
            Log.e("Inside encrypt", " " + e.toString());
            return null;
        } catch (BadPaddingException e) {
            Log.e("Inside encrypt", " " + e.toString());
            return null;
        }
        return result;
    }

    String getDecryptedValueAsString(String data, byte[] salt, byte[] key, byte[] iv){
        return PasswordUtils.byteToString(
                getDecryptedValue(data,salt,key,iv));
    }

    byte [] getDecryptedValue(String data, byte[] salt, byte[] key, byte[] iv) {
        byte[] result = null;

        checkData(data, salt, key, iv);

        try {
            result = PasswordCipher.decryptWithSalt(data, salt, key, iv);
        } catch (InvalidKeyException e) {
            Log.e("AccountRecord","In getDecryptedValue(), Invalid key " + e.toString());
        } catch (BadPaddingException e) {
            Log.e("AccountRecord","In getDecryptedValue(), Invalid key " + e.toString());
        }

        return result;
    }

    private void checkData(String data, byte[] salt, byte[] key, byte[] iv){
        if ( null == data || data.isEmpty()){
            throw new NullPointerException();
        }
        checkEachByteArray(salt, key, iv);
    }

    private void checkData(byte [] data, byte[] salt, byte[] key, byte[] iv){
        checkEachByteArray(data, salt, key, iv);
    }

    private void checkEachByteArray(byte [] ... infos){
        for ( byte [] info : infos ){
            if ( null == info || info.length == 0){
                throw new NullPointerException();
            }
        }
    }



    byte [] getAccountIv(){
        thrownExceptionIfLocked();
        throwExceptionIfNotOnDatabase();

        if ( null == mTempIv){
            mTempIv = getDecryptedMetadata(mCipherIv);
        }
        return mTempIv;
    }

    byte [] getAccountSalt(){
        thrownExceptionIfLocked();
        throwExceptionIfNotOnDatabase();

        if ( null == mTempSalt){
            mTempSalt = getDecryptedMetadata(mCipherSalt);
        }
        return mTempSalt;
    }

    private void throwExceptionIfNotOnDatabase(){
        if( !isAccountOnDatabase() ){
            throw new IllegalStateException("No information about accounts can be retrieved" +
                    " if the application is locked!");
        }
    }

    byte [] getDecryptedMetadata(String cipherValue){
        return getDecryptedValue(cipherValue,
                getDatabaseKey().getSalt(), getDatabaseKey().getKey(), getDatabaseKey().getIv());
    }

    public static void addObservers(DatabaseObserver observer){
       mObservers.add(observer);
    }

    public interface DatabaseObserver {
        void notifyDataChanged();
    }

}
