package com.example.mark.passwordlocker.helpers;

import android.content.Context;
import android.util.Log;

import com.example.mark.passwordmanager.PasswordUtils;
import com.example.mark.passwordmanager.cipher.PasswordCipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mark on 3/12/15.
 */

public final class ApplicationPassword extends SharedPrefsActor implements
        Counter.CounterCallBack, ApplicationPreferences.PreferencesSecondsToLockListener { //TODO reviewed
    private final String PREFERENCES_NAME = "app_pref";
    private final String APP_PASSWORD_KEY = "app_password";
    private final String APP_PASSWORD_HINT = "password_hint";
    private static Context mContext;
    private static int THRESHOLD_TO_LOCK = -1;
    private static ApplicationPassword mInstance;
    private static ApplicationPreferences mPreferences;
    private static boolean mIsAppLocked;
    private static byte [] mKey;
    private static List<ApplicationPasswordListener> mListeners;
    private int mSecondsToLock = 0;
    private Counter mCounter;

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

    public void addListener(ApplicationPasswordListener listener){
        if ( null == mListeners){
            mListeners = new ArrayList<>();
        }
        mListeners.add(listener);
    }

    private void updateListeners(){
        Log.e("Application Password", "updating Listeners!!");
        if (null != mListeners){
            for ( ApplicationPasswordListener listener : mListeners){
                if ( isApplicationLocked()){
                    listener.passwordIsLocked();
                    Log.e("Application Password", "passwordIsLocked!!");
                }else{
                    listener.passwordIsUnlocked();
                    Log.e("Application Password", "passwordIsUnlocked!!");
                }
            }
        }
    }

    private ApplicationPreferences getAppPreferences(){
        if (null == mPreferences){
            mPreferences = ApplicationPreferences.getInstance();
            mPreferences.addPreferencesAppLockChangeListener(this);
        }

        return mPreferences;
    }

    private int getSecondsToLockApp(){
        if ( mSecondsToLock == 0){
            mSecondsToLock = getAppPreferences().getSecondsToLockApplication();
        }
        return mSecondsToLock;
    }

    //todo move to applicationState
    //this method is called if the user change the seconds preferences
    @Override
    public void updateSeconds(int seconds) {
        mSecondsToLock = seconds;
        updateCounter();
    }
    //todo move to applicationState
    private void updateCounter(){
        if( isToLockPassword()){
            setCounterToLockeApplication();
        }
    }


    //todo move to applicationState
    //this method is called by a counter after mSecondsTolock seconds
    @Override
    public void calledByCounter(int seconds ) {
        if ( isToLockPassword() && getSecondsToLockApp() == seconds ){
            lockPassword();
            deleteKey();
        }
    }

    private void deleteKey(){
        mKey = null;
        Log.e("Application Password", "Key was deleted!!");
    }

    //todo move to applicationState
    public void unlockPassword( ){
        if ( null != mKey )  {
            mIsAppLocked = false;
            setCounterToLockeApplication();
            updateListeners();
            Log.e("Application Password", "Unlock Password!! from unlocked");
        }
    }


    public void lockPassword(){
        mIsAppLocked = true;
        if ( getSecondsToLockApp() == -2){
            deleteKey();
        }

        updateListeners();// TODO ??
    }

    //todo move to applicationState
    private void setCounterToLockeApplication(){
        int seconds = getSecondsToLockApp();
        if ( seconds >= 30 ){
            mCounter = new Counter(this);
            mCounter.startCounter(seconds);
        }
    }

    //todo move to applicationState
    public boolean isApplicationLocked(){
        return mIsAppLocked;
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


    public interface ApplicationPasswordListener {
        void passwordIsLocked();

        void passwordIsUnlocked();

    }

}
