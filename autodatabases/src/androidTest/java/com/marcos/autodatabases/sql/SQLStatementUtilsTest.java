package com.marcos.autodatabases.sql;

import android.test.AndroidTestCase;


/**
 * Created by mark on 4/24/15.
 */
public class SQLStatementUtilsTest extends AndroidTestCase {



    public void testInsertInsideSingleQuotes(){
        String test = "test";
        String expected = "\'test\'";
        assertTrue(expected.equals(SQLStatementUtils.insertInsideSingleQuotes(test)));
    }

    public void testSetupValueForStatementString(){
        String test = "test";
        String expected = "\'test\'";
        String result = SQLStatementUtils.setupValueForStatement(test);
        assertTrue(expected.equals(result));

    }

    public void testSetupValueForStatementBoolean(){
        boolean test = true;
        String expected = "1";
        //noinspection ConstantConditions
        String result = SQLStatementUtils.setupValueForStatement(test);
        assertTrue(expected.equals(result));
    }

    public void testSetupValueForStatementDouble(){
        double test = 3.7;
        String expected = "3.7";
        String result = SQLStatementUtils.setupValueForStatement(test);
        assertTrue(expected.equals(result));
    }

    public void testSetupValueForStatementInteger(){
        int test = 5;
        String expected = "5";
        String result = SQLStatementUtils.setupValueForStatement(test);
        assertTrue(expected.equals(result));
    }

    public void testSetupValueForStatementNullObject(){
        String test = null;
        String expected = "null";
        //noinspection ConstantConditions
        String result = SQLStatementUtils.setupValueForStatement(test);
        assertTrue(expected.equals(result));
    }

    public void testSetupValueForStatementCharacter(){
        char test = 'C';
        String expected = "\'C\'";
        String result = SQLStatementUtils.setupValueForStatement(test);
        assertTrue(expected.equals(result));
    }

}
