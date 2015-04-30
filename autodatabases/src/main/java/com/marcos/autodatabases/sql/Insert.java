package com.marcos.autodatabases.sql;

import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.marcos.autodatabases.annotations.Column;
import com.marcos.autodatabases.modelUtils.ModelsInfo;
import com.marcos.autodatabases.models.Model;
import com.marcos.autodatabases.utils.DatabaseHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by marcos on 11/24/14.
 */
public class Insert {

    private SQLInsertCommand mSQLInsert;

    private Insert() {
        mSQLInsert = new SQLInsertCommand();
    }

    public static long model(Model model) { //todo check if needs to be closed
        DatabaseHelper helper = DatabaseHelper.getInstance();
        SQLiteDatabase db = helper.getWritable();
        long result;
            try{
                result = db.insertOrThrow(model.getTableName(),
                        null, getContentValues(model));

            } catch (SQLiteConstraintException e){
                result = -1;
                Log.e("DATABASE TRANSACTION", " Insert model " +
                        e.getMessage()); //do not remove
            }
        db.close();
        helper.closeDatabase();

        return result;
    }

    public static Insert into(String tableName) {
        Insert mInsert = new Insert();
        mInsert.mSQLInsert.setTableName(tableName);
        return mInsert;
    }

    public static Insert into(Class<? extends Model> aClass) {
        Insert mInsert = new Insert();
        mInsert.mSQLInsert.setTableName(ModelsInfo.getInstance().getTableFromClass(aClass));
        return mInsert;
    }

    public final Insert columnAndValues(String column, Object values) {
        mSQLInsert.insertValue(column, values);
        return this;
    }

    public final void execute() {
        Log.d("DATABASE TRANSACTIONS ", mSQLInsert.getSQLStatement());
        DatabaseHelper helper = DatabaseHelper.getInstance();
        helper.executeSQL(mSQLInsert.getSQLStatement());
    }

    private static ContentValues getContentValues(Model model) { //TODO private stat
        ContentValues cv = new ContentValues();

        Field[] fieldsList = model.getClass().getDeclaredFields();
        for (Field field : fieldsList) {
            Column col = field.getAnnotation(Column.class);
            if (col != null) {
                try {
                    cv = setValue(cv, field, col, model);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return cv;
    }


    private static ContentValues setValue(ContentValues cv, Field field, Column col, Model model) {
        field.setAccessible(true);

        try {
            if (field.get(model) == null){
                cv.putNull(col.name());
                return cv;
            }else{
                String index = field.getType().getName();
                switch (index) {
                    case "java.lang.Integer":
                        cv.put(col.name(), Integer.parseInt(field.get(model).toString()));
                        return cv;
                    case "java.lang.Byte":
                        cv.put(col.name(),Byte.parseByte(field.get(model).toString()));
                        return cv;
                    case "java.lang.Short":
                        cv.put(col.name(),Short.parseShort(field.get(model).toString()));
                        return cv;
                    case "byte":
                    case "short":
                    case "int":
                        cv.put(col.name(), field.getInt(model));
                        return cv;
                    case "java.lang.Long":
                        cv.put(col.name(),Long.parseLong(field.get(model).toString()));
                        return cv;

                    case "long":
                        cv.put(col.name(), field.getLong(model));
                        return cv;

                    case "java.lang.Float":
                        cv.put(col.name(),Float.parseFloat(field.get(model).toString()));
                        return cv;

                    case "float":
                        cv.put(col.name(), field.getFloat(model));
                        return cv;
                    case "java.lang.Double":
                        cv.put(col.name(),Float.parseFloat(field.get(model).toString()));
                        return cv;

                    case "double":
                        cv.put(col.name(), field.getDouble(model));
                        return cv;

                    case "java.lang.Boolean":
                        cv.put(col.name(),Boolean.parseBoolean(field.get(model).toString()));
                        return cv;
                    case "boolean":
                        cv.put(col.name(), field.getBoolean(model)? 1 : 0);
                        return cv;

                    case "java.lang.Character":
                    case "char":
                        cv.put(col.name(), field.get(model).toString().substring(0, 1));
                        return cv;

                    case "java.lang.String":
                        cv.put(col.name(), field.get(model).toString());
                        return cv;

                    default:
                        cv.putNull(col.name());
                        return cv;
                }

            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return cv;
    }
}
