package com.marcos.autodatabases.sql;

import android.os.AsyncTask;

import com.marcos.autodatabases.utils.DatabaseHelper;

/**
 * Created by mark on 5/4/15.
 */
public class SQLExecutor extends AsyncTask<SQLCommandBase,Void,  Void> {
    private DatabaseHelper mDatabaseHelper;

    SQLExecutor(DatabaseHelper helper){
        mDatabaseHelper = helper;
    }


    @Override
    protected Void doInBackground(SQLCommandBase... params) {
        //mDatabaseHelper = DatabaseHelper.getInstance();

        if (null == mDatabaseHelper){
            throw new NullPointerException("Inside AsynkTask");
        }

        for (SQLCommandBase statement :params){
            mDatabaseHelper.executeSQL(statement.getSQLStatement());
        }
        //mDatabaseHelper.closeDatabase();
        return null;

    }
}
