package com.example.mark.passwordmanager;

import android.test.AndroidTestCase;


import com.example.mark.passwordmanager.generator.PasswordGenerator;
import com.example.mark.passwordmanager.meter.PasswordMeter;

import static com.example.mark.passwordmanager.meter.PasswordMeter.PasswordStrength.*;

/**
 * Created by mark on 3/2/15.
 */
public class PasswordTest extends AndroidTestCase
        implements PasswordMeter.PasswordMeterListener, PasswordGenerator.PasswordGeneratorListener{

    PasswordMeter mMeter;
    PasswordGenerator mGenerator;
    PasswordMeter.PasswordStrength stCompared;
    RawData mPassword;
    int passwordLength;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMeter = new PasswordMeter(this);
        mGenerator = new PasswordGenerator(this);

    }

    @Override //this method is called from PasswordMeter with the PasswordStrenght result
    public void setStrength(PasswordMeter.PasswordStrength strength) {
        assertEquals(mPassword.toString(), stCompared, strength);
    }

    @Override //this method is called from PasswordGenerator with the result
    public void passwordGeneratorCallBack(RawData password) {
        mPassword = password;
        mMeter.checkPasswordStrength(password);
    }

    public void testGeneratedPasswordStrengthGood(){
        passwordLength = 8;
        stCompared = GOOD;
        mGenerator.generatePassword(passwordLength);
    }

    public void testGeneratedPasswordStrengthStrong1(){ //TODO fix that on password meter!!!!,
        passwordLength = 10;                            //TODO Not constant results!!!!
        stCompared = STRONG;                            //TODO Should be always STRONG!!!
        mGenerator.generatePassword(passwordLength);
    }

    public void testGeneratedPasswordStrengthStrong2(){
        passwordLength = 12;
        stCompared = STRONG;
        mGenerator.generatePassword(passwordLength);
    }





    //these methos are to test a password strength
    public void testInvalidWithSpace1(){
        mPassword = new RawData("maria da silva".toCharArray());
        stCompared = INVALID;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testInvalidWithSpace2(){
        mPassword = new RawData("mariadasilva ".toCharArray());
        stCompared = INVALID;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testInvalidWithSpace3(){
        mPassword = new RawData(" mariadasilva".toCharArray());
        stCompared = INVALID;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testWeak1(){
        mPassword = new RawData("aaaaa".toCharArray());
        stCompared = WEAK;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testWeak2(){
        mPassword = new RawData("aaaaaaaa".toCharArray());
        stCompared = WEAK;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testWeak3(){
        mPassword = new RawData("bananada".toCharArray());
        stCompared = WEAK;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testWeak4(){
        mPassword = new RawData("BaNaNaDa".toCharArray());
        stCompared = WEAK;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testWeak5(){
        mPassword = new RawData("banan123".toCharArray());
        stCompared = WEAK;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testWeak6(){
        mPassword = new RawData("AAAbbb11".toCharArray());
        stCompared = WEAK;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testWeak7(){
        mPassword = new RawData("bananada123".toCharArray());
        stCompared = WEAK;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testWeak8(){
        mPassword = new RawData("ABCabc12".toCharArray());
        stCompared = WEAK;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testModerated1(){
        mPassword = new RawData("AAAbbb111$$$".toCharArray());
        stCompared = MODERATED;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testModerated2(){
        mPassword = new RawData("AdCb11$$".toCharArray());
        stCompared = MODERATED;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testModerated3(){
        mPassword = new RawData("ABcd12&#".toCharArray());
        stCompared = MODERATED;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testModerated4(){
        mPassword = new RawData("$2uF3xO1".toCharArray());
        stCompared = MODERATED;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testGood1(){
        mPassword = new RawData("a1$F*8kQ".toCharArray());
        stCompared = GOOD;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testStrong1(){
        mPassword = new RawData("a1$F*8kQ5@".toCharArray());
        stCompared = STRONG;
        mMeter.checkPasswordStrength(mPassword);
    }

    public void testStrong2(){
        mPassword = new RawData("a1$F*8kQ5@s2".toCharArray());
        stCompared = STRONG;
        mMeter.checkPasswordStrength(mPassword);
    }

//    public void testStrong3(){
//        mPassword = new PasswordRaw("bQg2?vQ2J~".toCharArray());
//        stCompared = STRONG;
//        mMeter.checkPasswordStrength(mPassword);
//    }
//


}
