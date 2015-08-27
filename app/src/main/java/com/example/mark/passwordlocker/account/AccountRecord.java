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
import java.util.List;

import javax.crypto.BadPaddingException;

/**
 * Created by mark on 3/11/15.
 */


//TODO must be tested

@Table(name = "account_record")
public final class AccountRecord extends Model {

    private AccountSensitiveData mAccountSensitiveData;
    private static List<DatabaseListener> mListeners = new ArrayList<>();
    private static DatabaseKey mDatabaseKey;
    private static ApplicationState mAppState;

    @Column(name = "ciphedPassword", notNull = true)
    private String mCiphedPassword;

    @Column(name= "ciphedAccount", notNull = true)
    private String mCiphedAccount;

    @Column(name = "ciphedSalt", notNull = true)
    protected String mCiphedSalt;

    @Column(name = "chipedIv", notNull = true)
    private String mCiphedIv;

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

    public void save(){

        byte [] iv = PasswordCipher.generateRandomIv();
        byte [] salt = PasswordCipher.generateRandomSalt();

        // encrypt the account using a randomly created iv and salt
        mCiphedAccount = encrypt( mAccountSensitiveData.getAccount(), salt,
                getDatabaseKey().getKey(), iv);
        mCiphedPassword = encrypt( mAccountSensitiveData.getPassword(), salt,
                getDatabaseKey().getKey() , iv);

        //encrypt the salt and iv used using the application password,
        // key and iv through databaseKey
        mCiphedSalt = encryptMetaData(salt);
        mCiphedIv = encryptMetaData(iv);

        super.save();
        isOnDatabase = true;
        notifyListeners();
    }

    boolean isAccountOnDatabase(){
        return isOnDatabase;
    }


    String encryptMetaData( byte [] data){
        return encrypt(data, getDatabaseKey().getSalt(),
                getDatabaseKey().getKey(), getDatabaseKey().getIv());
    }

    private DatabaseKey getDatabaseKey(){
        if ( null == mDatabaseKey){
            mDatabaseKey = DatabaseKey.getInstance();
        }
        return mDatabaseKey;
    }

    public void deleteAccount(){
        if (isAccountOnDatabase()){
            delete();
            notifyListeners();
        }
    }

    public static void deleteAccount(long id){
        delete(AccountRecord.class, id);
        notifyListeners();
    }

    private static void notifyListeners(){
        for ( DatabaseListener list : mListeners){
            list.notifyDataChanged();
        }
    }
    //data, salt should not be null, must be checked before encrypt being called
    String encrypt(byte [] data, byte [] salt, byte [] key, byte [] iv){
        String result;

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

    public String getAccountPassword(){
        if (!isApplicationLocked()) {
            return getDecryptedValueAsString(mCiphedPassword,
                    getAccountSalt(), getDatabaseKey().getKey(), getAccountIv());
        }
        return "";
    }

    public String getAccount(){
        if ( !isApplicationLocked()){
            if ( null == mTempAccount){
                mTempAccount = getDecryptedValueAsString(mCiphedAccount,
                        getAccountSalt(), getDatabaseKey().getKey(), getAccountIv());
            }
            return mTempAccount;
        }
        return "";
    }
    private boolean isApplicationLocked(){
        return ApplicationState.getInstance().isApplicationLocked();
    }

    String getDecryptedValueAsString(String data, byte[] salt, byte[] key, byte[] iv){
        return PasswordUtils.byteToString(
                getDecryptedValue(data,salt,key,iv));
    }

    byte [] getDecryptedValue(String data, byte[] salt, byte[] key, byte[] iv) {
        byte[] result = null;

        if (null == data) {
            throw new NullPointerException();
        }
        try {
            result = PasswordCipher.decryptWithSalt(data, salt, key, iv);
        } catch (InvalidKeyException e) {
            Log.e("AccountRecord","In getDrecyptedValue(), Invalid key " + e.toString());
        } catch (BadPaddingException e) {
            Log.e("AccountRecord","In getDrecyptedValue(), Invalid key " + e.toString());
        }

        return result;
    }


    byte [] getAccountIv(){
        if ( null == mTempIv){
            mTempIv = geDecryptedtMetadata(mCiphedIv);
        }
        return mTempIv;
    }

    byte [] getAccountSalt(){
        if ( null == mTempSalt){
            mTempSalt = geDecryptedtMetadata(mCiphedSalt);
        }
        return mTempSalt;
    }

    byte [] geDecryptedtMetadata(String ciphedValue){
        return getDecryptedValue(ciphedValue,
                getDatabaseKey().getSalt(), getDatabaseKey().getKey(), getDatabaseKey().getIv());
    }

    public static void addListener(DatabaseListener listener){
       mListeners.add(listener);
    }

    public interface DatabaseListener{
        void notifyDataChanged();
    }

}
