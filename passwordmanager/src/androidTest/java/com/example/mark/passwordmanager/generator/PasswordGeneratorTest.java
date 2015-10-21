package com.example.mark.passwordmanager.generator;





import android.test.AndroidTestCase;

import com.example.mark.passwordmanager.RawData;


/**
 * Created by mark on 3/2/15.
 */
public class PasswordGeneratorTest extends AndroidTestCase implements
        PasswordGenerator.PasswordGeneratorListener {

    private final PasswordGenerator gen = new PasswordGenerator(this);
    private int actualPasswordLength;

    public void testPasswordLength1(){
         actualPasswordLength = 8;
         gen.generatePassword(actualPasswordLength);
    }

    public void testPasswordLength2(){
        actualPasswordLength = 9;
        gen.generatePassword(actualPasswordLength);
    }

    public void testPasswordLength3(){
        actualPasswordLength = 10;
        gen.generatePassword(actualPasswordLength);
    }

    public void testPasswordLength4(){
        actualPasswordLength = 11;
        gen.generatePassword(actualPasswordLength);
    }

    public void testPasswordLength5(){
        actualPasswordLength = 12;
        gen.generatePassword(actualPasswordLength);
    }

    public void testPasswordLength6(){
        actualPasswordLength = 13;
        gen.generatePassword(actualPasswordLength);
    }

    public void testPasswordLength7(){
        actualPasswordLength = 14;
        gen.generatePassword(actualPasswordLength);
    }


    @Override
    public void passwordGeneratorCallBack(RawData password) {
        assertEquals("Password Length", password.length(), actualPasswordLength);
        //Log.d("PasswordGenerator test", "Length " + password.length());
    }
}
