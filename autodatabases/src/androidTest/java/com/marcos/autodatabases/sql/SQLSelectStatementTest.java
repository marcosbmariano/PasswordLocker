package com.marcos.autodatabases.sql;

import android.test.AndroidTestCase;
import android.util.Log;
import com.marcos.autodatabases.utils.SQLConstants;

/**
 * Created by mark on 4/28/15.
 */
public class SQLSelectStatementTest extends AndroidTestCase {

    private SQLSelectStatement mSelectStatement;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String tableName = "myTable";
        mSelectStatement = new SQLSelectStatement();
        mSelectStatement.setTableName(tableName);
    }

    public void test(){
        String expected = "SELECT * FROM myTable;";
        Log.e("Inside Select test", mSelectStatement.getSQLStatement());
        assertTrue(expected.equals(mSelectStatement.getSQLStatement()));
    }


    public void test1(){
        String expected = "SELECT * FROM myTable WHERE "+ SQLConstants.ID + " = 23;";
        mSelectStatement.where(SQLConstants.ID, 23);
        assertTrue(expected.equals(mSelectStatement.getSQLStatement()));
    }

    public void test2(){
        String expected = "SELECT columnName FROM myTable WHERE "+ SQLConstants.ID + " = 23;";
        mSelectStatement.where(SQLConstants.ID, 23);
        mSelectStatement.column("columnName");
        assertTrue(expected.equals(mSelectStatement.getSQLStatement()));
    }
}
