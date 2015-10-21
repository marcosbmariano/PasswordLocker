package com.marcos.autodatabases;

import android.test.AndroidTestCase;

import com.marcos.autodatabases.models.Account;
import com.marcos.autodatabases.models.Address;
import com.marcos.autodatabases.models.BoxedTypes;
import com.marcos.autodatabases.models.Costumer;
import com.marcos.autodatabases.models.User;
import com.marcos.autodatabases.utils.DatabaseHelper;

/**
 * Created by mark on 3/10/15.
 */
public class DatabaseHelperTest extends AndroidTestCase {

    private DatabaseHelper helper;

    private final String defaultDatabaseName = "my_database.db";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        DatabaseHelper.setupContext(getContext());
        helper = DatabaseHelper.getInstance();
    }

    public void testCreateDatabase1() {
        helper = DatabaseHelper.getInstance();
        //Fail to add any class that extends Model
        try{
            helper.createDatabase();
            fail();
        }catch (IllegalStateException e){
            //do nothing
        }

    }

    public void testCreateDatabase2() {
        helper = DatabaseHelper.getInstance();
        helper.addModel(Costumer.class, User.class, Address.class, BoxedTypes.class, Account.class);
        helper.createDatabase();
        assertEquals(helper.getDatabaseName(), defaultDatabaseName, helper.getDatabaseName());
    }


}
