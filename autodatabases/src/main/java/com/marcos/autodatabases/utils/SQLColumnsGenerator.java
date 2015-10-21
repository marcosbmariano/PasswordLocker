package com.marcos.autodatabases.utils;

import com.marcos.autodatabases.annotations.Column;
import com.marcos.autodatabases.models.Model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.marcos.autodatabases.utils.SQLConstants.PRIMARY_KEY;


/**
 * Created by mark on 5/1/15.
 */
class SQLColumnsGenerator {

    private SQLColumnsGenerator(){}

    static String getColumnsStatement(Class<? extends Model> aClass) {
        List<String> columns = new ArrayList<>();
        columns.add(PRIMARY_KEY);
        columns.addAll( getColumns(aClass.getDeclaredFields()));
        return setupColumnsStatement(columns);
    }

    private static String setupColumnsStatement(List<String> columns){
        String result = columns.toString();
        int length = result.length();
        return "( " + result.substring(1, length -1) + " )";
    }

    private static List<String> getColumns(Field[] fieldsList){
        List<String> result = new ArrayList<>();

        for (Field field : fieldsList) {
            Column fieldAnnotation = field.getAnnotation(Column.class);
            if (fieldAnnotation != null) {
                result.add(mountColumn(fieldAnnotation, field));
            }
        }
        return result;
    }


    private static String mountColumn(Column fieldAnnotation, Field field) {
        return fieldAnnotation.name() +
                SQLColumnTypeSelector.getColumnType(field) +
                getConstraints(fieldAnnotation);
    }

    private static String getConstraints(Column fieldAnnotation){
        String result = "";

        if (fieldAnnotation.notNull()){
            result += " NOT NULL";
        }

        if (fieldAnnotation.unique()){
            result += " UNIQUE";
        }
        return result;
    }


}
