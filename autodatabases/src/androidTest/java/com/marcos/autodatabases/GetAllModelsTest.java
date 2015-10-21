package com.marcos.autodatabases;

import android.test.AndroidTestCase;
import android.util.Log;

import com.marcos.autodatabases.models.Account;

import java.util.List;

/**
 * Created by mark on 3/13/15.
 */
public class GetAllModelsTest extends AndroidTestCase{
    private Account account;
    private List<Account> accounts;
    private final int loop = 10;



    public void testGetAll(){

        for ( int i = 0; i < loop; i++){
            account = new Account("Joao" + i, "Paulo " + i);
            account.save();
        }

        accounts = Account.getAllAccounts();

        assertEquals(loop, accounts.size());

        for( Account acc : accounts){
            Log.d("GetAllTest Account ", "name" + acc.getName() +  ", Id " + acc.getId());
            acc.delete();
        }

    }


}
