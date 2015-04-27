package com.example.mark.passwordmanager.meter;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by mark on 2/26/15.
 */
public class CharacterRepetitionMeter {

    private CharacterRepetitionMeter(){
        throw new UnsupportedOperationException("CharacterRepetitionMeter cannot be instantiated!");
    }

    static int valueOf(char [] password){
        return checkForRepetition(password);
    }


    private static int checkForRepetition(char [] password){
        int result = 0;
        Set<Character> characters = new HashSet(password.length);

        for (char c : password){
            characters.add(c);
        }
        result = password.length - characters.size();

        return (result *= 1.5);
    }
}
