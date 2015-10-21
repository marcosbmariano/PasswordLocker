package com.marcos.autodatabases.sql;

import android.content.ContentValues;
import com.example.mark.myutils.ClassSelector;
import com.marcos.autodatabases.annotations.Column;
import com.marcos.autodatabases.models.Model;

import java.lang.reflect.Field;

/**
 * Created by mark on 5/7/15.
 */
public class SetContentValue extends ClassSelector<Void> {
    private final ContentValues mContentValues;
    private final Field mField;
    private final Column mColumn;
    private final Model mModel;
    private static SetContentValue mSetContenValues; //TODO why static?

    private SetContentValue(ContentValues cv, Field field, Column col, Model model){
        mContentValues = cv;
        mField = field;
        mColumn = col;
        mModel = model;
    }


    public static void setValueContentValues(ContentValues cv, Field field, Column col, Model model){
        mSetContenValues = new SetContentValue(cv, field, col, model);
        mSetContenValues.mField.setAccessible(true);
        mSetContenValues.selectMethod(getClassName(field, model));
        mSetContenValues.mField.setAccessible(false);
    }


    private static String getClassName(Field field, Model model){
        try {
            if ( null == field.get(model)){
                return "null";
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return field.getType().getName();
    }

    @Override
    protected Void getByte() {
        try {
            mContentValues.put(mColumn.name(), mField.getInt(mModel));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getBoxedByte() {
        try {
            mContentValues.put(mColumn.name(), Byte.parseByte(mField.get(mModel).toString()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getInt() {
        try {
            mContentValues.put(mColumn.name(), mField.getInt(mModel));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getBoxedInteger() {
        try {
            mContentValues.put(mColumn.name(), Integer.parseInt(mField.get(mModel).toString()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getShort() {
        try {
            mContentValues.put(mColumn.name(), mField.getInt(mModel));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getBoxedShort() {
        try {
            mContentValues.put(mColumn.name(), Short.parseShort(mField.get(mModel).toString()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getLong() {
        try {
            mContentValues.put(mColumn.name(), mField.getLong(mModel));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getBoxedLong() {
        try {
            mContentValues.put(mColumn.name(), Long.parseLong(mField.get(mModel).toString()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getFloat() {
        try {
            mContentValues.put(mColumn.name(), mField.getFloat(mModel));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getBoxedFloat() {
        try {
            mContentValues.put(mColumn.name(), Float.parseFloat(mField.get(mModel).toString()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getDouble() {
        try {
            mContentValues.put(mColumn.name(), mField.getDouble(mModel));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getBoxedDouble() {
        try {
            mContentValues.put(mColumn.name(), Float.parseFloat(mField.get(mModel).toString()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getBoolean() {
        try {
            mContentValues.put(mColumn.name(), mField.getBoolean(mModel) ? 1 : 0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getBoxedBoolean() {
        try {
            mContentValues.put(mColumn.name(), Boolean.parseBoolean(mField.get(mModel).toString()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getChar() {
        try {
            mContentValues.put(mColumn.name(), mField.get(mModel).toString().substring(0, 1));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getBoxedChar() {
        getChar();
        return null;
    }

    @Override
    protected Void getString() {
        try {
            mContentValues.put(mColumn.name(), String.valueOf(mField.get(mModel)));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Void getDefault() {
        mContentValues.putNull(mColumn.name());
        return null;
    }


}
