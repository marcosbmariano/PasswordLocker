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


/**
 * Created by marcos on 11/24/14.
 */
public class Insert {

    private final SQLInsertCommand mSQLInsert;

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
                Log.d("DATABASE TRANSACTION", " Insert model " +
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
        mSQLInsert.execute();
    }

    private static ContentValues getContentValues(Model model) {
        ContentValues cv = new ContentValues();

        Field[] fieldsList = model.getClass().getDeclaredFields();
        for (Field field : fieldsList) {
            Column col = field.getAnnotation(Column.class);
            if (null != col) {
                SetContentValue.setValueContentValues(cv, field, col,model);
            }
        }
        return cv;
    }

}
