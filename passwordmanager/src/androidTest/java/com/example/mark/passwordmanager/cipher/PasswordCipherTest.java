package com.example.mark.passwordmanager.cipher;


import android.test.AndroidTestCase;
import com.example.mark.passwordmanager.PasswordUtils;

import java.security.InvalidKeyException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;


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

        byte [] cipherText = new byte[0];
        try {
            cipherText = PasswordCipher.encryptWithSalt(data, salt, key, iv);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        String ciphedString = PasswordUtils.byteToString(cipherText);
        byte [] byteDecryptedText = new byte[0];
        try {
            byteDecryptedText = PasswordCipher.decryptWithSalt(ciphedString, salt, key, iv);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        assertTrue(Arrays.equals(data,byteDecryptedText ));
    }

    public void testEmptyData(){
        String text = "";

        byte [] iv = PasswordCipher.generateRandomIv();
        byte [] data = PasswordUtils.charToBytes(text.toCharArray());
        byte [] key = PasswordCipher.generateRandomKey();
        byte [] salt = PasswordUtils.stringToBytes("SaltSalt");

        byte [] cipherText = new byte[0];
        try {
            cipherText = PasswordCipher.encryptWithSalt(data, salt, key, iv);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        String ciphedString = PasswordUtils.byteToString(cipherText);
        byte [] byteDecryptedText = new byte[0];
        try {
            byteDecryptedText = PasswordCipher.decryptWithSalt(ciphedString, salt, key, iv);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        assertTrue(Arrays.equals(data,byteDecryptedText ));
    }

    public void testWrongKey(){
        String text = "jsjshrhchxd";
        byte [] iv = PasswordCipher.generateRandomIv();
        byte [] data = PasswordUtils.charToBytes(text.toCharArray());
        byte [] key = PasswordCipher.generateRandomKey();
        byte [] salt = PasswordUtils.stringToBytes("SaltSalt");

        byte [] cipherText = new byte[0];
        try {
            cipherText = PasswordCipher.encryptWithSalt(data, salt, key, iv);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        String ciphedString = PasswordUtils.byteToString(cipherText);
        byte [] byteDecryptedText = new byte[0];
        //another key
        key = PasswordCipher.generateRandomKey();
        try {
            byteDecryptedText = PasswordCipher.decryptWithSalt(ciphedString, salt, key, iv);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        assertFalse(Arrays.equals(data,byteDecryptedText ));


    }


}
