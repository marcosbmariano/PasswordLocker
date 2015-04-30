package com.marcos.autodatabases.sql;

import android.util.Log;

import com.marcos.autodatabases.modelUtils.ModelsInfo;
import com.marcos.autodatabases.models.Model;
import com.marcos.autodatabases.utils.DatabaseHelper;
import com.marcos.autodatabases.utils.ModelUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marcos on 11/24/14.
 */
public class Delete {

    private SQLDeleteCommand mSQLDelete;

    private Delete() {
        mSQLDelete = new SQLDeleteCommand();

    }

    public static Delete from(String tableName) {
        Delete delete = new Delete();
        delete.mSQLDelete.setTableName(tableName);
        return delete;
    }

    public static Delete from(Class<? extends Model> aClass) {
        Delete delete = new Delete();
        delete.mSQLDelete.setTableName( ModelsInfo.getInstance().getTableFromClass(aClass));
        return delete;
    }

    public Delete whereId(long id) {
        mSQLDelete.where(Model.ID, id);
        return this;
    }

    public Delete where(String column, Object value) {
        mSQLDelete.where(column, value);
        return this;
    }

    public void execute() {
        DatabaseHelper helper = DatabaseHelper.getInstance();
        Log.d("DB DELETE TRANSACTIONS", "Delete: " + mSQLDelete.getSQLStatement());
        helper.executeSQL(mSQLDelete.getSQLStatement());
        helper.closeDatabase();

    }

    public static void deleteChildren(Model model) {
        List<Model> childrenList = new ArrayList<>();
        HashMap<String, Class<? extends Model>> relationalTables = model.getRelationalTables();

        for (String relationalTable : relationalTables.keySet()) {

            Map<Integer, Integer> ids = ModelUtils.getChildrenIdsMap(relationalTable, model);
            Class<? extends Model> childClass = relationalTables.get(relationalTable);

            for (int childId : ids.keySet()) {
                childrenList.add(Model.getModel(childClass, childId));
            }
            deleteRelationalData(relationalTable, model);
        }

        for (Model child : childrenList) {
            child.delete();
        }
    }

    public static void deleteFromRelational(Model model){

        String relational = ModelsInfo.getInstance().
                getRelationalTableFromClass(model.getClass());

        if (!relational.isEmpty()){
            Delete.deleteRelationalData(relational, model);
        }
    }

    private static void deleteRelationalData(String relationalTable, Model model) {
        Delete.from(relationalTable)
                .where(model.getColumnOnRelational(), model.getId()).execute();
    }

}
