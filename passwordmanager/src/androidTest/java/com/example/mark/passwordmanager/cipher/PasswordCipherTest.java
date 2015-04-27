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

        byte [] iv = PasswordCipher.generateIv();
        byte [] data = PasswordUtils.charToBytes(text.toCharArray());
        byte [] key = PasswordCipher.generateKey();

        byte [] cipherText = PasswordCipher.encrypt(data,key, iv);

        byte [] byteDecryptedText = PasswordCipher.decrypt( cipherText,key,  iv);

        assertTrue(Arrays.equals(data,byteDecryptedText ));
    }

    public void testEmptyData(){
        String text = "";

        byte [] iv = PasswordCipher.generateIv();
        byte [] data = PasswordUtils.charToBytes(text.toCharArray());
        byte [] key = PasswordCipher.generateKey();

        byte [] cipherText = PasswordCipher.encrypt(data, key, iv);

        byte [] byteDecryptedText = PasswordCipher.decrypt( cipherText,key, iv);

        assertTrue(Arrays.equals(data,byteDecryptedText ));
    }


}
