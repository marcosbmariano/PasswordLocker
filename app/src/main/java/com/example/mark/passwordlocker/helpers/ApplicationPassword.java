package com.example.mark.passwordlocker.helpers;

import android.content.Context;
import android.util.Log;

import com.example.mark.passwordmanager.PasswordUtils;
import com.example.mark.passwordmanager.cipher.PasswordCipher;

/**
 * Created by mark on 3/12/15.
 */

public final class ApplicationPassword extends SharedPrefsActor { //TODO reviewed
    private final String PREFERENCES_NAME = "app_pref";
    private final String APP_PASSWORD_NAME = "app_password";
    private final String APP_PASSWORD_HINT = "password_kint";
    private static boolean isPasswordValid;
    private static Context mContext;
    private static ApplicationPassword mInstance;

    private ApplicationPassword(){
        if ( null == mContext){
            throw new NullPointerException(
                    "ApplicationPassword must have an context reference, use setContext");
        }
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

    public void saveApplicationPassword(String password){
        saveDataToSharedPref(APP_PASSWORD_NAME, encryptBoolean(password) );
    }

    private String encryptBoolean(String password){
        byte [] data = PasswordUtils.stringToBytes( String.valueOf(Boolean.TRUE) );
        byte [] keyAndIv = generateKeyFromPassword(password);
        byte [] cipherData = PasswordCipher.encrypt(data, keyAndIv, keyAndIv);

        return PasswordUtils.byteToString(cipherData);
    }

    private byte [] generateKeyFromPassword(String input){
        byte [] result = new byte [0];

        if ( !input.isEmpty()){
            result = PasswordUtils.stringToBytes( ensure16Char(input) );
        }
        return result;
    }

    private String ensure16Char(String input){
        int keySize = 16;
        StringBuilder builder = new StringBuilder( 2 * keySize);

        while (builder.length() < keySize){
            builder.append(input);
        }
        return builder.substring(0,keySize);
    }

    public void saveHint(String hint){
        saveDataToSharedPref(APP_PASSWORD_HINT, hint);
    }

    public boolean isPasswordDefined(){
        return hasDataOnSharedPref(APP_PASSWORD_NAME);
    }

    public boolean checkPassword(String password){

        isPasswordValid = Boolean.valueOf( decryptBoolean(password) );
        return isPasswordValid;
    }

    private String decryptBoolean(String password){
        String cipheredPasswordCheck = getStringFromPreferences(APP_PASSWORD_NAME);
        byte [] keyAndIv = generateKeyFromPassword(password);
        byte [] data = PasswordUtils.stringToBytes(cipheredPasswordCheck);
        byte [] decryptedByte = PasswordCipher.decrypt(data, keyAndIv, keyAndIv);

        return PasswordUtils.byteToString(decryptedByte );
    }

    public byte [] getAppKey(){
        return PasswordUtils.stringToBytes(
                getStringFromPreferences(APP_PASSWORD_NAME));
    }

    public boolean isPasswordValid(){
        return isPasswordValid;
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
