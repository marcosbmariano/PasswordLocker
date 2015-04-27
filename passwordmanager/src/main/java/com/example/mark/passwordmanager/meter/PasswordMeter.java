package com.example.mark.passwordmanager.meter;



import android.os.AsyncTask;

import com.example.mark.passwordmanager.RawData;



/**
 * Created by mark on 2/19/15.
 */


public final class PasswordMeter {
    public static enum PasswordStrength { INVALID, MODERATED, GOOD, WEAK, STRONG }
    private PasswordMeterListener listener;
    private final int TOO_SHORT = -1;


    private int pMinimumLength = 7; //TODO lower and upper bound for the length of the password?


    public PasswordMeter(PasswordMeterListener listener){
        this.listener = listener;
    }

    public void checkPasswordStrength(RawData password){

        if (password.length() > pMinimumLength){
            new PasswordMeterTask().execute(password);
        }else {
            listener.setStrength(PasswordStrength.WEAK);
        }
    }


        //TODO change the name to valueOf
    PasswordStrength calculate(RawData password){

        PasswordStrength result = null;
        final char [] pass = password.getDataCharArray();

        if (isPasswordInvalid(password)){
           return PasswordStrength.INVALID;
        }

        if (calculatePasswordLengthStrength(password) == TOO_SHORT){
            result = PasswordStrength.WEAK;
        }else{
            result = getPasswordClassification( calculatePasswordPoints(password) );
        }

        return result;
    }

    private boolean isPasswordInvalid(RawData password){
        return (SpaceMeter.valueOf(password) > 0);
    }

    private PasswordStrength getPasswordClassification(int points){ //TODO improve this, very confuse!!!!
        PasswordStrength strength = PasswordStrength.WEAK;

        if( points >= 5 && points <= 10 ){
            strength = PasswordStrength.MODERATED;
        } else if( points >= 11 && points <= 13){
            strength = PasswordStrength.GOOD;
        } else if ( points >= 14){
            strength = PasswordStrength.STRONG;
        }
        return strength;
    }

    private int calculatePasswordPoints(RawData password){

        int points = 0;
        char [] pass = password.getDataCharArray();
        points += LowerCaseMeter.valueOf(password);
        points += UpperCaseMeter.valueOf(password);
        points += SymbolMeter.valueOf(password);
        points += NumbersMeter.valueOf(password);
        points += calculatePasswordLengthStrength(password);
        points -= CharacterRepetitionMeter.valueOf(pass);
        points -= ConsecutiveMeter.valueOf(pass);

        return points;
    }

    private int calculatePasswordLengthStrength(RawData password){
        int result = 0;
        final int length = password.length();

        if (length <= 7){
            result = -1;
        }else{
            result = length - 7;
        }
        return (int)(result * 1.5);
    }

    class PasswordMeterTask extends AsyncTask<RawData, Void, PasswordStrength> {

        @Override
        protected PasswordStrength doInBackground(RawData... params) {

            return calculate(params[0]);
        }

        @Override
        protected void onPostExecute(PasswordStrength strength) {

            listener.setStrength(strength);
        }
    }

    public interface PasswordMeterListener{
        void setStrength(PasswordStrength strength);
    }

}
