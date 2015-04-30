package com.example.mark.myutils;

import android.test.AndroidTestCase;

import java.util.SimpleTimeZone;


/**
 * Created by mark on 4/29/15.
 */
public class ClassSelectorTest extends AndroidTestCase {
    private ClassSelectorForTest mSelector;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mSelector = new ClassSelectorForTest();
    }

    public void testByte(){
        byte x = 3;
        assertEquals(BYTE,mSelector.getResult(x));
        Byte test = 2;
        assertEquals(BOXED_BYTE,mSelector.getResult(test));
    }

    public void testInteger(){
        Integer test = 5;
        assertEquals(BOXED_INTEGER, mSelector.getResult(test));
    }

    public void testShort(){
        Short test = Short.MAX_VALUE;
        assertEquals(BOXED_SHORT, mSelector.getResult(test));
    }

    public void testLong(){
        Long test = Long.MAX_VALUE;
        assertEquals(BOXED_LONG, mSelector.getResult(test));
    }

    public void testBoolean(){
        Boolean test = Boolean.FALSE;
        assertEquals(BOXED_BOOLEAN, mSelector.getResult(test));
    }

    public void testSChar(){
        Character test = 'C';
        assertEquals(BOXED_CHAR, mSelector.getResult(test));
    }

    public void testString(){
        String test = "testTest";
        assertEquals(STRING, mSelector.getResult(test));
    }

    public void testDefault(){
        Object test = null;
        assertEquals(DEFAULT, mSelector.getResult(test));
    }






    String BYTE = "byte";
    String BOXED_BYTE ="boxedByte";
    String INTEGER = "integer";
    String BOXED_INTEGER = "boxedInteger";
    String SHORT = "SHORT";
    String BOXED_SHORT ="boxedShort";
    String LONG = "long";
    String BOXED_LONG = "boxedLong";
    String FLOAT = "float";
    String BOXED_FLOAT = "boxedFLoat";
    String DOUBLE = "double";
    String BOXED_DOUBLE = "boxedDouble";
    String BOOLEAN = "boolean";
    String BOXED_BOOLEAN = "boxedBoolean";
    String CHAR = "char";
    String BOXED_CHAR = "boxedChar";
    String STRING = "string";
    String DEFAULT = "default";

    class ClassSelectorForTest extends ClassSelector<String>{


        @Override
        String getByte() {
            return BYTE;
        }

        @Override
        String getBoxedByte() {
            return BOXED_BYTE;
        }

        @Override
        String getInt() {
            return INTEGER;
        }

        @Override
        String getBoxedInteger() {
            return BOXED_INTEGER;
        }

        @Override
        String getShort() {
            return SHORT;
        }

        @Override
        String getBoxedShort() {
            return BOXED_SHORT;
        }

        @Override
        String getLong() {
            return LONG;
        }

        @Override
        String getBoxedLong() {
            return BOXED_LONG;
        }

        @Override
        String getFloat() {
            return FLOAT;
        }

        @Override
        String getBoxedFloat() {
            return BOXED_FLOAT;
        }

        @Override
        String getDouble() {
            return DOUBLE;
        }

        @Override
        String getBoxedDouble() {
            return BOXED_DOUBLE;
        }

        @Override
        String getBoolean() {
            return BOOLEAN;
        }

        @Override
        String getBoxedBoolean() {
            return BOXED_BOOLEAN;
        }

        @Override
        String getChar() {
            return CHAR;
        }

        @Override
        String getBoxedChar() {
            return BOXED_CHAR;
        }

        @Override
        String getString() {
            return STRING;
        }

        @Override
        String getDefault(String index) {
            return DEFAULT;
        }
    }
}
