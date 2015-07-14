package com.example.mark.passwordmanager.cipher;


import android.test.AndroidTestCase;
import com.example.mark.passwordmanager.PasswordUtils;
import java.util.Arrays;


/**
 * Created by mark on 3/9/15.
 */
public class PasswordCipherTest extends AndroidTestCase{


    public void testDataEncode(){
        String text = "Mariquitachiquechique";

        byte [] iv = PasswordCipher.generateRandomIv();
        byte [] data = PasswordUtils.charToBytes(text.toCharArray());
        byte [] key = PasswordCipher.generateRandomKey();
        byte [] salt = PasswordUtils.stringToBytes("SaltSalt");

        byte [] cipherText = PasswordCipher.encryptWithSalt(data, salt, key, iv);
        String ciphedString = PasswordUtils.byteToString(cipherText);
        byte [] byteDecryptedText = PasswordCipher.decryptWithSalt( ciphedString,salt, key,  iv);

        assertTrue(Arrays.equals(data,byteDecryptedText ));
    }

    public void testEmptyData(){
        String text = "";

        byte [] iv = PasswordCipher.generateRandomIv();
        byte [] data = PasswordUtils.charToBytes(text.toCharArray());
        byte [] key = PasswordCipher.generateRandomKey();
        byte [] salt = PasswordUtils.stringToBytes("SaltSalt");

        byte [] cipherText = PasswordCipher.encryptWithSalt(data, salt, key, iv);
        String ciphedString = PasswordUtils.byteToString(cipherText);
        byte [] byteDecryptedText = PasswordCipher.decryptWithSalt(ciphedString, salt, key, iv);

        assertTrue(Arrays.equals(data,byteDecryptedText ));
    }


}
