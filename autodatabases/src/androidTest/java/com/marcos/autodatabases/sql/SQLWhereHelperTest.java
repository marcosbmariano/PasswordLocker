package com.marcos.autodatabases.sql;

import android.test.AndroidTestCase;

import com.marcos.autodatabases.models.Model;

/**
 * Created by mark on 4/28/15.
 */
public class SQLWhereHelperTest extends AndroidTestCase {
    private SQLWhereHelper mWhereHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mWhereHelper = new SQLWhereHelper();
    }

    public void test1(){
        String expected = " WHERE id = 15";
        mWhereHelper.where(Model.ID, 15);
        assertTrue(expected.equals(mWhereHelper.getWhereStatement()));
    }

    public void test2(){
        String expected = " WHERE Name = \'Mary\'";
        mWhereHelper.where("Name", "Mary");
        assertTrue(expected.equals(mWhereHelper.getWhereStatement()));
    }
}
