package com.marcos.autodatabases.sql;

/**
 * Created by mark on 4/24/15.
 */
class SQLDeleteCommand extends SQLCommandBase {
    private final static String FROM = "FROM ";
    private final static String DELETE = "DELETE ";
    private final SQLWhereHelper mWhereHelper;

    SQLDeleteCommand(){
        mWhereHelper = new SQLWhereHelper();
        appendToStatement(DELETE);
    }

    void where(String column, Object value){
        mWhereHelper.where(column, value);
    }

    @Override
    void finalizeStatement() {
        appendToStatement(FROM);
        appendToStatement(getTableName());
        appendToStatement(mWhereHelper.getWhereStatement());
    }
}
