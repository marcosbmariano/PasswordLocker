package com.marcos.autodatabases.utils;

import com.example.mark.myutils.ClassSelector;

import java.lang.reflect.Field;
import static com.marcos.autodatabases.utils.SQLConstants.*;

/**
 * Created by mark on 4/30/15.
 */
class SQLColumnTypeSelector extends ClassSelector<String> {

    private SQLColumnTypeSelector(){

    }

    public static String getColumnType(Field field){
        SQLColumnTypeSelector gen = new SQLColumnTypeSelector();
        return gen.selectMethod(field.getType().getName());
    }

    @Override
    protected String getByte() {
        return INTEGER;
    }

    @Override
    protected String getBoxedByte() {
        return INTEGER;
    }

    @Override
    protected String getInt() {
        return INTEGER;
    }

    @Override
    protected String getBoxedInteger() {
        return INTEGER;
    }

    @Override
    protected String getShort() {
        return INTEGER;
    }

    @Override
    protected String getBoxedShort() {
        return INTEGER;
    }

    @Override
    protected String getLong() {
        return INTEGER;
    }

    @Override
    protected String getBoxedLong() {
        return INTEGER;
    }

    @Override
    protected String getFloat() {
        return REAL;
    }

    @Override
    protected String getBoxedFloat() {
        return REAL;
    }

    @Override
    protected String getDouble() {
        return REAL;
    }

    @Override
    protected String getBoxedDouble() {
        return REAL;
    }

    @Override
    protected String getBoolean() {
        return INTEGER;
    }

    @Override
    protected String getBoxedBoolean() {
        return INTEGER;
    }

    @Override
    protected String getChar() {
        return TEXT;
    }

    @Override
    protected String getBoxedChar() {
        return TEXT;
    }

    @Override
    protected String getString() {
        return TEXT;
    }

    @Override
    protected String getDefault() { //TODO fix this!!!!
        return null;
    }
}
