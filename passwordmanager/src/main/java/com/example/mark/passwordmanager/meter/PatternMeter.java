package com.example.mark.passwordmanager.meter;


import com.example.mark.passwordmanager.RawData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mark on 2/26/15.
 */
abstract class PatternMeter {

    private Pattern pattern;
    private RawData password; //todo check if it is never assigned

    PatternMeter(){

    }


    final void setPattern(Pattern pattern){
        this.pattern = pattern;
    }

    final void setPassword(RawData password){
        this.password = password;
    }


    final int calculatePatternPoints(){

        final int occur = numberOfPatterOccurrences(password, pattern);
        final int percentageOfOccour = patternPercentage(occur, password.length());

        return calculatePatternStrength(percentageOfOccour);
    }

    private int numberOfPatterOccurrences(RawData password, Pattern pattern){
        int result = 0;
        Matcher matcher = pattern.matcher(password.toString());
        while(matcher.find()){
            result++;
        }
        return result;
    }

    private int patternPercentage(int occur, int length){
        //int perc = ( (100 * occur) / length );
        return  ( (100 * occur) / length );
    }

    private int calculatePatternStrength(int percentage){
        int result = 0;

        if(( percentage == 0)){
            result = 0;
        }else if( percentage > 0 && percentage <= 14 || ( percentage >= 38 )){
            result = 1;
        }else if ( ( percentage >= 15 && percentage <= 19) ||
                ( percentage >= 28 && percentage <= 37)) {
            result = 2;

        }else if ( percentage >=20 && percentage <= 27){
            result = 3;
        }

        return result;
    }

}


