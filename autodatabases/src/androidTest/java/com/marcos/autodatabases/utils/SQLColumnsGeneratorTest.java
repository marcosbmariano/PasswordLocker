package com.marcos.autodatabases.utils;

import android.test.AndroidTestCase;
import android.util.Log;


import com.marcos.autodatabases.models.User;

/**
 * Created by mark on 5/1/15.
 */
public class SQLColumnsGeneratorTest extends AndroidTestCase {

    public void test1(){
        String expected = "( Id INTEGER PRIMARY KEY AUTOINCREMENT, lastName TEXT," +
                " Name TEXT NOT NULL UNIQUE, boolean INTEGER, double REAL, long INTEGER," +
                " byte INTEGER, char TEXT, float REAL, short INTEGER, Number INTEGER )";
        String result = SQLColumnsGenerator.getColumnsStatement(User.class);
        assertTrue(expected.equals(result));
    }
}
