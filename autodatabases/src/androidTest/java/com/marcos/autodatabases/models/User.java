package com.marcos.autodatabases.models;



import com.marcos.autodatabases.annotations.Column;
import com.marcos.autodatabases.annotations.HasMany;
import com.marcos.autodatabases.annotations.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcos on 11/13/14.
 */
@Table(name = "Users")
public class User extends Model {


    public User(){
        //All the Model subclasses must have a zero argument constructor or default constructor
    }

    @Column(name = "Number")
    private int number;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "Name", notNull = true, unique = true)
    private String name;

    @Column( name = "short")
    private short mShort;

    @Column (name = "byte")
    private byte mByte;

    @Column (name = "long")
    private long mLong;

    @Column (name = "float")
    private float mFloat;

    @Column(name = "double")
    private double mDouble;

    @Column(name = "boolean")
    private boolean mBoolean;

    @Column (name = "char")
    private char mChar;

    @HasMany
    List<Costumer> costumers;


    public List<Costumer> getCostumersChildren(){
        List<Model> list = this.getChildren(Costumer.class);
        List<Costumer> costumersList = new ArrayList<>();
        costumersList.addAll((List<Costumer>)(List<?>) list);

        return costumersList;
    }

    public void save(){
        super.save();
    }

    public String getLastName() {
        return lastName;
    }

    public short getmShort() {
        return mShort;
    }

    public void setmShort(short mShort) {
        this.mShort = mShort;
    }

    public byte getmByte() {
        return mByte;
    }

    public void setmByte(byte mByte) {
        this.mByte = mByte;
    }

    public void setmLong(long mLong) {
        this.mLong = mLong;
    }

    public float getmFloat() {
        return mFloat;
    }

    public void setmFloat(float mFloat) {
        this.mFloat = mFloat;
    }

    public double getmDouble() {
        return mDouble;
    }

    public void setmDouble(double mDouble) {
        this.mDouble = mDouble;
    }

    public boolean ismBoolean() {
        return mBoolean;
    }

    public void setmBoolean(boolean mBoolean) {
        this.mBoolean = mBoolean;
    }

    public char getmChar() {
        return mChar;
    }

    public void setmChar(char mChar) {
        this.mChar = mChar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


}
