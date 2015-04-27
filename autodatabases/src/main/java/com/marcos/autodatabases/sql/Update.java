package com.marcos.autodatabases.sql;

import android.util.Log;

import com.marcos.autodatabases.models.Model;
import com.marcos.autodatabases.utils.DatabaseHelper;
import com.marcos.autodatabases.utils.ModelUtils;

/**
 * Created by marcos on 11/24/14.
 */
public class Update {
    private String mSQLStatement;


    private Update() {
        mSQLStatement = "UPDATE ";
    }



    public void execute() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        Log.d("DATABASE TRANSACTIONS", "Update: " + mSQLStatement);
        helper.executeSQL(mSQLStatement);
        helper.closeDatabase();
    }

    public Update getValuesFromModel(Model model) {
        mSQLStatement += ModelUtils.getQueryFromFields(model, false);
        return this;
    }

    public static Update from(String tableName) {
        Update up = new Update();
        up.mSQLStatement += tableName + " SET ";
        return up;
    }

    public Update whereId(long id) {
        mSQLStatement += " WHERE " + Model.ID + " = " + id + " ;";
        return this;
    }

    public Update where(String column, String value) {
        mSQLStatement += " where " + column + " = " + "'" + value + "'";
        return this;
    }

    public Update andWhere(String column, String value) {
        mSQLStatement += " AND " + column + " = " + "'" + value + "'";
        return this;
    }

    public Update where(String column, Object value) {
        mSQLStatement += " where " + column + " = " + value.toString();
        return this;
    }

    public Update andWhere(String column, Object value) {
        mSQLStatement += " AND " + column + " = " + value.toString();
        return this;
    }


}
