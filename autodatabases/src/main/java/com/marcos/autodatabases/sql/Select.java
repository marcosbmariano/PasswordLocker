package com.marcos.autodatabases.sql;

import android.database.Cursor;
import android.util.Log;

import com.marcos.autodatabases.modelUtils.ModelsInfo;
import com.marcos.autodatabases.models.Model;
import com.marcos.autodatabases.utils.DatabaseHelper;
import com.marcos.autodatabases.utils.ModelUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by marcos on 11/24/14.
 */
public class Select {
    private String mQuery;
    private String mTableName;

    private Select() {
        mQuery = "SELECT ";
        mTableName = "";
    }

    public static Select all() {
        return new Select();
    }

    public Select from(String tableName) {
        mQuery += " * FROM " + tableName + " ";
        mTableName = tableName;
        return this;
    }

    public Select from(Class<? extends Model> aClass) {
        mTableName = ModelsInfo.getInstance().getTableFromClass(aClass);
        mQuery += " * FROM " + mTableName + " ";
        return this;
    }

    public Select where(String column, String value) {
        mQuery += " where " + column + " = " + "'" + value + "'";
        return this;
    }

    public Select andWhere(String column, String value) {
        mQuery += " AND " + column + " = " + "'" + value + "'";
        return this;
    }

    public Select where(String column, Object value) {
        mQuery += " where " + column + " = " + value.toString();
        return this;
    }

    public Select andWhere(String column, Object value) {
        mQuery += " AND " + column + " = " + value.toString();
        return this;
    }

    public Model executeSingle() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        Log.d("DATABASE TRANSACTION ", mQuery + " ;");
        Cursor cv = helper.query(mQuery + " ;");

        Class<? extends Model> aClass = ModelsInfo.getInstance()
                .getClassFromTable(mTableName);

        Model model = null;
        if (cv != null && cv.moveToFirst()) {
            model = ModelUtils.buildModel(aClass, cv, null);
            cv.close();
        }
        helper.closeDatabase();

        return model;
    }

    public List<Model> execute() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        Log.d("DATABASE TRANSACTION ", mQuery + " ;");
        Cursor cv = helper.query(mQuery + " ;");

        Class<? extends Model> aClass =
                ModelsInfo.getInstance().getClassFromTable(mTableName);

        List<Model> items = new ArrayList<Model>();
        Field[] fields = aClass.getDeclaredFields();

        while (cv.moveToNext()) {
            items.add(ModelUtils.buildModel(aClass, cv, fields));
        }
        cv.close();
        helper.closeDatabase();
        return items;
    }


    public Cursor executeForCursor() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        Log.d("INSIDE SELECT ", mQuery + " ;");

        return helper.query(mQuery + " ;");
    }


}
