package com.example.mark.passwordmanager.generator;



import android.os.AsyncTask;
import com.example.mark.passwordmanager.RawData;
import java.util.Random;

/**
 * Created by mark on 3/2/15.
 */
public class PasswordGenerator   {

    //TODO take care of length thresolds!!!!!

    private PasswordGeneratorListener mListener;
    private Random mRandom;


    public PasswordGenerator( PasswordGeneratorListener listener){

        if (null == listener){
            throw new NullPointerException("" +  //IllegalStateException??
                    "PasswordGenerator must have an instance not null of a PasswordManagerListener");
        }else{
            mListener = listener;
            mRandom = new Random();
        }
    }


    public void generatePassword(int passwordLength ){
        if ( passwordLength > 7){
            new PasswordGeneratorTask().execute(passwordLength);
        }else{
            throw new IllegalArgumentException(
                    "PasswordGenerator cannot generate a password with length smaller than 8");
        }
    }

    private RawData generatePasswordAsync(int passwordLength){
        CharList list = getCharactersList(passwordLength);
        char [] password = new char [list.getSize()];

        for (int i = 0; i < password.length; i++){
            password[i] = getCharacter( list.popType());
        }
        return new RawData(password);
    }

    private CharList getCharactersList(int passwordLength){
        return new CharList(passwordLength);
    }


    private char getCharacter(int type){
        final int NUMBERS = 0;
        final int LOWER = 1;
        final int CAPS = 2;
        final int SYMBOLS = 3;

        switch (type){
            case NUMBERS:
                return getNumber();

            case LOWER:
                return getLowerLetter();

            case CAPS:
                return getCapsLetter();

            case SYMBOLS:
                return getSymbol();

            default:
                throw new IllegalArgumentException(
                        "getCharacter inside PasswordGenerator has a bug!!!");
        }
    }


    private char getNumber(){

        return (char)(mRandom.nextInt(10) + 48);
    }

    private char getCapsLetter(){

        return (char)(mRandom.nextInt(26) + 65);
    }

    private char getLowerLetter(){

        return (char)(mRandom.nextInt(26) + 97);
    }

    private char getSymbol(){
        char [] symbols = {'!','@','#','$','%','^','&','*','?','>','<','~','(',')'};

        return symbols[mRandom.nextInt(symbols.length)];
    }



    private class PasswordGeneratorTask extends AsyncTask<Integer, Void, RawData> {

        @Override
        protected RawData doInBackground(Integer... params) {

            return generatePasswordAsync(params[0]);
        }

        @Override
        protected void onPostExecute(RawData password) {

            mListener.passwordGeneratorCallBack(password);
        }
    }

    public interface PasswordGeneratorListener{
        void passwordGeneratorCallBack(RawData password);
    }


}
