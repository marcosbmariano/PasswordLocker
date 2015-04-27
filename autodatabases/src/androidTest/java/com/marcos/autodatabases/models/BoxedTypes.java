package com.marcos.autodatabases.models;

import com.marcos.autodatabases.annotations.Column;
import com.marcos.autodatabases.annotations.Table;

/**
 * Created by marcos on 12/4/14.
 */
@Table(name = "BoxedTypes")
public class BoxedTypes extends Model {

    //All the Model subclasses must have a zero argument constructor or default constructor


    public void save(){
        super.save();
    }

    @Column(name=  "Integer")
    public Integer mInt;

    @Column(name = "Long")
    public Long mLong;

    @Column(name = "Byte")
    public Byte mByte;

    @Column(name = "Double")
    public Double mDouble;

    @Column(name = "Short")
    public Short mShort;

    @Column(name = "Float")
    public Float mFloat;

    @Column(name = "Boolean")
    public Boolean mBoolean;

    @Column(name = "Character")
    public Character mChar;






}
