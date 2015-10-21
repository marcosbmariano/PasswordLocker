package com.marcos.autodatabases.models;



import com.marcos.autodatabases.annotations.Column;
import com.marcos.autodatabases.annotations.HasMany;
import com.marcos.autodatabases.annotations.Table;

import java.util.List;

/**
 * Created by marcos on 11/13/14.
 */
@Table(name = "Costumers")
public class Costumer extends Model {

    //All the Model subclasses must have a zero argument constructor or default constructor

    @Column(name = "Name")
    private String name;

    @HasMany
    List<Address> adress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save(){
        super.save();
    }


//    public List<Address> getAdress() {
//        return adress;
//    }

//    public void setAdress(List<Address> adress) {
//        this.adress = adress;
//    }

}
