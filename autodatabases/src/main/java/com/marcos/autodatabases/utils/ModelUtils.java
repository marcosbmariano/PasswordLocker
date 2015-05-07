package com.marcos.autodatabases.utils;

import android.database.Cursor;

import com.marcos.autodatabases.annotations.Column;
import com.marcos.autodatabases.modelUtils.ValueFromCursor;
import com.marcos.autodatabases.models.Model;
import com.marcos.autodatabases.sql.Select;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marcos on 11/24/14.
 */
// Noninstantiable utility class
public class ModelUtils {


    private ModelUtils(){
        throw new AssertionError();
    }

    public static String getClassColumnOnRelational(Class<? extends Model> aClass) {
        return aClass.getSimpleName() + SQLConstants.ID;
    }

    public static HashMap<String, Object> getColumnAndValues(Model model) {
        Field[] fieldsList = model.getClass().getDeclaredFields();
        HashMap<String, Object> result = new HashMap<>(fieldsList.length + 1);

        for (Field field : fieldsList) {
            field.setAccessible(true);
            Column col = field.getAnnotation(Column.class);
            if (col != null) {
                try {
                    result.put(col.name(), field.get(model));
                }catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            field.setAccessible(false);

            }
        }
        return result;
    }

    public static List<Model> getChildren(Model model, Class<? extends Model> childrenClass) { //ok

        String relationalTable = getRelationalTableName(model, childrenClass);
        List<Model> childList = new ArrayList<>();

        if (model.hasRelationWith(relationalTable)) {

            Cursor cv = getCursorFromRelational(model, relationalTable);

            if (cv.getCount() > 0) {
                String childRelationalColumn = ModelUtils.getClassColumnOnRelational(childrenClass);

                while (cv.moveToNext()) {
                    int childId = cv.getInt(cv.getColumnIndex(childRelationalColumn));
                    childList.add(Model.getModel(childrenClass, childId));
                }
            }
            cv.close();
        }
        return childList;
    }

    private static String getRelationalTableName(Model parent, Class<? extends Model> childrenClass){
        String childTable = SQLiteUtils.getTableNameFromClass(childrenClass);
        return parent.getTableName() + childTable;
    }

    private static Cursor getCursorFromRelational(Model model, String relationalTable){
        return Select.from(relationalTable)
                .where(model.getColumnOnRelational(), model.getId())
                .executeForCursor();
    }

    public static HashMap<Integer, Integer> getChildrenIdsMap(String relationalTable, Model model) {
        HashMap<Integer, Integer> relationalIds = new HashMap<>();
        Cursor cv = getCursorFromRelational(model, relationalTable);

        String childColumn = model.getChildColumnOnRelational(relationalTable);
        while (cv.moveToNext()) {
            int childId = cv.getInt(cv.getColumnIndex(childColumn));
            int relId = cv.getInt(cv.getColumnIndex(SQLConstants.ID));
            relationalIds.put(childId, relId);
        }
        cv.close();
        DatabaseHelper.getInstance().closeDatabase();

        return relationalIds;
    }

    public static Model buildModel(Class<? extends Model> aClass, Cursor cv) { //TODO and HasMany???

        Model model = createModelInstance(aClass);
        Field[] fields = model.getClass().getDeclaredFields();

        model.setId(cv.getInt(cv.getColumnIndex(SQLConstants.ID)));
        for (Field field : fields) {
            Column col = field.getAnnotation(Column.class);
            if (col != null) {

                try {
                    field.setAccessible(true);
                    field.set(model, ValueFromCursor.getValueFromCursor(cv, field, col));

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(false);
            }
        }
        return model;
    }

    private static Model createModelInstance(Class<? extends Model> aClass){
        Model result = new Model();
        try {
            result = aClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }
}
