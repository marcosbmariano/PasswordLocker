package com.marcos.autodatabases.sql;



import android.util.Log;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by mark on 4/24/15.
 */
class SQLUpdateCommand extends SQLCommandBase {
    private final static String SET = " SET ";
    private final static String UPDATE = "UPDATE ";
    private SQLWhereHelper mWhereHelper;


    SQLUpdateCommand(){
        mWhereHelper = new SQLWhereHelper();
        appendToStatement(UPDATE);
    }

    void where(String column, Object value){
        mWhereHelper.where(column, value);
    }

    void newValue(String columnName, Object value){
        insertColumnsAndValues(columnName,value);
    }

    String getColumnsAndValuesStatement(){
        return setupColumnAndValuesforUpdate(getColumnsAndValues());
    }

    void addMapColumnsAndValues(Map<String, Object> map){
        Log.e("Inside SQLUpdate", "how many " + map.size());
        insertColumnsAndValues(map);
    }


    String setupColumnAndValuesforUpdate(Map<String, Object> map){
        StringBuffer result = new StringBuffer(150);
        Iterator<String> columns = map.keySet().iterator();

        while(columns.hasNext()){
            String column = columns.next();

            result.append(insertEqualsInBetween(column, map.get(column)));

            if (columns.hasNext()){
                result.append(", ");
            }
        }
        result.trimToSize();
        return result.toString();
    }

    private String insertEqualsInBetween(String column, Object value){
        return column + " = " + SQLStatementUtils.setupValueForStatement( value );
    }

    @Override
    void finalizeStatement() {
        appendToStatement(getTableName());
        appendToStatement(SET);
        appendToStatement( getColumnsAndValuesStatement() );
        appendToStatement(mWhereHelper.getWhereStatement());
    }
}
