package com.marcos.autodatabases.sql;

/**
 * Created by mark on 4/28/15.
 */
class SQLSelectStatement extends SQLCommandBase {
    private static String SELECT = "SELECT ";
    private static String FROM = " FROM ";
    private static String BETWEEN_SELECT_AND_FROM = "*";
    private SQLWhereHelper mWhereHelper;

    SQLSelectStatement(){
        appendToStatement(SELECT);
        mWhereHelper = new SQLWhereHelper();
    }

    void where(String column, Object value){
        mWhereHelper.where(column, value);
    }

    void column(String column){
        BETWEEN_SELECT_AND_FROM = column;
    }


    @Override
    void finalizeStatement() {
        appendToStatement(BETWEEN_SELECT_AND_FROM);
        appendToStatement(FROM);
        appendToStatement(getTableName());
        appendToStatement(mWhereHelper.getWhereStatement());
    }
}
