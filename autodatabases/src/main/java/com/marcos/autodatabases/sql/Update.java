package com.marcos.autodatabases.sql;

import com.marcos.autodatabases.models.Model;
import com.marcos.autodatabases.utils.ModelUtils;
import com.marcos.autodatabases.utils.SQLConstants;

/**
 * Created by marcos on 11/24/14.
 */
public class Update {
    private final SQLUpdateCommand mSQLUpdateCommand;

    Update() {
        mSQLUpdateCommand = new SQLUpdateCommand();
    }

    public Update getValuesFromModel(Model model) {
        mSQLUpdateCommand.addMapColumnsAndValues(ModelUtils.getColumnAndValues(model));
        return this;
    }

    public static Update from(String tableName) {
        Update up = new Update();
        up.mSQLUpdateCommand.setTableName(tableName);
        return up;
    }

    public Update whereId(long id) {
        mSQLUpdateCommand.where(SQLConstants.ID, id);
        return this;
    }

    public Update where(String column, Object value) {
        mSQLUpdateCommand.where(column, value);
        return this;
    }

    public void execute() {
        mSQLUpdateCommand.execute();
    }

}
