package com.marcos.autodatabases.sql;

/**
 * Created by mark on 4/28/15.
 */
class SQLWhereHelper {
    private String mWhereStatement;

    SQLWhereHelper(){
        mWhereStatement = "";
    }

    void where(String column, Object value){

        mWhereStatement = " WHERE " + column + " = " +
                 SQLStatementUtils.setupValueForStatement(value);

    }

    String getWhereStatement(){
        return mWhereStatement;
    }
}
