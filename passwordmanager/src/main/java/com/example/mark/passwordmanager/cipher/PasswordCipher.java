package com.example.mark.passwordmanager.cipher;

import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by mark on 3/9/15.
 */

public class PasswordCipher {
    private static final String ENCRYPT_ALGORITHM = "AES";
    private static final String ENCRYPT_ALGORITHM_W_PADDING = "AES/CBC/PKCS5Padding";
    private static final String RDN_ALGORITHM = "SHA1PRNG";

    //TODO SALT!!!!!!!!!

    public static byte[] generateKey(){
        SecretKey secretKey = null;

        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPT_ALGORITHM);
            SecureRandom random = SecureRandom.getInstance(RDN_ALGORITHM);

            keyGenerator.init(256, random);
            secretKey = keyGenerator.generateKey();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return secretKey.getEncoded();
    }

    public static byte [] generateIv(){
        Random rand = new Random();
        byte [] result = new byte[16];
        rand.nextBytes(result);
        return result;
    }

    public static byte[] encrypt(byte [] data, byte [] key,  byte [] iv){
        return decryptEncript( data,key, Cipher.ENCRYPT_MODE, iv );
    }

    public static byte[] decrypt(byte [] data,  byte [] key,  byte [] iv){
        return decryptEncript( data ,key, Cipher.DECRYPT_MODE, iv);
    }

    private static byte [] addSaltToData(byte [] data, byte [] salt ){

        byte [] result = new byte[data.length + salt.length];

        for ( int i = 0; i < data.length; i++){
            result[i] = data[i];
        }
        int difference = data.length;
        for ( int i = 0; i < salt.length; i++){
            result[difference + i] = salt[i];
        }
        return result;
    }

    private static byte [] subtractSaltFromData(byte [] data, byte [] salt){
        return Arrays.copyOfRange(data, 0, data.length - salt.length);
    }


    static byte [] decryptEncript( byte [] data,byte [] key, int cipherMode, byte [] iv){
        SecretKeySpec sKeySpec = new SecretKeySpec(key, ENCRYPT_ALGORITHM);
        Cipher cipher;
        byte [] result = new byte [0]; //this is initialized, so if a exception occur, the return will not be null

        try{
            cipher = Cipher.getInstance(ENCRYPT_ALGORITHM_W_PADDING);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(cipherMode, sKeySpec, ivSpec);
            result = cipher.doFinal(data);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        return result;

    }

}
