package com.example.mark.passwordlocker.helpers;

import android.content.Context;
import android.util.Log;

import com.example.mark.passwordmanager.PasswordUtils;
import com.example.mark.passwordmanager.cipher.PasswordCipher;

import java.util.Arrays;

/**
 * Created by mark on 3/12/15.
 */

public final class ApplicationPassword extends SharedPrefsActor { //TODO reviewed
    private final String PREFERENCES_NAME = "app_pref";
    private final String APP_PASSWORD_KEY = "app_password";
    private final String APP_PASSWORD_HINT = "password_hint";
    private static Context mContext;
    private static ApplicationPassword mInstance;
    private static ApplicationPreferences mPreferences;
    private static boolean mIsPasswordLocked;
    private static byte [] mKey;
    private int mSecondsToLock = 0;


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

    private int getSecondsToLockApp(){
        if ( mSecondsToLock == 0){
            mSecondsToLock = getAppPreferences().getSecondsToLockApplication();
        }
        return mSecondsToLock;
    }

    public boolean isKeyValid(){
        return null != mKey;
    }

    public boolean isPasswordLocked(){
        return mIsPasswordLocked;
    }

    //todo move to applicationState
    public void unlockPassword( ){
        if ( null != mKey )  {
            mIsPasswordLocked = false;           ;
            Log.e("Application Password", "Unlock Password!! from unlocked");
        }
    }

    public void lockPassword(){
        mIsPasswordLocked = true;
        if ( getSecondsToLockApp() == -2){
            deleteKey();
        }
    }

    private void deleteKey(){
        mKey = null;
        Log.e("Application Password", "Key was deleted!!");
    }


    public void saveApplicationPassword(String password){
        saveDataToSharedPref(APP_PASSWORD_KEY, encryptBoolean(password) );
    }

    public void deleteApplicationPassword(){
        deleteDataFromSharedPref(APP_PASSWORD_KEY);
    }

    private byte [] generateKeyFromPassword(String password){
        if (null == mKey){
            mKey = PasswordCipher.generateKeyFromPassword(password);
        }
        return mKey;
    }

    public void saveHint(String hint){
        saveDataToSharedPref(APP_PASSWORD_HINT, hint);
    }

    public boolean isPasswordDefined(){
        return hasDataOnSharedPref(APP_PASSWORD_KEY);
    }

    public boolean checkPassword(String password){
        if ( Boolean.valueOf( decryptBoolean(password) )){
            unlockPassword();
            return true;
        }else{
            lockPassword();
            return false;
        }
    }
    private String encryptBoolean(String password){
        byte [] data = PasswordUtils.stringToBytes( String.valueOf(Boolean.TRUE) );
        byte [] key = generateKeyFromPassword(password);
        byte [] iv = generateIV(key);
        byte [] cipherData = PasswordCipher.encrypt(data, key, iv);

        return PasswordUtils.byteToString(cipherData);
    }

    private String decryptBoolean(String password){
        String cipheredPasswordCheck = getStringFromPreferences(APP_PASSWORD_KEY);
        byte [] key = generateKeyFromPassword(password);
        byte [] iv = generateIV(key);
        byte [] data = PasswordUtils.stringToBytes(cipheredPasswordCheck);
        byte [] decryptedByte = PasswordCipher.decrypt(data, key, iv);

        return PasswordUtils.byteToString(decryptedByte);
    }

    private byte [] generateIV(byte [] key){
        return Arrays.copyOfRange(key, 0, 16);
    }

    public byte [] getAppKey(){
        return mKey.clone();
    }

    public byte [] getAppIv(){
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
        return PREFERENCES_NAME;
    }

}
