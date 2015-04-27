package com.marcos.autodatabases.sql;

import com.marcos.autodatabases.models.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by mark on 4/24/15.
 */
public abstract class SQLCommandBase {
    private StringBuilder mSQLStatement;
    private String mTableName;
    private Map<String, Object> mModelColumnsAndValues;
    public final String ID = Model.ID;
    private boolean mIsStatementFinalized = false;


    SQLCommandBase(){
        mModelColumnsAndValues = new HashMap<>();
        mSQLStatement = new StringBuilder(150);
    }

    public void setTableName(String tableName){
        mTableName = tableName;
    }

    protected String getTableName(){
        return mTableName;
    }
    protected void appendToStatement(String statement){
        mSQLStatement.append(statement);
    }

    public String getSQLStatement(){
        if ( !mIsStatementFinalized ){
            finalizeStatement();
            addSemiColonToStatement();
            mSQLStatement.trimToSize();
            mIsStatementFinalized = true;
        }
        return mSQLStatement.toString();
    }
    private void addSemiColonToStatement(){
        appendToStatement(";");
    }
    abstract void finalizeStatement();

    public void insertColumnsAndValues(String column, Object value){
        mModelColumnsAndValues.put(column, value);
    }

    public void insertColumnsAndValues(Map<String, Object> map){
        mModelColumnsAndValues.putAll(map);
    }

    public Map<String, Object> getColumnsAndValues(){
        return mModelColumnsAndValues;
    }

    public List getColumns(){
        return setColumnsAsList(mModelColumnsAndValues);
    }

    protected List<String> setColumnsAsList(Map<String, Object> map){
        List<String> result = new ArrayList<>();
        Iterator<String> iterator = map.keySet().iterator();

        while(iterator.hasNext()){
            result.add(iterator.next());
        }
        return result;
    }
    public List<Object> getValues(){
        return setValuesAsList(mModelColumnsAndValues);
    }

    public List<Object> setValuesAsList(Map<String, Object> map){
        List<Object> result = new ArrayList<>();
        Iterator iterator = map.values().iterator();

        while(iterator.hasNext()){
            result.add(iterator.next());
        }
        return result;
    }
}












//DELETE FROM database_name.table_name;
//DELETE FROM database_name.table_name WHERE id = 42;
//INSERT INTO database_name.table_name ( col1, col2 ) VALUES ( val1, val2 );
//INSERT INTO table_name VALUES ( val1, val2, val3... );
//SELECT * FROM tbl;
//SELECT name FROM employees WHERE employee_id = 54923;
//UPDATE database_name.table_name SET col5 = val5, col2 = val2 WHERE id = 42;