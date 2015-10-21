package com.marcos.autodatabases.utils;

import android.test.AndroidTestCase;


import com.marcos.autodatabases.models.User;

/**
 * Created by mark on 5/1/15.
 */
public class SQLColumnsGeneratorTest extends AndroidTestCase {

    public void test1(){
        String expected ="( Id INTEGER PRIMARY KEY AUTOINCREMENT, lastName TEXT, boolean INTEGER," +
                " byte INTEGER, char TEXT, double REAL, float REAL, long INTEGER, short INTEGER," +
                " Name TEXT NOT NULL UNIQUE, Number INTEGER )";

        String result = SQLColumnsGenerator.getColumnsStatement(User.class);
        assertTrue(expected.equals(result));
    }
}
