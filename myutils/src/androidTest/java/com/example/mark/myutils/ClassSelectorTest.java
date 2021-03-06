package com.example.mark.myutils;

import android.test.AndroidTestCase;


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
        int x = 3;
        assertEquals(INTEGER, mSelector.getResult(x));
        Integer test = 5;
        assertEquals(BOXED_INTEGER, mSelector.getResult(test));
    }

    public void testShort(){
        short x = Short.MAX_VALUE;
        assertEquals(SHORT, mSelector.getResult(x));
        Short test = Short.MAX_VALUE;
        assertEquals(BOXED_SHORT, mSelector.getResult(test));
    }

    public void testLong(){
        long x = Long.MAX_VALUE;
        assertEquals(LONG, mSelector.getResult(x));
        Long test = Long.MAX_VALUE;
        assertEquals(BOXED_LONG, mSelector.getResult(test));
    }

    public void testBoolean(){
        boolean x = true;
        //noinspection ConstantConditions
        assertEquals(BOOLEAN, mSelector.getResult(x));
        Boolean test = Boolean.FALSE;
        //noinspection ConstantConditions
        assertEquals(BOXED_BOOLEAN, mSelector.getResult(test));
    }

    public void testDouble(){
        double x = Double.MAX_VALUE;
        assertEquals(DOUBLE, mSelector.getResult(x));
        Double test = Double.MAX_VALUE;
        assertEquals(BOXED_DOUBLE, mSelector.getResult(test));
    }

    public void testFloat(){
        float x = Float.MAX_VALUE;
        assertEquals(FLOAT, mSelector.getResult(x));
        Float test = Float.MAX_VALUE;
        assertEquals(BOXED_FLOAT, mSelector.getResult(test));
    }

    public void testChar(){
        char x = 'h';
        assertEquals(CHAR, mSelector.getResult(x));
        Character test = 'C';
        assertEquals(BOXED_CHAR, mSelector.getResult(test));
    }

    public void testString(){
        String test = "testTest";
        assertEquals(STRING, mSelector.getResult(test));
    }

    public void testDefault(){
        Object test = null;
        //noinspection ConstantConditions
        assertEquals(DEFAULT, mSelector.getResult(test));
        //it does not expect an array, so it defaults
        String [] x = {"", ""};
        assertEquals(DEFAULT, mSelector.getResult(x));
    }

    public void testInCaseOfOtherClass(){
        AnotherClass x = new AnotherClass();
        assertEquals(ANOTHER_CLASS, mSelector.getResult(x));
    }

    private final String BYTE = "byte";
    private final String BOXED_BYTE ="boxedByte";
    private final String INTEGER = "integer";
    private final String BOXED_INTEGER = "boxedInteger";
    private final String SHORT = "SHORT";
    private final String BOXED_SHORT ="boxedShort";
    private final String LONG = "long";
    private final String BOXED_LONG = "boxedLong";
    private final String FLOAT = "float";
    private final String BOXED_FLOAT = "boxedFLoat";
    private final String DOUBLE = "double";
    private final String BOXED_DOUBLE = "boxedDouble";
    private final String BOOLEAN = "boolean";
    private final String BOXED_BOOLEAN = "boxedBoolean";
    private final String CHAR = "char";
    private final String BOXED_CHAR = "boxedChar";
    private final String STRING = "string";
    private final String DEFAULT = "default";
    private final String ANOTHER_CLASS = "anotherClass";

    class ClassSelectorForTest extends ClassSelector<String>{

        private Object mObj;
        public String getResult(Object obj){
            mObj = obj;
            return super.getResult(obj);
        }

        @Override
        protected String getByte() { return BYTE; }

        @Override
        protected String getBoxedByte() {
            return BOXED_BYTE;
        }

        @Override
        protected String getInt() {
            return INTEGER;
        }

        @Override
        protected String getBoxedInteger() {
            return BOXED_INTEGER;
        }

        @Override
        protected String getShort() {
            return SHORT;
        }

        @Override
        protected String getBoxedShort() {
            return BOXED_SHORT;
        }

        @Override
        protected String getLong() {
            return LONG;
        }

        @Override
        protected String getBoxedLong() {
            return BOXED_LONG;
        }

        @Override
        protected String getFloat() {
            return FLOAT;
        }

        @Override
        protected String getBoxedFloat() {
            return BOXED_FLOAT;
        }

        @Override
        protected String getDouble() {
            return DOUBLE;
        }

        @Override
        protected String getBoxedDouble() {
            return BOXED_DOUBLE;
        }

        @Override
        protected String getBoolean() {
            return BOOLEAN;
        }

        @Override
        protected String getBoxedBoolean() {
            return BOXED_BOOLEAN;
        }

        @Override
        protected String getChar() {
            return CHAR;
        }

        @Override
        protected String getBoxedChar() {
            return BOXED_CHAR;
        }

        @Override
        protected String getString() {
            return STRING;
        }

        @Override
        protected String getDefault() {
            if (mObj instanceof AnotherClass){
                return getAnotherClass((AnotherClass)mObj);
            }else{
                return DEFAULT;
            }
        }

        //this is in case of the need to extend the class to handle another case of
        //object

        String getAnotherClass(AnotherClass anotherClass){
            return ANOTHER_CLASS;
        }
    }

    private class AnotherClass{

    }
}
