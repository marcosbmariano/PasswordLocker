package com.marcos.autodatabases.models;

import com.marcos.autodatabases.modelUtils.ModelInfo;
import com.marcos.autodatabases.modelUtils.ModelsInfo;
import com.marcos.autodatabases.sql.Delete;
import com.marcos.autodatabases.sql.Insert;
import com.marcos.autodatabases.sql.Select;
import com.marcos.autodatabases.sql.Update;
import com.marcos.autodatabases.utils.ModelUtils;
import com.marcos.autodatabases.utils.SQLConstants;
import java.util.HashMap;
import java.util.List;

//todo catch android.database.sqlite.SQLiteException:

/**
 * Created by marcos on 11/13/14.
 */
public class Model implements Cloneable{

    private ModelInfo mModelInfo;
    private long mId = 0;
    private static final int HASH_PRIME = 1583;


    public Model() {
        setModelInfo();
    }

    //when an model object is cloned, if was cloned after the obj being saved,
    //the clone will have the same mId as the main object, so it's necessary that
    //the clone have it's mId set to 0
    //if used, Override in the subclass
    public final Model clone() throws CloneNotSupportedException { //TODO fix this clone
        Model clone = null;
        try {
            clone = (Model)super.clone();
            clone.setId(0);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    public final long getId() {
        return mId;
    }

    public final void setId(int id) {
        mId = id;
    }

    public static Model getModel(Class<? extends Model> aClass, long id) { //ok
        return Select.from(aClass).where(SQLConstants.ID, id).executeSingle();
    }

    public static List<Model> getModels(Class<? extends Model> aClass) { //ok
        return Select.from(aClass).execute();
    }

    @Override
    public final boolean equals(Object o){
        boolean result = false;
        if ( o != null && o.getClass().equals(this.getClass()) ){
            if (((Model) o).getId() == this.getId()){
                result = true;
            }
        }
        return result;
    }

    @Override
    public final int hashCode() {
        return (HASH_PRIME * ( this.getClass().hashCode() + (int)this.getId() ) );
    }

    public static void delete(Class<? extends Model> modelClass, long modelId){
        Model model = getModel(modelClass, modelId);
        model.delete();
    }

    public final void delete() { //ok

        Delete delete = Delete.from(getClass()).whereId(getId());

        if (hasModelRelations()) {
            Delete.deleteChildren(this);
            Delete.deleteFromRelational(this);
            delete.execute();
        } else {
            Delete.deleteFromRelational(this);
            delete.execute();
        }
    }

    public final List<Model> getChildren(Class<? extends Model> aClass) { //ok
        return ModelUtils.getChildren(this, aClass);
    }

    public final HashMap<String, Class<? extends Model>> getRelationalTables() { //should be private?
        return mModelInfo.getRelationalTables();
    }


    public final String getColumnOnRelational() { //ok
        return mModelInfo.getRelationalColumn();
    }

    public final String getChildColumnOnRelational(String relationalTable) {
        return mModelInfo.getChildRelationalColumn(relationalTable);
    }

    public final boolean hasRelationWith(String tableName) { //ok
        return mModelInfo.hasRelationsWith(tableName);
    }

    public final boolean hasRelationWith(Model model) {
        return mModelInfo.hasRelationsWith(model);
    }

    public final boolean hasModelRelations() { //ok
        return mModelInfo.hasRelations();
    }

    protected void save() {
        saveOrUpdate();
    }

    private void saveOrUpdate(){
        if (0 == mId) {
            mId = addNewModel();
        } else {
            update();
        }
    }

    private void update() {
        Update.from(getTableName())
                .getValuesFromModel(this)
                .whereId(getId())
                .execute();
    }

    private long addNewModel() {
        return Insert.model(this);
    }

    public final void addChildModel(Model child) {
        if (hasRelationWith(child)) {
            String tableName = getRelationalTableName(child);

            Insert.into(tableName).columnAndValues(getColumnOnRelational(), getId()).
                    columnAndValues(child.getColumnOnRelational(), child.getId()).execute();
        }
    }

    private String getRelationalTableName(Model model){
        String parentTable = "";
        String childTable = "";
        if (hasRelationWith(model)) {
            parentTable = this.getTableName();
            childTable = model.getTableName();
        }
        return parentTable + childTable;
    }

    private void setModelInfo() {
        mModelInfo = ModelsInfo.getInstance().getModelInfo(getClass());
    }

    public final String getTableName() {
        return mModelInfo.getTableName();
    }


    //Returns the class, the table name and the object mId in the table
    @Override
    public String toString() {
        return getClass().getSimpleName() + ", table: " + getTableName() + ", mId: " + mId;
    }
}