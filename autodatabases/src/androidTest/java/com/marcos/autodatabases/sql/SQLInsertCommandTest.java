package com.marcos.autodatabases.sql;

import android.test.AndroidTestCase;

/**
 * Created by mark on 4/27/15.
 */
public class SQLInsertCommandTest extends AndroidTestCase {
    private SQLInsertCommand mInsert;



    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String tableName = "myTable";
        mInsert = new SQLInsertCommand();
        mInsert.setTableName(tableName);
    }

    public void test1(){
        String expected = "INSERT INTO myTable ( Name, Age, Weight )" +
                " VALUES ( 'Maria' , 23 , 3.33 ) ;";
        mInsert.insertValue("Name", "Maria");
        mInsert.insertValue("Age", 23);
        mInsert.insertValue("Weight", 3.33);
        assertTrue(expected.equals(mInsert.getSQLStatement()));
    }

    public void test2(){
        String expected = "INSERT INTO myTable ( Name ) VALUES ( 'Maria' ) ;";
        mInsert.insertValue("Name", "Maria");
        assertTrue(expected.equals(mInsert.getSQLStatement()));
    }
}
