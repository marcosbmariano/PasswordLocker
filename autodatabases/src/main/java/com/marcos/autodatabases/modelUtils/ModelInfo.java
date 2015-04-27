package com.marcos.autodatabases.modelUtils;

import com.marcos.autodatabases.models.Model;
import com.marcos.autodatabases.utils.SQLiteUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by marcos on 11/13/14.
 * this class stores info related to each class that extends Model
 *
 */
public class ModelInfo {

    private final String  mClassName; //stores full name;
    private final String mTableName;
    private final String mSimpleClassName;
    private final List<String> mModelSchemas; //is it really final???
    private final HashMap<String, Class<? extends Model>> mRelationalTables; //private??? //is it really final?
    private final Class<? extends Model> mThisClass;


    public ModelInfo(Class<? extends Model> aClass) {
        mThisClass = aClass;
        mClassName = aClass.getName();
        mSimpleClassName = aClass.getSimpleName();
        mTableName = SQLiteUtils.getTableName(aClass);
        mModelSchemas = SQLiteUtils.getSQLCommands(aClass);
        mRelationalTables = SQLiteUtils.getRelationalTables(aClass);
    }


    public Class<? extends Model> getModelClass() {
        return mThisClass;
    }

    public String getClassName() {
        return mClassName;
    }

    public String getTableName() {
        return mTableName;
    }

    public HashMap<String, Class<? extends Model>> getRelationalTables() {
        return mRelationalTables;
    }

    public List<String> getSchemas() {
        return mModelSchemas;
    }

    public boolean hasRelations() {
        return !mRelationalTables.isEmpty();
    }

    public boolean hasRelationsWith(Model model) {
        boolean result = false;
        if (hasRelations()) {
            result = mRelationalTables.containsValue(model.getClass());
        }
        return result;
    }

    public boolean hasRelationsWith(String tableName) {
        boolean result = false;
        if (hasRelations()) {
            result = mRelationalTables.containsKey(tableName);
        }
        return result;
    }

    //returns the column name related to the child class of the relational table
    public String getChildRelationalColumn(String relationaltable) {
        return mRelationalTables.get(relationaltable).getSimpleName() + "Id";
    }

    //returns the column name related to this model
    public String getRelationalColumn() {
        return mSimpleClassName + "Id";
    }

}