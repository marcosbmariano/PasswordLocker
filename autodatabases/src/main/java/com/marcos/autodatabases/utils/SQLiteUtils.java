package com.marcos.autodatabases.utils;

import com.marcos.autodatabases.annotations.Column;
import com.marcos.autodatabases.annotations.HasMany;
import com.marcos.autodatabases.annotations.Table;
import com.marcos.autodatabases.models.Model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by marcos on 11/13/14.
 */
// Noninstantiable utility class
public class SQLiteUtils {

    private static final String INTEGER = "INTEGER";
    private static final String TEXT = " TEXT";

    private SQLiteUtils(){
        throw new AssertionError();
    }


    public static List<String> getSQLCommands(Class<? extends Model> aClass) {
        return generateSQLCommands(aClass);
    }

    //move to model info
    public static String getTableName(Class<? extends Model> aClass) {        //checked OK
        return getTableFromClass(aClass);
    }


    public static HashMap<String, Class<? extends Model>> getRelationalTables(
            Class<? extends Model> aClass) {

        HashMap<String, Class<? extends Model>> relationalMap = new HashMap<>();
        Field[] fieldsList = aClass.getDeclaredFields();

        for (Field field : fieldsList) {
            HasMany hasMany = field.getAnnotation(HasMany.class);
            if (hasMany != null) {
                String relationalTable = getTableName(aClass) + getTableFromHasMany(field);
                String className = ReflectionUtils.getClassNameFromArrayList(field);
                Class<? extends Model> modelClass = ReflectionUtils.getModelClassFromString(className);
                relationalMap.put(relationalTable, modelClass);
            }
        }
        return relationalMap;
    }


    private static List<String> generateSQLCommands(Class<? extends Model> aClass) {
        List<String> SQLList = new ArrayList<String>();
        Field[] fieldsList = aClass.getDeclaredFields();
        String columnFields = "";

        for (Field field : fieldsList) {

            Column fieldAnnot = field.getAnnotation(Column.class);
            if (fieldAnnot != null) {

                columnFields = mountField(fieldAnnot, field, columnFields);

            } else {
                HasMany hasMany = field.getAnnotation(HasMany.class);
                if (hasMany != null) {
                    SQLList.add(getSQLCommnadFromHasMany(aClass, hasMany, field));
                }
            }
        }
        SQLList.add(setupSQLString(getTableName(aClass), columnFields));
        return SQLList;
    }

    private static String mountField(Column fieldAnnot, Field field, String old) {
        String result = old;
        result += ", ";
        result += fieldAnnot.name();
        result += " ";
        result += getFieldType(field);
        if (fieldAnnot.notNull()){
            result += "  NOT NULL ";
        }

        if (fieldAnnot.unique()){
            result += " UNIQUE ";
        }
        return result;
    }

    private static String setupSQLString(String tableName, String fields) {
        final String createTable = "CREATE TABLE ";
        final String ID_SETUP = " ( id  INTEGER PRIMARY KEY AUTOINCREMENT";
        final String CLOSE = " );";

        return createTable + tableName + ID_SETUP + fields + CLOSE;
    }


    public static String getTableFromHasMany(Field field) {
        String table = "";
        String className = "";
        className = ReflectionUtils.getClassNameFromArrayList(field);
        Class<?> aClass = ReflectionUtils.getClassFromString(className);

        boolean isSubClass = false;

        if (aClass != null) {
            isSubClass = ReflectionUtils.isModelSubclass(aClass);
        }

        if (isSubClass) {
            table = getTableFromClass(aClass);
        }
        return table;
    }


    public static String getTableFromClass(Class<?> aClass) { //checked
        String tableName = "";
        Table tableAnnot = aClass.getAnnotation(Table.class);
        if (tableAnnot != null) {
            tableName = tableAnnot.name();
        } else {
            throw new IllegalArgumentException("The table name on a Model object must be defined! ");
        }

        return tableName;
    }

    private static String getSQLCommnadFromHasMany(Class<? extends Model> aClass, HasMany hasMany, Field field) {
        //todo must be improved
        String firstTableName = getTableFromClass(aClass);
        String secondTableName = getTableFromHasMany(field);
        String firstForeignKey = aClass.getSimpleName() + "Id ";//generate first foreign key
        String secondForeignKey = "";

        secondForeignKey = ReflectionUtils.getSimpleClassName(
                ReflectionUtils.getClassNameFromArrayList(field)) + "Id ";

        String fields = ", ";
        fields += firstForeignKey + INTEGER;
        fields += ", ";
        fields += secondForeignKey + INTEGER;
        fields += ", ";
        fields += generateForeignKeyCommand(firstForeignKey, firstTableName);
        fields += ", ";
        fields += generateForeignKeyCommand(secondForeignKey, secondTableName);

        return setupSQLString(firstTableName + secondTableName, fields);
    }


    private static String generateForeignKeyCommand(String foreignKey, String tableName) {
        String command = "";
        command = "FOREIGN KEY ( " + foreignKey + " ) REFERENCES " + tableName
                + "(id)";

        return command;
    }

    private static String getFieldType(Field field) {
        final String REAL = "REAL ";
        String index = field.getType().getName();
        switch (index) {
            case "java.lang.Byte":
            case "byte":
            case "java.lang.Integer":
            case "int":
            case "java.lang.Short":
            case "short":
            case "java.lang.Long":
            case "long":
            case "java.lang.Boolean":
            case "boolean":
                return INTEGER;

            case "java.lang.Float":
            case "float":
            case "java.lang.Double":
            case "double":
                return REAL;

            case "java.lang.Character":
            case "char":
            case "java.lang.String":
                return TEXT;

            default:
                return "ERROR ERROR ERROR ERROR ERROR ERROR !";
            //TODO fix for other types, like BLOB...

        }

//		TEXT,NUMERIC,INTEGER,REAL,NONE


    }


}
