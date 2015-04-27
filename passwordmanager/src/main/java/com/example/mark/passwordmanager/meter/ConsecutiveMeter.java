package com.example.mark.passwordmanager.meter;


/**
 * Created by mark on 2/26/15.
 */
class ConsecutiveMeter {

    private ConsecutiveMeter(){
        throw new UnsupportedOperationException("ConsecutiveMeter cannot be instantiated");
    }

    static int valueOf(char [] password){
        return calculateConsecutive(password);
    }

    private static int calculateConsecutive(final char [] password){
        int result = 0;
        int anteriorChar;
        int currentChar;
        for( int i = 1; i < password.length ; i++){

            anteriorChar = (int)password[i-1];
            currentChar = (int)password[i];

            if ( isSequenceSameCase(anteriorChar,currentChar) ){
                result += 2;
            }else if( isSequenceDifferentCase(anteriorChar,currentChar) ){
                result += 1;
            }

        }

        return (int)(result/1.5);
    }
    private static boolean isSequenceSameCase(int firstChar, int secondChar){
        return (firstChar + 1 == secondChar);
    }

    private static boolean isSequenceDifferentCase(int anteriorChar, int currentChar){
        return ( (anteriorChar + 33 == currentChar)  || (anteriorChar - 31 == currentChar));
    }



}
