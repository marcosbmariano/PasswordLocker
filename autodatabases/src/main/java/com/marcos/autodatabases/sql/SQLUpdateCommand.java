package com.marcos.autodatabases.sql;

/**
 * Created by mark on 4/24/15.
 */
public class SQLUpdateCommand extends SQLCommandBase {
    private final static String SET = "SET ";
    private final static String UPDATE = "UPDATE ";
    private String whereStatement = "";


    SQLUpdateCommand(){
        appendToStatement(UPDATE);
    }

    void whereId(int id){
        where(ID, id);
    }

    void where(String column, Object value){
        whereStatement = " WHERE " + column + " = " + String.valueOf(value);
    }



    @Override
    void finalizeStatement() {

    }
}
