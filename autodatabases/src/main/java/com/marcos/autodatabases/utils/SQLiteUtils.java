package com.marcos.autodatabases.utils;


import com.marcos.autodatabases.annotations.HasMany;
import com.marcos.autodatabases.annotations.Table;
import com.marcos.autodatabases.models.Model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.marcos.autodatabases.utils.SQLConstants.*;
/**
 * Created by marcos on 11/13/14.
 */
// Non instantiable utility class
public class SQLiteUtils {

    private SQLiteUtils(){
        throw new AssertionError();
    }

    public static List<String> generateSQLCommands(Class<? extends Model> aClass) {
        List<String> schemasList = new ArrayList<>();
        schemasList.add(getTableSchema(aClass));
        schemasList.addAll(SQLRelationalTablesGenerator.getRelationalTablesSchemaList(aClass));
        return schemasList;
    }

    private static String getTableSchema(Class<? extends Model> aClass){
        return CREATE_TABLE + getTableNameFromClass(aClass) +
                SQLColumnsGenerator.getColumnsStatement(aClass) + ";";
    }

    public static String getTableNameFromHasManyField(Field field) {
        String className = ReflectionUtils.getClassNameOfFieldUnderHasMany(field);
        Class<? extends Model> aClass = ReflectionUtils.getModelClassFromClassName(className);

        return getTableNameFromClass(aClass);
    }

    public static String getTableNameFromClass(Class<? extends Model> aClass) { //checked

        Table tableAnnotation = aClass.getAnnotation(Table.class);
        if (tableAnnotation != null) {
            return tableAnnotation.name();
        } else {
            throw new IllegalArgumentException(
                    "The table name on a Model object must be defined!");
        }
    }

    public static HashMap<String, Class<? extends Model>> getRelationalTablesNameAndClass(
            Class<? extends Model> aClass) {

        HashMap<String, Class<? extends Model>> relationalMap = new HashMap<>();
        Field[] fieldsList = aClass.getDeclaredFields();

        for (Field field : fieldsList) {
            HasMany hasMany = field.getAnnotation(HasMany.class);

            if (hasMany != null) {
                String relationalTable = getTableNameFromClass(aClass)
                        + getTableNameFromHasManyField(field);

                String className = ReflectionUtils.getClassNameOfFieldUnderHasMany(field);

                Class<? extends Model> modelClass =
                        ReflectionUtils.getModelClassFromClassName(className);

                relationalMap.put(relationalTable, modelClass);
            }
        }
        return relationalMap;
    }




}
