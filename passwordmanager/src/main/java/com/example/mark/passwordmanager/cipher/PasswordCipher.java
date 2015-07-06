package com.example.mark.passwordmanager.cipher;

import android.util.Log;

import com.example.mark.passwordmanager.PasswordUtils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by mark on 3/9/15.
 */

public class PasswordCipher {
    private static final String ENCRYPT_ALGORITHM = "AES";
    private static final String ENCRYPT_ALGORITHM_W_PADDING = "AES/CBC/PKCS5Padding";
    private static final String RDN_ALGORITHM = "SHA1PRNG";
    private static final int SALT_SIZE = 24;
    private static final int IV_SIZE = 16;
    private static final int PBKDF2_ITERATIONS = 1000;
    private static final int PBKDF2_KEY_LENGTH = 256;

    //TODO SALT!!!!!!!!!

    public static byte[] generateRandomKey(){
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

    public static byte [] generateRandomIv(){
        return generateSecureRandomNumber(IV_SIZE);
    }

    public static byte [] generateRandomSalt(){
        return generateSecureRandomNumber(SALT_SIZE);
    }

    private static byte [] generateSecureRandomNumber(int size){
        SecureRandom rand = new SecureRandom();
        byte [] result = new byte[size];
        rand.nextBytes(result);
        return result;
    }


    public static byte [] generateKeyFromPassword(String password){

        byte [] result = new byte [0];

        char [] passwordChar = password.toCharArray();
        byte [] salt = PasswordUtils.stringToBytes(password);

        PBEKeySpec spec = new PBEKeySpec(passwordChar, salt, PBKDF2_ITERATIONS, PBKDF2_KEY_LENGTH);
        SecretKeyFactory skf;
        try {
            skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            result = skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte [] encryptWithSalt(byte [] data, byte [] salt, byte [] key,  byte [] iv){
        byte [] saltedData = addSaltToData(data, salt);
        return decryptEncript(Cipher.ENCRYPT_MODE, saltedData, key, iv);
    }

    public static byte[] encrypt(byte [] data, byte [] key,  byte [] iv){
        return decryptEncript( Cipher.ENCRYPT_MODE, data, key, iv);
    }

    public static byte[] decryptWithSalt(String data, byte [] salt,  byte [] key,  byte [] iv){
        byte [] saltedData = PasswordUtils.stringToBytes(data);
        byte [] temp = decryptEncript(Cipher.DECRYPT_MODE, saltedData, key, iv);
        return subtractSaltFromData( temp, salt);
    }

    public static byte[] decrypt(byte [] data,  byte [] key,  byte [] iv){
        return decryptEncript( Cipher.DECRYPT_MODE, data, key, iv );
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


    static byte [] decryptEncript( int cipherMode, byte [] data, byte [] key, byte [] iv){
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
