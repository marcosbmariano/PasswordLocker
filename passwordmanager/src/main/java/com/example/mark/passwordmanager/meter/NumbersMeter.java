package com.example.mark.passwordmanager.meter;


import com.example.mark.passwordmanager.RawData;

import java.util.regex.Pattern;

/**
 * Created by mark on 2/26/15.
 */
class NumbersMeter extends PatternMeter {

    private NumbersMeter(RawData password){
        setPassword(password);
        setPattern(Pattern.compile("[\\d]"));
    }

    static int valueOf(RawData password){
        return new NumbersMeter(password).calculatePatternPoints();
    }


}
