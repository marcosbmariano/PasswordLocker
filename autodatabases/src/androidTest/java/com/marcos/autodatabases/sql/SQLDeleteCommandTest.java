package com.marcos.autodatabases.sql;

import android.test.AndroidTestCase;
import android.util.Log;


/**
 * Created by mark on 4/24/15.
 */
public class SQLDeleteCommandTest extends AndroidTestCase {
    private final String mTableName = "myTable";
    private SQLDeleteCommand mDeleteCommand;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mDeleteCommand = new SQLDeleteCommand();
    }

    public void test1(){
        mDeleteCommand.setTableName(mTableName);
        String match = "DELETE FROM myTable;";
        String statement = mDeleteCommand.getSQLStatement();
        Log.e("inside delete", statement);
        assertTrue(match.equals(statement));
    }

    public void test2(){
        mDeleteCommand.setTableName(mTableName);
        mDeleteCommand.where("AddressId", 2);
        String match = "DELETE FROM myTable WHERE AddressId = 2;";
        String statement = mDeleteCommand.getSQLStatement();
        assertTrue(match.equals(statement));
    }

}
