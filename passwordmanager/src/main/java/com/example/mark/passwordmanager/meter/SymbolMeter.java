package com.example.mark.passwordmanager.meter;

import com.example.mark.passwordmanager.RawData;

import java.util.regex.Pattern;

/**
 * Created by mark on 2/26/15.
 */
class SymbolMeter extends PatternMeter {
    private final Pattern pattern = Pattern.compile("[!@#$%^&*?><~()]");

    private SymbolMeter(RawData password){
        this.password = password;
        setPattern(pattern);
    }

    static int valueOf(RawData password){
        return new SymbolMeter(password).calculatePatternPoints();
    }



}
