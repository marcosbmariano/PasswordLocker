package com.mark.passwordlocker.account;

import android.test.suitebuilder.annotation.LargeTest;

import com.example.mark.passwordmanager.PasswordUtils;
import com.example.mark.passwordmanager.cipher.PasswordCipher;

import junit.framework.TestCase;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.util.Arrays;

/**
 * Created by mark on 8/19/15.
 */
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EncryptMetadataTest extends TestCase {
    private AccountRecord mAccountRecord;
    //private final String dummyData = "asfdfghe3857292743";
    private String mEncryptedData;
    private byte [] mData, mKey, mIv, mSalt;


    @Override
    protected void setUp() throws Exception {
        mAccountRecord = new AccountRecord("Google", "1234456");
        mData = PasswordUtils.stringToBytes("asfdfghe3857292743");
        mKey = PasswordCipher.generateRandomKey();
        mIv = PasswordCipher.generateRandomIv();
        mSalt = PasswordCipher.generateRandomSalt();
        mEncryptedData = mAccountRecord.encrypt(mData, mSalt, mKey, mIv);
        super.setUp();
    }
    public void testAPreconditions(){
        assertNotNull(mAccountRecord);
        assertNotNull(mData);
        assertNotNull(mKey);
        assertNotNull(mIv);
        assertNotNull(mSalt);
        assertNotNull("encryptedData is null", mEncryptedData);
    }

    public void testValidPassword(){
        byte [] decryptedData =
                mAccountRecord.getDecryptedValue(mEncryptedData, mSalt, mKey, mIv);
        assertTrue(Arrays.equals(mData, decryptedData));
    }

    public void testWrongPassword(){
        byte [] wrongKey = PasswordCipher.generateRandomKey();
        byte [] decryptedData =
                mAccountRecord.getDecryptedValue(mEncryptedData, mSalt,wrongKey, mIv);
        assertNull("decryptedData should be null", decryptedData);
    }

    public void testEncryptionNullKey(){
        mKey = null;
        try {
            //noinspection ConstantConditions
            mAccountRecord.encrypt(mData, mSalt, mKey, mIv);
            fail("Null argument is supposed to thrown an exception");
        }catch (NullPointerException e){
            //Do nothing
        }catch (IllegalArgumentException e){
            //do nothing
        }
    }

    public void testEncryptionNullData(){
        mData = null;
        tryEncryptionWithNullArgument();
    }

    public void testEncryptionNullSalt(){
        mSalt = null;
        tryEncryptionWithNullArgument();
    }

    public void testEncryptionNullIv(){
        mIv = null;
        tryEncryptionWithNullArgument();
    }

    private void tryEncryptionWithNullArgument(){
        try {
            mAccountRecord.encrypt(mData, mSalt, mKey, mIv);
            fail("Null argument is supposed to thrown an exception");
        }catch (NullPointerException e){
            //Do nothing
        }
    }
    public void testDecryptionNullKey(){
        mKey = null;
        try {
            //noinspection ConstantConditions
            mAccountRecord.getDecryptedValue(mEncryptedData, mSalt, mKey, mIv);
            fail("DecryptionWith null key should thrown exception");
        } catch (NullPointerException e){
            //do nothing
        }
    }

    public void testDecryptionNullData(){
        mEncryptedData = null;
        tryDecryptionWithNullArgument();
    }

    public void testDecryptionNullSalt(){
        mSalt = null;
        tryDecryptionWithNullArgument();
    }

    public void testDecryptionNullIv(){
        mIv = null;
        tryDecryptionWithNullArgument();
    }

    private void tryDecryptionWithNullArgument(){
        try {
            mAccountRecord.getDecryptedValue(mEncryptedData, mSalt,mKey, mIv);
            fail("Null argument is supposed to thrown an exception");
        }catch (NullPointerException e){
            //Do nothing
        }
    }

    public void testDecryptionWrongData(){
        mData = PasswordUtils.stringToBytes("blalalalal");

    }



}































//