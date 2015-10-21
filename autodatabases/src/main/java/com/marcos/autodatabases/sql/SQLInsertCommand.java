package com.marcos.autodatabases.sql;


import java.util.Iterator;
import java.util.List;

/**
 * Created by mark on 4/27/15.
 */
class SQLInsertCommand extends SQLCommandBase {
    private final static String INSERT_INTO = "INSERT INTO ";
    private final static String VALUES = "VALUES";

    SQLInsertCommand(){
        appendToStatement(INSERT_INTO);
    }


    void insertValue(String column, Object value){
        insertColumnsAndValues(column, value);
    }

    private String getColumnAndValuesForInsert(){
        StringBuilder result= new StringBuilder(150);
        result.append(getColumnsAsString());
        result.append(VALUES);
        result.append(getValuesAsString());
        return result.toString();
    }

    private String getColumnsAsString(){
        String columnsList = getColumns().toString();
        int length = columnsList.length();
        return insertBetweenParentheses( columnsList.substring(1, length -1) );
    }


    private String getValuesAsString(){
        StringBuilder result = new StringBuilder(150);
        List<Object> valuesList = getValues();
        Iterator<Object> iterator = valuesList.iterator();

        while( iterator.hasNext()){
            result.append(SQLStatementUtils.setupValueForStatement(iterator.next()));

            if (iterator.hasNext()){
                result.append(" , ");
            }
        }
        return  insertBetweenParentheses(result.toString());
    }

    private String insertBetweenParentheses(String text){
        return " ( " + text + " ) ";
    }

    @Override
    void finalizeStatement() {
        appendToStatement(getTableName());
        appendToStatement( getColumnAndValuesForInsert());
    }
}
