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
    private byte [] mSalt;
    private static Context mContext;
    private static ApplicationPassword mApplicationPassword;
    private static DatabaseKey mDatabaseKeyInstance;


    public DatabaseKey(){
        if ( null == mContext){
            throw new NullPointerException(
                    "DatabaseKey must have a valid Context reference before being used!");
        }else{
            mApplicationPassword = ApplicationPassword.getInstance();
        }
    }

    public static void setContext( Context context){
        mContext = context;
    }

    public static DatabaseKey getInstance(){
        if ( null == mDatabaseKeyInstance){
            mDatabaseKeyInstance = new DatabaseKey();
        }
        return mDatabaseKeyInstance;
    }


    private String encryptMetadata(byte[] data){

        if ( !isDatabaseAcessible()){
            throw new SecurityException("Application is locked!");
        }

        return PasswordUtils.byteToString(
                PasswordCipher.encrypt(data, mApplicationPassword.getAppKey(),
                        mApplicationPassword.getAppKey()));
    }

    private boolean isDatabaseAcessible(){
        return !mApplicationPassword.isApplicationLocked();
    }

    public byte [] getKey(){
        if ( mApplicationPassword.isApplicationLocked() ){
            throw new IllegalStateException("The key cannot be used if the Application is locked!!");
        }
        return mApplicationPassword.getAppKey();
    }

    public byte [] getIv(){
        if ( mApplicationPassword.isApplicationLocked() ){
            throw new IllegalStateException("The key cannot be used if the Application is locked!!");
        }
        return mApplicationPassword.getAppIv();

    }

    public byte [] getSalt(){
        if ( mApplicationPassword.isApplicationLocked() ){
            throw new IllegalStateException("The key cannot be used if the Application is locked!!");
        }
        return mApplicationPassword.getAppKey();
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
