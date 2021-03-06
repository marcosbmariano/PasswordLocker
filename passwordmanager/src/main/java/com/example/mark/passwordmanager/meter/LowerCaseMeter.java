package com.example.mark.passwordmanager.meter;



import com.example.mark.passwordmanager.RawData;

import java.util.regex.Pattern;

/**
 * Created by mark on 2/26/15.
 */
class LowerCaseMeter extends PatternMeter {

    private LowerCaseMeter(RawData password){
        setPassword(password);
        setPattern( Pattern.compile("[a-z]"));
    }

    static int valueOf(RawData password){
        return new LowerCaseMeter(password).calculatePatternPoints();
    }

}
