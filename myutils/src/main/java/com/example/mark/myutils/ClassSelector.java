package com.example.mark.myutils;


/**
 * Created by mark on 4/29/15.
 * In case the need of extend to user this class for another type of class, look the example
 * in this class test
 */
public abstract class ClassSelector<E> {
    private final static String BYTE ="byte";
    private final static String BOXED_BYTE ="java.lang.Byte";
    private final static String INTEGER = "int";
    private final static String BOXED_INTEGER = "java.lang.Integer";
    private final static String SHORT = "short";
    private final static String BOXED_SHORT ="java.lang.Short";
    private final static String LONG  = "long";
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


    protected E getResult(Object obj){
        return selectMethod(getClassName(obj));
    }
    protected E getResult(byte x){ //todo check this argument
        return getByte();
    }

    protected E getResult(int x){
        return getInt();
    } //todo check this argument

    protected E getResult(short x){
        return getShort();
    } //todo check this argument

    protected E getResult(long x){
        return getLong();
    } //todo check this argument

    protected E getResult(char x){
        return getChar();
    } //todo check this argument

    protected E getResult(boolean x){
        return getBoolean();
    } //todo check this argument

    protected E getResult(double x){
        return getDouble();
    } //todo check this argument

    protected E getResult(float x){
        return getFloat();
    } //todo check this argument

    protected String getClassName(Object obj){
        if ( null == obj){
            return "null";
        }
        return String.valueOf(obj.getClass().getName());
    }

    protected final E selectMethod(String index) {

        switch (index) {
            case BYTE:
                return getByte();
            case BOXED_BYTE:
                return getBoxedByte();
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
                return getDefault();
        }
    }

    protected abstract E getByte();
    protected abstract E getBoxedByte();
    protected abstract E getInt();
    protected abstract E getBoxedInteger();
    protected abstract E getShort();
    protected abstract E getBoxedShort();
    protected abstract E getLong();
    protected abstract E getBoxedLong();
    protected abstract E getFloat();
    protected abstract E getBoxedFloat();
    protected abstract E getDouble();
    protected abstract E getBoxedDouble();
    protected abstract E getBoolean();
    protected abstract E getBoxedBoolean();
    protected abstract E getChar();
    protected abstract E getBoxedChar();
    protected abstract E getString();
    protected abstract E getDefault();

}
