package com.example.mark.passwordlocker;


import android.util.Log;

import com.example.mark.passwordlocker.helpers.DatabaseKey;
import com.example.mark.passwordmanager.PasswordUtils;
import com.example.mark.passwordmanager.RawData;
import com.example.mark.passwordmanager.cipher.PasswordCipher;
import com.marcos.autodatabases.annotations.Column;
import com.marcos.autodatabases.annotations.Table;
import com.marcos.autodatabases.models.Model;
import com.marcos.autodatabases.sql.Delete;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 3/11/15.
 */

//TODO reviewed!!!


@Table(name = "account_record")
public class AccountRecord extends Model {

    private Account mAccount;
    private static List<DatabaseListener> mListeners = new ArrayList<>();

    @Column(name = "cipherPassword")
    private String cipherPassword;

    @Column(name= "cipherAccount")
    private String cipherAccount;

    public AccountRecord(){
    //this is necessary for the database automation
    }

    public AccountRecord(RawData account, RawData password){
        mAccount = new Account(account, password);
    }

    public static List<AccountRecord> getAllAccounts(){
        List<Model> models = Model.getModels(AccountRecord.class);
        List<AccountRecord> accounts = new ArrayList<>(models.size());

        for(Model model :models){
            accounts.add((AccountRecord)model);
        }
        return accounts;
    }

    public void save(DatabaseKey key){
        cipherAccount = encrypt( mAccount.getAccount(), key);
        cipherPassword = encrypt( mAccount.getPassword(), key);
        super.save();
        notifyListeners();
    }

    public void deleteAccount(){
        delete();
        notifyListeners();
    }

    public static void deleteAccount(long id){ //TODO test this
        //Delete.from(AccountRecord.class).whereId(id).execute();
        delete(AccountRecord.class, id);
        notifyListeners();
    }

    private static void notifyListeners(){
        for ( DatabaseListener list : mListeners){
            list.notifyDataChanged();
        }
    }

    private String encrypt(byte [] data, DatabaseKey key){
        return PasswordUtils.byteToString(
                PasswordCipher.encrypt(data, key.getKey(),key.getIv() ));
    }

    public String getAccountPassword(DatabaseKey key){
        return getDecryptedValue( cipherPassword, key);
    }

    public String getAccount(DatabaseKey key){
        return getDecryptedValue(cipherAccount, key);
    }

    private String getDecryptedValue( String value, DatabaseKey key){
        byte [] result  = {' '};

        if (null != value) {
            result = PasswordCipher.decrypt(
                        PasswordUtils.stringToBytes(value),
                        key.getKey(),
                        key.getIv());
        }
        return PasswordUtils.byteToString(result);
    }

    public static void addListener(DatabaseListener listener){
       mListeners.add(listener);
    }

    public interface DatabaseListener{
        public void notifyDataChanged();
    }

}
