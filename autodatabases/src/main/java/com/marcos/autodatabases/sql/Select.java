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

    private SQLSelectStatement mSQLSelect;

    private Select() {
        mSQLSelect = new SQLSelectStatement();
    }

    public static Select from(String tableName) {
        Select result = new Select();
        result.mSQLSelect.setTableName(tableName);
        return result;
    }

    public static Select from(Class<? extends Model> aClass) {
        return Select.from(ModelsInfo.getInstance().getTableFromClass(aClass));
    }

    public Select where(String column, Object value) {
        mSQLSelect.where(column, value);
        return this;
    }

    public Model executeSingle() {
        DatabaseHelper helper = DatabaseHelper.getInstance();

        Cursor cv = executeForCursor();

        Class<? extends Model> aClass = ModelsInfo.getInstance()
                .getClassFromTable(mSQLSelect.getTableName());

        Model model = null;
        if (cv != null && cv.moveToFirst()) {
            model = ModelUtils.buildModel(aClass, cv, null);
            cv.close();
        }
        helper.closeDatabase();

        return model;
    }

    public List<Model> execute() {

        Cursor cv = executeForCursor();

        Class<? extends Model> aClass =
                ModelsInfo.getInstance().getClassFromTable( mSQLSelect.getTableName());

        List<Model> items = new ArrayList<Model>();
        Field[] fields = aClass.getDeclaredFields();

        while (cv.moveToNext()) {
            items.add(ModelUtils.buildModel(aClass, cv, fields));
        }
        cv.close();

        return items;
    }

    public Cursor executeForCursor() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        Log.d("INSIDE SELECT ", mSQLSelect.getSQLStatement());

        return helper.query(mSQLSelect.getSQLStatement());
    }


}
