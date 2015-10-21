package com.example.mark.passwordmanager.meter;

import com.example.mark.passwordmanager.RawData;

import java.util.regex.Pattern;

/**
 * Created by mark on 2/26/15.
 */
class SpaceMeter extends PatternMeter {

    private SpaceMeter(RawData password){
        setPassword(password);
        setPattern(Pattern.compile("\\s"));
    }

    static int valueOf(RawData password){
        return new SpaceMeter(password).calculatePatternPoints();
    }

}
