package com.example.mark.passwordmanager.meter;

import com.example.mark.passwordmanager.RawData;

import java.util.regex.Pattern;

/**
 * Created by mark on 2/26/15.
 */
class UpperCaseMeter extends PatternMeter {
    //private final Pattern pattern = Pattern.compile("[A-Z]");


    private UpperCaseMeter(RawData password){
        setPassword(password);
        setPattern(Pattern.compile("[A-Z]"));
    }

    static int valueOf(RawData password){
        return new UpperCaseMeter(password).calculatePatternPoints();
    }

}
