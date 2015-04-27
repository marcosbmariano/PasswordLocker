package com.marcos.autodatabases.sql;

/**
 * Created by mark on 4/24/15.
 */
public class SQLDeleteCommand extends SQLCommandBase {
    private final static String FROM = "FROM ";
    private final static String DELETE = "DELETE ";
    private String whereStatement = "";

    SQLDeleteCommand(){
        appendToStatement(DELETE);
    }

    void whereId(int id){
        where(ID, id);
    }

    void where(String column, Object value){
        whereStatement = " WHERE " + column + " = " + String.valueOf(value);
    }

    @Override
    void finalizeStatement() {
        appendToStatement(FROM);
        appendToStatement(getTableName());
        appendToStatement(whereStatement);
    }
}
