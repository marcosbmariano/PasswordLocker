package com.example.mark.passwordlocker.helpers;




import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;


import com.example.mark.passwordmanager.PasswordUtils;
import com.example.mark.passwordmanager.cipher.PasswordCipher;
import com.example.mark.passwordmanager.generator.PasswordGenerator;
import com.example.mark.passwordmanager.meter.PasswordMeter;

import java.util.Arrays;


/**
 * Created by mark on 1/20/15.
 */
public class PasswordManager {
//
//    public static final String PASSWORD_PREFERENCES = "password_pref";
//    private final int MINIMUM_PASS_LENGTH = 8;
//    private final int MAX_PASS_LENGTH = 14;
//    //private final Context mContext;


    //TODO fix the meter problem!!!!
    //TODO add salt to the password!!!



//    public PasswordManager(PasswordManagerListerners listerner){
//        if ( null == listerner){
//            throw new NullPointerException(
//                    "A PasswordManager must have a valid PasswordManagerListener!");
//        }else {
//           // mMeter = new PasswordMeter(listerner);
//            //mPGenerator = new PasswordGenerator(listerner);
//            //mContext = (Context)listerner;
//        }
//    }

//    public PasswordManager( Context context){
//        mContext = context;
//    }






    //garbage?
//    public static String encodeText(byte [] password){
//        return PasswordCipher.byteArrayToString(password);
//    }



    public static boolean isAutentic(byte [] password, byte [] savedPassword){ //TODO erase this
       return Arrays.equals(password, savedPassword) ;
    }

    public static boolean isEqualAndNotEmpty(EditText password, EditText confirmation){
        boolean isConfirmed = false;
        char [] pass = PasswordUtils.getChars(password);
        char [] confirma = PasswordUtils.getChars(confirmation);

        if ((Arrays.equals(pass, confirma) && (pass.length !=0) && (confirma.length != 0))){
            isConfirmed = true;
        }

        return isConfirmed;

    }

}
