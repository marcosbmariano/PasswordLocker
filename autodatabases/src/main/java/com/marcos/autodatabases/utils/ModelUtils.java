package com.marcos.autodatabases.utils;

import android.database.Cursor;

import com.marcos.autodatabases.annotations.Column;
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
        return aClass.getSimpleName() + "Id";
    }


    public static HashMap<String, Object> getColumnAndValues(Model model) {
        Field[] fieldsList = model.getClass().getDeclaredFields();
        HashMap<String, Object> result = new HashMap<>(fieldsList.length + 1);

        for (Field field : fieldsList) {
            Column col = field.getAnnotation(Column.class);
            if (col != null) {
                try {
                    result.put(col.name(), field.get(model));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        return result;
    }


    public static String getQueryFromFields(Model model, boolean usesAnd) {
        //todo check if AND is used, if not remove it
        String divisor = "";
        if (usesAnd) {
            divisor = " AND ";
        } else {
            divisor = " , ";
        }
        HashMap<String, String> fields = getColumnAndValuesString(model);
        String result = "";
        for (String key : fields.keySet()) {
            String value = fields.get(key);
            if (!value.equals("\'\'")) {
                if (result.isEmpty()) {
                    result = key + " = " + value;
                } else {
                    result += divisor + key + " = " + value;
                }
            }
        }

        return result;
    }

    public static HashMap<String, String> getColumnAndValuesString(Model model) {
        Field[] fieldsList = model.getClass().getDeclaredFields();
        HashMap<String, String> result = new HashMap<>(fieldsList.length + 1);

        for (Field field : fieldsList) {
            Column col = field.getAnnotation(Column.class);
            if (col != null) {
                try {
                    field.setAccessible(true);
                    result.put(col.name(), setupString(field.get(model)));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        return result;
    }



    //this method is called by getColumnAndValuesString
    //it gets the object instance returned by the method get from a field Object
    //and prepares it to a correct SQL interpretation of its value
    private static String setupString(Object obj) {

        String singleQuote = "\'";
        String result = "";
        String value;
        if (obj != null){
            value = obj.toString();
        } else{
            value = "NULL";
        }

        if ((obj instanceof String) ||(obj instanceof Character)) {
            //if its an instance of a String or a Char, surrounds it by a single quote
            result = singleQuote + value + singleQuote;

        } else if( obj instanceof Boolean) {
            //if it's a boolean converts into 1 for true, and 0 for false
            result = "" + (((Boolean) obj) ? 1 : 0);
        }else{
            result = value;
        }
        return result;
    }

    public static List<Model> getChildren(Model model, Class<? extends Model> aClass) { //ok
        String childTable = SQLiteUtils.getTableFromClass(aClass);
        String relationsTable = model.getTableName() + childTable;
        List<Model> childList = new ArrayList<>();

        if (model.hasRelationWith(relationsTable)) {

            Cursor cv = Select.all().from(relationsTable).
                    where(model.getColumnOnRelational(), model.getId()).executeForCursor();

            if (cv.getCount() > 0) {
                String childRelationalColumn = ModelUtils.getClassColumnOnRelational(aClass);

                while (cv.moveToNext()) {
                    int childId = cv.getInt(cv.getColumnIndex(childRelationalColumn));
                    childList.add(Model.getModel(aClass, childId));
                }
            }
            cv.close();
        }
        return childList;
    }

    public static HashMap<Integer, Integer> getChildrenIdsMap(String relationalTable, Model model) {

        HashMap<Integer, Integer> relationalIds = new HashMap<>();

        Cursor cv = Select.all().from(relationalTable).
                where(model.getColumnOnRelational(), model.getId()).executeForCursor();

        String childColumn = model.getChildColumnOnRelational(relationalTable);
        while (cv.moveToNext()) {
            int childId = cv.getInt(cv.getColumnIndex(childColumn));
            int relId = cv.getInt(cv.getColumnIndex(Model.ID));
            relationalIds.put(childId, relId);
        }
        cv.close();
        DatabaseHelper.getInstance().closeDatabase();

        return relationalIds;
    }



    public static Model buildModel(Class<? extends Model> aClass, Cursor cv, Field[] fields) {

        Model model = null;
        Field[] fieldz = fields;
        try {
            model = aClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        fieldz = aClass.getDeclaredFields();

        if (model != null) {
            model.setId(cv.getInt(cv.getColumnIndex("id")));
            for (Field field : fieldz) {
                Column col = field.getAnnotation(Column.class);
                if (col != null) {
                    try {
                        field.setAccessible(true);
                        field.set(model, setValueFromDatabase(cv, field, col));

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return model;
    }

    private static Object setValueFromDatabase(Cursor cv, Field field, Column col) {
        String index = field.getType().getName();
        switch (index) {
            case "java.lang.Byte":
            case "byte":
                return (byte)cv.getInt(cv.getColumnIndex(col.name()));
            case "java.lang.Integer":
            case "int":
                return cv.getInt(cv.getColumnIndex(col.name()));

            case "java.lang.Short":
            case "short":
                return cv.getShort(cv.getColumnIndex(col.name()));

            case "java.lang.Long":
            case "long":
                return cv.getLong(cv.getColumnIndex(col.name()));

            case "java.lang.Float":
            case "float":
                return cv.getFloat(cv.getColumnIndex(col.name()));

            case "java.lang.Double":
            case "double":
                return cv.getDouble(cv.getColumnIndex(col.name()));

            case "java.lang.Boolean":
            case "boolean":
                return cv.getInt(cv.getColumnIndex(col.name())) != 0;

            case "java.lang.Character":
            case "char":
                //getString .charAt(0)
                return cv.getString(cv.getColumnIndex(col.name())).charAt(0);

            case "java.lang.String":
                return cv.getString(cv.getColumnIndex(col.name()));

            default:
                return "ERROR ERROR ERROR ERROR ERROR ERROR !";
            //TODO fix for other types, like BLOB...

        }

    }


}
