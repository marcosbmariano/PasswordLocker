package com.marcos.autodatabases.utils;

import android.test.AndroidTestCase;

import com.marcos.autodatabases.models.User;

import java.util.List;

/**
 * Created by mark on 5/1/15.
 */
public class SQLRelationalGenTest extends AndroidTestCase {



    public void test1(){
        String expected = "CREATE TABLE UsersCostumers( Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " UserId INTEGER, CostumerId INTEGER, FOREIGN KEY ( UserId ) REFERENCES Users(Id)," +
                " FOREIGN KEY ( CostumerId ) REFERENCES Costumers(Id) );";
        List<String> resultList = SQLRelationalTablesGenerator.getRelationalTablesSchemaList(User.class);

        String result = resultList.get(0);
        assertTrue(expected.equals(result));

    }
}
