package com.example.mark.passwordmanager.meter;

import com.example.mark.passwordmanager.RawData;

import java.util.regex.Pattern;

/**
 * Created by mark on 2/26/15.
 */
public class SpaceMeter extends PatternMeter {
    final private Pattern pattern = Pattern.compile("\\s");

    private SpaceMeter(RawData password){
        this.password = password;
        setPattern(pattern);
    }

    static int valueOf(RawData password){
        return new SpaceMeter(password).calculatePatternPoints();
    }

}
