package com.example.mark.myutils;

import android.util.Log;

import java.lang.reflect.Type;

/**
 * Created by mark on 4/29/15.
 */
public abstract class ClassSelector<E> {
    private final static String BYTE = "byte";
    private final static String BOXED_BYTE ="java.lang.Byte";
    private final static String INTEGER = "int";
    private final static String BOXED_INTEGER = "java.lang.Integer";
    private final static String SHORT = "short";
    private final static String BOXED_SHORT ="java.lang.Short";
    private final static String LONG = "long";
    private final static String BOXED_LONG  = "java.lang.Long";
    private final static String FLOAT = "float";
    private final static String BOXED_FLOAT = "java.lang.Float";
    private final static String DOUBLE = "double";
    private final static String BOXED_DOUBLE = "java.lang.Double";
    private final static String BOOLEAN = "boolean";
    private final static String BOXED_BOOLEAN = "java.lang.Boolean";
    private final static String CHAR = "char";
    private final static String BOXED_CHAR = "java.lang.Character";
    private final static String STRING = "java.lang.String";




    E getResult(Object obj){

        String className = getClassName(obj);
        Log.e("Inside ClassSelector", className);
        return selectMethod(getClassName(obj), obj);
    }
    E getResult(byte x){
        return getByte(x);
    }

    String getClassName(Object obj){
        if ( null == obj){
            return "null";
        }
        return String.valueOf(obj.getClass().getName());
    }


    final E selectMethod(String index, Object obj) {

        switch (index) {
            case BOXED_BYTE:
                return getBoxedByte((Byte)obj);
            case INTEGER:
                return getInt();
            case BOXED_INTEGER:
                return getBoxedInteger();
            case SHORT:
                return getShort();
            case BOXED_SHORT:
                return getBoxedShort();
            case LONG:
                return getLong();
            case BOXED_LONG:
                return getBoxedLong();
            case FLOAT:
                return getFloat();
            case BOXED_FLOAT:
                return getBoxedFloat();
            case DOUBLE:
                return getDouble();
            case BOXED_DOUBLE:
                return getBoxedDouble();
            case BOOLEAN:
                return getBoolean();
            case BOXED_BOOLEAN:
                return getBoxedBoolean();
            case CHAR:
                return getChar();
            case BOXED_CHAR:
                return getBoxedChar();
            case STRING:
                return getString();
            default:
                return getDefault(index);
        }
    }

    abstract E getByte(byte x);
    abstract E getBoxedByte(Byte x);
    abstract E getInt();
    abstract E getBoxedInteger();
    abstract E getShort();
    abstract E getBoxedShort();
    abstract E getLong();
    abstract E getBoxedLong();
    abstract E getFloat();
    abstract E getBoxedFloat();
    abstract E getDouble();
    abstract E getBoxedDouble();
    abstract E getBoolean();
    abstract E getBoxedBoolean();
    abstract E getChar();
    abstract E getBoxedChar();
    abstract E getString();
    abstract E getDefault(String index);

}
