package com.example.mark.passwordlocker.helpers;

import android.content.Context;
import android.util.Log;

import com.example.mark.passwordmanager.PasswordUtils;
import com.example.mark.passwordmanager.cipher.PasswordCipher;
import com.example.mark.passwordmanager.helpers.EmptyPasswordException;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;

/**
 * Created by mark on 3/12/15.
 * This class is a singleton
 * This class controls the password used to open the application, the main password
 * There is no reference to the password itself, after being checked for authenticity
 * a key is loaded, mKey, and all the security transactions are based on this key
 */

public final class ApplicationPassword extends SharedPrefsActor {
    //private final String PREFERENCES_NAME = "app_pref";
    private final String APP_PASSWORD_KEY = "app_password";
    private final String APP_PASSWORD_HINT = "password_hint";
    private static Context mContext;
    private static ApplicationPassword mInstance;
    private static ApplicationPreferences mPreferences;
    private static boolean mIsPasswordLocked = true;
    private static byte [] mKey;

    private ApplicationPassword(){
        if ( null == mContext){
            throw new NullPointerException(
                    "ApplicationPassword must have an context reference, use setContext");
        }
        lockPassword();
    }

    public static void setContext(Context context){
        mContext = context;
    }

    public static ApplicationPassword getInstance(){
        if ( null == mInstance){
            mInstance = new ApplicationPassword();
        }
        return mInstance;
    }

    private ApplicationPreferences getAppPreferences(){
        if (null == mPreferences){
            mPreferences = ApplicationPreferences.getInstance();
        }
        return mPreferences;
    }

    public void saveApplicationPassword(String password){
        if ( !isPasswordDefined()){
            saveDataToSharedPref(APP_PASSWORD_KEY, encryptBoolean(password) );
        }
    }

    private byte [] generateKeyFromPassword(String password){
        byte [] result = new byte [16];

        try {
            result = PasswordCipher.generateKeyFromPassword(password);
        } catch (EmptyPasswordException e) {
            lockPassword();
        }

        return result;
    }

    public void saveHint(String hint){
        saveDataToSharedPref(APP_PASSWORD_HINT, hint);
    }
    //is defined if a reference to it it is saved on the shared preferences
    public boolean isPasswordDefined(){
        return hasDataOnSharedPref(APP_PASSWORD_KEY);
    }

    //the password itself is never stored, the password its used to encrypt the boolean TRUE
    //and will be considered valid in case of a successful decryption of the valued saved on
    //the shared preferences( encrypted Boolean.TRUE )
    boolean isPasswordValid(String password){
        if ( Boolean.valueOf( decryptBoolean(password) )){
            unlockPasswordAndLoadKey(password);
            return true;
        }else{
            deleteKey();
            lockPassword();
            return false;
        }
    }

    void unlockPasswordAndLoadKey(String password){
        loadKey(password);
        mIsPasswordLocked = false;
    }

    private void loadKey(String password){
        mKey = generateKeyFromPassword(password);
    }

    //The password will be unlocked only if there is still a reference to the key generated by the
    //user password
    void unlockPassword( ){
        if ( null != mKey )  {
            mIsPasswordLocked = false;
        }
    }

    //only the last option (-2) on the preferences settings (Seconds to lock the application) will not
    //require that the key must be deleted
    public void lockPassword(){
        Log.e("Lock Password ", "LOCK PASSWORD");
        mIsPasswordLocked = true;
        if ( getAppPreferences().getSecondsToLockApplication() > -2){
            deleteKey();
        }
    }

    void deleteKey(){
        Log.e("delete key", "delete key");
        mKey = null;
    }

    //only a valid key is loaded into mKey (checked by isPasswordValid())
    //if the key is deleted it will become invalid
    boolean isKeyValid(){
        return (null != mKey);
    }

    boolean isPasswordLocked(){
        return mIsPasswordLocked;
    }


    //uses the user password to encrypt "Boolean.TRUE", and is the result that will be stored
    String encryptBoolean(String password){
        byte [] data = PasswordUtils.stringToBytes(String.valueOf(Boolean.TRUE));
        byte [] key = generateKeyFromPassword(password);
        byte [] iv = generateIV(key);
        byte [] cipherData;
        try {
            cipherData = PasswordCipher.encrypt(data, key, iv);
        } catch (InvalidKeyException e) {
            Log.e("Inside encryptBoolean","Invalid Key " + e.toString());
            return null;
        } catch (BadPaddingException e) {
            Log.e("Inside encryptBoolean", "Invalid Key " + e.toString());
            return null;
        }

        return PasswordUtils.byteToString(cipherData);
    }

    String decryptBoolean(String password){
        if (  password == null || password.isEmpty() ){
            return Boolean.FALSE.toString();
        }
        String encryptedData = getStringFromPreferences(APP_PASSWORD_KEY);
        return decrypt(password, encryptedData);
    }

    //method was split from decryptBoolean for testing
    String decrypt(String password, String encryptData){
        byte [] key = generateKeyFromPassword(password);
        byte [] iv = generateIV(key);
        byte [] data = PasswordUtils.stringToBytes(encryptData);
        byte [] decryptedByte;
        try {
            decryptedByte = PasswordCipher.decrypt(data, key, iv);
        } catch (InvalidKeyException e) {
            Log.e("Application Password", "Inside decrypt,  Invalid Key " + e.toString());
            return Boolean.FALSE.toString();
        } catch (BadPaddingException e) {
            Log.e("Application Password","Inside decrypt Invalid Key " + e.toString());
            return Boolean.FALSE.toString();
        }

        return PasswordUtils.byteToString(decryptedByte);
    }

    private byte [] generateIV(byte [] seed){
        byte [] shaSeed = new byte[16];
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            shaSeed = sha256.digest(seed);
        } catch (NoSuchAlgorithmException e) {
            Log.e("Application Password", "generateIv() " + e.toString());
        }

        return Arrays.copyOfRange(shaSeed, 0 , 16);
    }
    //the key its also used to as key to the database
    byte [] getAppKey(){
        if ( isPasswordLocked()){
            throw new IllegalStateException("Password is locked");
        }

       return mKey.clone();
    }

    byte [] getAppIv(){
        if ( isPasswordLocked()){
            throw new IllegalStateException("Password is locked");
        }

        return generateIV(mKey);
    }

    public String getHint(){
        return getStringFromPreferences(APP_PASSWORD_HINT);
    }

    public boolean hasHint(){
        return hasDataOnSharedPref(APP_PASSWORD_HINT);
    }

    @Override
    protected Context getContext() {
        return mContext;
    }

    @Override
    protected String getPreferencesName() {
        //final String PREFERENCES_NAME = "app_pref";
        return "app_pref";//PREFERENCES_NAME;
    }

}
