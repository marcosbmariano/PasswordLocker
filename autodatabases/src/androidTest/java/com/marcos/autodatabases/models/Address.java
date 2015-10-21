package com.marcos.autodatabases.models;


import com.marcos.autodatabases.annotations.Column;
import com.marcos.autodatabases.annotations.Table;

/**
 * Created by marcos on 11/21/14.
 */
@Table(name = "Address")
public class Address extends Model {

    //All the Model subclasses must have a zero argument constructor or default constructor

    @Column(name = "Street")
    private String street;

    @Column(name = "Number")
    private int number;

    public void save(){
        super.save();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(@SuppressWarnings("SameParameterValue") int number) {
        this.number = number;
    }


}
