package com.marcos.autodatabases.sql;

import android.test.AndroidTestCase;
import com.marcos.autodatabases.utils.SQLConstants;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mark on 4/27/15.
 */
public class SQLUpdateCommantTest extends AndroidTestCase {
    private final String mTableName = "myTable";
    private SQLUpdateCommand mUpdate;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mUpdate = new SQLUpdateCommand();
    }

    public void testSetupColumnsAndValues(){
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("name", "Maria");
        values.put("Age", 23);
        values.put("Weight", 174.3);
        String expected = "name = 'Maria', Age = 23, Weight = 174.3";
        assertTrue(expected.equals(mUpdate.setupColumnAndValuesForUpdate(values)));
    }

    public void test1(){
        String expected = "UPDATE myTable SET columnName = 'value' WHERE "+ SQLConstants.ID + " = 5;";
        mUpdate.setTableName(mTableName);
        mUpdate.newValue("columnName", "value");
        mUpdate.where(SQLConstants.ID,5);
        assertTrue(expected.equals(mUpdate.getSQLStatement()));
    }

    public void test2(){
        String expected = "UPDATE myTable SET columnName =" +
                " 'value', int = 5, double = 3.33 WHERE " + SQLConstants.ID+ " = 10;";
        mUpdate.setTableName(mTableName);
        mUpdate.newValue("columnName", "value");
        mUpdate.newValue("int", 5);
        mUpdate.newValue("double", 3.33);
        mUpdate.where(SQLConstants.ID,10);
        //Log.e("inside Update", mUpdate.getSQLStatement());
        assertTrue(expected.equals(mUpdate.getSQLStatement()));

    }



}

