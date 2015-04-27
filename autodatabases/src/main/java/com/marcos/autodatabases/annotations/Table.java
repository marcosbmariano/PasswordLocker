package com.marcos.autodatabases.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by marcos on 11/13/14.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    //public static final String DEFAULT_ID_NAME = "_id";
    public String name();
    //public String id() default DEFAULT_ID_NAME;


}