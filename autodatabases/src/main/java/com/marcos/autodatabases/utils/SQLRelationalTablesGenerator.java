package com.marcos.autodatabases.utils;


import com.marcos.autodatabases.annotations.HasMany;
import com.marcos.autodatabases.models.Model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.marcos.autodatabases.utils.SQLConstants.*;
/**
 * Created by mark on 5/1/15.
 */
class SQLRelationalTablesGenerator {
    private String mFirstTableName;
    private String mSecondTableName;
    private String mRelationalTableName;
    private String mFirstForeignKey;
    private String mSecondForeignKey;

    private static SQLRelationalTablesGenerator mGenerator;


    static List<String> getRelationalTablesSchemaList(Class<? extends Model> aClass){

        Field[] fieldsList = aClass.getDeclaredFields();
        List<String> result = new ArrayList<>();

        for (Field field : fieldsList) {
            HasMany hasMany = field.getAnnotation(HasMany.class);
            if (hasMany != null) {
                mGenerator = new SQLRelationalTablesGenerator();
                result.add(mGenerator.generateRelationalSchema(aClass,field));
            }
        }
        return result;
    }

    private String generateRelationalSchema(Class<? extends Model> aClass, Field field) {
        setTablesNames(aClass, field);
        setForeignKeys(aClass, field);
        return CREATE_TABLE + mRelationalTableName + getColumnsStatement() + ";";
    }

    private String getColumnsStatement(){
        List<String> columns = new ArrayList<>();
        columns.add(PRIMARY_KEY);
        columns.add(mFirstForeignKey + INTEGER);
        columns.add(mSecondForeignKey + INTEGER);
        columns.add(generateForeignKeyCommand(mFirstForeignKey, mFirstTableName));
        columns.add(generateForeignKeyCommand(mSecondForeignKey, mSecondTableName));

        return "( " + asStringAndTrimBraquets(columns) + " )";
    }

    private String asStringAndTrimBraquets(List<String> list){
        String result = list.toString();
        int length = result.length();
        return result.substring(1, length -1);
    }

    private void setTablesNames(Class<? extends Model> aClass, Field field){
        mFirstTableName = SQLiteUtils.getTableNameFromClass(aClass);
        mSecondTableName = SQLiteUtils.getTableNameFromHasManyField(field);
        mRelationalTableName = mFirstTableName + mSecondTableName;
    }

    private void setForeignKeys(Class<? extends Model> aClass, Field field){
        mFirstForeignKey = aClass.getSimpleName() + ID;
        mSecondForeignKey = ReflectionUtils.getSimpleClassName(
                ReflectionUtils.getClassNameOfFieldUnderHasMany(field)) + ID;
    }


    private static String generateForeignKeyCommand(String foreignKey, String tableName) {
        return "FOREIGN KEY ( " + foreignKey + " ) REFERENCES " + tableName
                + "(" + ID +")";
    }

}
