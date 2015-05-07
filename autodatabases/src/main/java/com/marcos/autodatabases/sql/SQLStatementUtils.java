package com.marcos.autodatabases.sql;

/**
 * Created by mark on 4/24/15.
 */
class SQLStatementUtils {

    public static String setupValueForStatement(Object obj) {

        if ((obj instanceof String) || (obj instanceof Character)) {
            return insertInsideSingleQuotes(String.valueOf(obj));

        } else if( obj instanceof Boolean) {
            //if it's a boolean converts into 1 for true, and 0 for false
            return "" + (((Boolean) obj) ? 1 : 0);
        }else{
            return String.valueOf(obj);
        }

    }

    static String insertInsideSingleQuotes(String string){
        return "\'" + string + "\'";
    }

}
