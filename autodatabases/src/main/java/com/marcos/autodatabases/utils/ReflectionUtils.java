package com.marcos.autodatabases.utils;



import com.marcos.autodatabases.models.Model;
import java.lang.reflect.Field;


/**
 * Created by marcos on 11/13/14.
 */
// Non instantiable utility class
 class ReflectionUtils {

    private ReflectionUtils(){
        throw new AssertionError();
    }


    public static Class<? extends Model> getModelClassFromClassName(String className) {
        Class<? extends Model> aClass = null;
        try {
            aClass = (Class<? extends Model>) Class.forName(className); //TODO unchecked cast
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return aClass;
    }

    public static String getClassNameOfFieldUnderHasMany(Field field) {

        if ( isFieldRightType(field) ){
            return getClassNameFromField(field);
        } else {
            throw new IllegalArgumentException(
                    "Fields under @HasMany must be contained by a ArrayList");
            //TODO fix this , should create a table white a representation of the Object
        }
    }

    private static String getClassNameFromField(Field field){
        String type = field.getType().getName();
        String className = field.getGenericType().toString();
        return className.substring(type.length() + 1, className.length() - 1);
    }

    private static boolean isFieldRightType(Field field){
        final String ARRAY_LIST = "java.util.ArrayList";
        final String LIST = "java.util.List";
        String type = field.getType().getName();

        return (type.equals(ARRAY_LIST)) || (type.equals(LIST));
    }

    public static String getSimpleClassName(String name) {
        String className[] = name.split("\\.");
        return className[className.length - 1];
    }


}
