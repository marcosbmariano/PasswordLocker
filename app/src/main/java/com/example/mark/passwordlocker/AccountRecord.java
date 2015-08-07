package com.example.mark.passwordlocker;


import android.util.Log;

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

/**
 * Created by mark on 3/11/15.
 */


//TODO must be tested

@Table(name = "account_record")
public class AccountRecord extends Model {

    private AccountSensitiveData mAccount;
    private static List<DatabaseListener> mListeners = new ArrayList<>();
    private static DatabaseKey mDatabaseKey;

    @Column(name = "ciphedPassword")
    private String mCiphedPassword;

    @Column(name= "ciphedAccount")
    private String mCiphedAccount;

    @Column(name = "ciphedSalt")
    protected String mCiphedSalt;

    @Column(name = "chipedIv")
    private String mCiphedIv;

    private String mTempAccount;
    private byte [] mTempIv;
    private byte [] mTempSalt;



    public AccountRecord(){
    //this is necessary for the database automation
    }

    public AccountRecord(RawData account, RawData password){
        mAccount = new AccountSensitiveData(account, password);
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
        mCiphedAccount = encrypt( mAccount.getAccount(), salt, getDatabaseKey().getKey(), iv);
        mCiphedPassword = encrypt( mAccount.getPassword(), salt, getDatabaseKey().getKey() , iv);

        //encrypt the salt and iv used using the application password,
        // key and iv through databaseKey
        mCiphedSalt = encrypt(salt, getDatabaseKey().getSalt(),
                getDatabaseKey().getKey(), getDatabaseKey().getIv());
        mCiphedIv = encrypt(iv, getDatabaseKey().getSalt(),
                getDatabaseKey().getKey(), getDatabaseKey().getIv());

        super.save();
        notifyListeners();
    }

    private DatabaseKey getDatabaseKey(){
        if ( null == mDatabaseKey){
            mDatabaseKey = DatabaseKey.getInstance();
        }
        return mDatabaseKey;
    }

    public void deleteAccount(){
        delete();
        notifyListeners();
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

    private String encrypt(byte [] data, byte [] salt, byte [] key, byte [] iv){
        String result = null;
        try {
            PasswordUtils.byteToString(
                   PasswordCipher.encryptWithSalt(data, salt, key, iv));
        } catch (InvalidKeyException e) {
            Log.e("Inside encrypt", " " + e.toString());
            return null;
        }
        return result;
    }

    public String getAccountPassword(){
        return getDecryptedValueAsString(mCiphedPassword,
                getAccountSalt(), getDatabaseKey().getKey(), getAccountIv());
    }

    public String getAccount(){
        if ( null == mTempAccount){
            mTempAccount = getDecryptedValueAsString(mCiphedAccount,
                    getAccountSalt(), getDatabaseKey().getKey(), getAccountIv());
        }
        return mTempAccount;
    }

    private String getDecryptedValueAsString(String data, byte[] salt, byte[] key, byte[] iv){
        return PasswordUtils.byteToString(
                getDecryptedValue(data,salt,key,iv));
    }

    private byte [] getDecryptedValue(String data, byte[] salt, byte[] key, byte[] iv){
        byte [] result  = {' '};

        if (null != data) {
            try {
                result = PasswordCipher.decryptWithSalt(data, salt, key, iv);
            } catch (InvalidKeyException e) {
                Log.e("In getDrecyptedValue", "Invalid key " + e.toString());
            }
        }
        return result;
    }

    private byte [] getAccountIv(){
        if ( null == mTempIv){
            mTempIv = geDecryptedtMetadata(mCiphedIv);
        }
        return mTempIv;
    }

    private byte [] getAccountSalt(){
        if ( null == mTempSalt){
            mTempSalt = geDecryptedtMetadata(mCiphedSalt);
        }
        return mTempSalt;
    }

    private byte [] geDecryptedtMetadata(String ciphedValue){
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
