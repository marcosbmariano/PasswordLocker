package com.marcos.autodatabases.modelUtils;


import android.database.Cursor;
import com.example.mark.myutils.ClassSelector;
import com.marcos.autodatabases.annotations.Column;
import java.lang.reflect.Field;


/**
 * Created by mark on 4/30/15.
 */
public class ValueFromCursor extends ClassSelector<Object> {
    private final Cursor mCursor;
    private final Column mColumn;

    private ValueFromCursor(Cursor cv,  Column col){
        mCursor = cv;
        mColumn = col;
    }

    public static Object getValueFromCursor(Cursor cv, Field field, Column col){
        ValueFromCursor result = new ValueFromCursor(cv,col);
        return result.selectMethod(field.getType().getName());
    }

    @Override
    protected Object getByte() {
        return getBoxedByte();
    }

    @Override
    protected Object getBoxedByte() {
        return (byte)mCursor.getInt(mCursor.getColumnIndex(mColumn.name()));
    }

    @Override
    protected Object getInt() {
        return getBoxedInteger();
    }

    @Override
    protected Object getBoxedInteger() {
        return mCursor.getInt(mCursor.getColumnIndex(mColumn.name()));
    }

    @Override
    protected Object getShort() {
        return getBoxedShort();
    }

    @Override
    protected Object getBoxedShort() {
        return mCursor.getShort(mCursor.getColumnIndex(mColumn.name()));
    }

    @Override
    protected Object getLong() {
        return getBoxedLong();
    }

    @Override
    protected Object getBoxedLong() {
        return mCursor.getLong(mCursor.getColumnIndex(mColumn.name()));
    }

    @Override
    protected Object getFloat() {
        return getBoxedFloat();
    }

    @Override
    protected Object getBoxedFloat() {
        return mCursor.getFloat(mCursor.getColumnIndex(mColumn.name()));
    }

    @Override
    protected Object getDouble() {
        return getBoxedDouble();
    }

    @Override
    protected Object getBoxedDouble() {
        return mCursor.getDouble(mCursor.getColumnIndex(mColumn.name()));
    }

    @Override
    protected Object getBoolean() {
        return getBoxedBoolean();
    }

    @Override
    protected Object getBoxedBoolean() {
        return mCursor.getInt(mCursor.getColumnIndex(mColumn.name())) != 0;
    }

    @Override
    protected Object getChar() {
        return getBoxedChar();
    }

    @Override
    protected Object getBoxedChar() {
        return mCursor.getString(mCursor.getColumnIndex(mColumn.name())).charAt(0);
    }

    @Override
    protected Object getString() {
        return mCursor.getString(mCursor.getColumnIndex(mColumn.name()));
    }

    @Override
    protected Object getDefault() {
        return "NULL";

    }

}
