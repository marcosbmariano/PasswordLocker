package com.marcos.autodatabases.sql;



import com.marcos.autodatabases.modelUtils.ModelsInfo;
import com.marcos.autodatabases.models.Model;
import com.marcos.autodatabases.utils.ModelUtils;
import com.marcos.autodatabases.utils.SQLConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marcos on 11/24/14.
 */
public class Delete {

    private final SQLDeleteCommand mSQLDeleteCommand;

    private Delete() {
        mSQLDeleteCommand = new SQLDeleteCommand();
    }

    public static Delete from(String tableName) {
        Delete delete = new Delete();
        delete.mSQLDeleteCommand.setTableName(tableName);
        return delete;
    }

    public static Delete from(Class<? extends Model> aClass) {
        Delete delete = new Delete();
        delete.mSQLDeleteCommand.setTableName(ModelsInfo.getInstance().getTableFromClass(aClass));
        return delete;
    }

    public Delete whereId(long id) {
        mSQLDeleteCommand.where(SQLConstants.ID, id);
        return this;
    }

    public Delete where(String column, Object value) {
        mSQLDeleteCommand.where(column, value);
        return this;
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

    public void execute(){
        mSQLDeleteCommand.execute();
    }

}
