package com.marcos.autodatabases.sql;

import android.test.AndroidTestCase;
import java.util.Map;

/**
 * Created by mark on 4/24/15.
 */
public class SQLCommandBaseTest extends AndroidTestCase {
    private SQLCommandForTest mForTest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mForTest = new SQLCommandForTest();
    }

    public void testInsertColumnsAndValues(){
        mForTest.insertColumnsAndValues("Name", "Maria");
        mForTest.insertColumnsAndValues("Age", 23);
        mForTest.insertColumnsAndValues("Weight", 3.33);
        Map map = mForTest.getColumnsAndValues();
        assertEquals(3, map.size());
        assertTrue(map.containsKey("Name"));
        assertTrue(map.containsKey("Age"));
        assertTrue(map.containsKey("Weight"));
        assertTrue(map.containsValue("Maria"));
        assertTrue(map.containsValue(23));
    }


    public void testNullTableName(){
        try{
            mForTest.getSQLStatement();
            fail();
        } catch (IllegalStateException e){
            //do nothing
        }
    }

    public void testEmptyTableName(){
        try{
            mForTest.setTableName("");
            mForTest.getSQLStatement();
            fail();
        } catch (IllegalStateException e){
            //do nothing
        }
    }

    class SQLCommandForTest extends SQLCommandBase{

        @Override
        void finalizeStatement() {

        }
    }


}



