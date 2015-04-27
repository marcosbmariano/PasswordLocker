package com.example.mark.passwordlocker.helpers;

import android.content.Context;

import com.example.mark.passwordmanager.PasswordUtils;
import com.example.mark.passwordmanager.cipher.PasswordCipher;

/**
 * Created by mark on 3/12/15.
 */
public final class DatabaseKey extends SharedPrefsActor {
    private final String PREFERENCES_NAME = "database_pref";
    private final String KEY_NAME = "database_key";
    private final String IV_NAME = "database_iv";
    private byte [] mKey;
    private byte [] mIv;
    private Context mContext;
    private ApplicationPassword mApplicationPassword;

    public DatabaseKey(Context context){
        if ( null == context){
            throw new NullPointerException(
                    "DatabaseKey must have a valid Context reference!");
        }else{
            mContext = context;
            mApplicationPassword = ApplicationPassword.getInstance();
            createKeyIfNoKey();
            createIvIfNoIv();
        }
    }

    private void createKeyIfNoKey(){
        byte [] key = PasswordCipher.generateKey();
        createData(getKeyName(), key );
    }

    private void createIvIfNoIv(){
        byte [] iv = PasswordCipher.generateIv();
        createData(getIvName(), iv);
    }

    private void createData(String dataName, byte [] data){
        if (!hasDataOnSharedPref( dataName )){
            saveDataToSharedPref(
                    dataName,
                    encryptData(data, mApplicationPassword.getAppKey()));
        }
    }

    private String encryptData(byte[] data, byte[] keyAndIv){
        return PasswordUtils.byteToString(
                PasswordCipher.encrypt(data, keyAndIv, keyAndIv));
    }

    public byte [] getKey(){
        if ( null == mKey ){
            mKey = getDecryptedData(getKeyName());
        }
        return mKey;
    }

    public byte [] getIv(){
        if ( null == mIv){
            mIv = getDecryptedData(getIvName());
        }
        return mIv;
    }

    private byte [] getDecryptedData(String dataName){
        byte [] result = null;

        if (!hasDataOnSharedPref(dataName) || mApplicationPassword.isPasswordValid() ){

            String encryptedKeyString = getStringFromPreferences(dataName);

            result = PasswordCipher.decrypt(
                        PasswordUtils.stringToBytes( encryptedKeyString ),
                        mApplicationPassword.getAppKey(), mApplicationPassword.getAppKey());
        }
        if ( null == result){
            result = getFakeKey();
        }

        return result;
    }

    private byte [] getFakeKey(){
        return new byte [] { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
                ' ', ' ', ' ', ' ', ' ', ' ', ' ',};
    }

    @Override
    protected Context getContext() {
        return mContext;
    }

    @Override
    protected String getPreferencesName() {
        return PREFERENCES_NAME;
    }

    protected String getKeyName() {
        return KEY_NAME;
    }

    protected String getIvName() {
        return IV_NAME;
    }

}
