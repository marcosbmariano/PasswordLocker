package com.marcos.autodatabases.sql;

import android.util.Log;
import com.marcos.autodatabases.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mark on 4/24/15.
 */
abstract class SQLCommandBase {
    private final StringBuilder mSQLStatement;
    private String mTableName;
    private final Map<String, Object> mModelColumnsAndValues;
    private boolean mIsStatementFinalized = false;


    SQLCommandBase(){
        mModelColumnsAndValues = new LinkedHashMap<>();
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
        if ( hasTableName()){
            if ( !mIsStatementFinalized ){
                finalizeStatement();
                addSemiColonToStatement();
                mSQLStatement.trimToSize();
                mIsStatementFinalized = true;
            }
            return mSQLStatement.toString();

        }else{
            throw new IllegalStateException("A SQL statement must have a table name, "
                    + this.getClass().getSimpleName());
        }

    }

    private boolean hasTableName(){
        return ( null != mTableName && !mTableName.isEmpty() );
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

    public void execute() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        Log.d("Database Transaction: ", getSQLStatement());
        helper.executeSQL(getSQLStatement());
        helper.closeDatabase();
    }

}
