package com.marcos.autodatabases.utils;


import com.marcos.autodatabases.models.Model;

import java.lang.reflect.Field;

/**
 * Created by marcos on 11/13/14.
 */
// Noninstantiable utility class
 class ReflectionUtils {

    private ReflectionUtils(){
        throw new AssertionError();
    }

    public static Class<?> getClassFromString(String className) {
        Class<?> aClass = null;
        try {
            aClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return aClass;
    }

    public static Class<? extends Model> getModelClassFromString(String className) {
        Class<? extends Model> aClass = null;
        try {
            aClass = (Class<? extends Model>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return aClass;
    }

    public static String getClassNameFromArrayList(Field field) {
        final String ARRAY_LIST = "java.util.ArrayList";
        final String LIST = "java.util.List";
        String type = field.getType().getName();
        String className = "";

        if ((type.equals(ARRAY_LIST)) || (type.equals(LIST))) {
            className = field.getGenericType().toString();
            className = className.substring(type.length() + 1, className.length() - 1);
        } else {
            return ""; //TODO fix this , should create a table white a representation of the Object
            // throw new TypeMismatch("Fields under @HasMany must be contained by a ArrayList");
        }
        return className;

    }

    public static boolean isModelSubclass(Class<?> aClass) {
        Class<?> superClass = aClass.getSuperclass();
        return Model.class.equals(superClass);
    }

    public static String getSimpleClassName(String name) {
        String className[] = name.split("\\.");
        return className[className.length - 1];
    }


}
