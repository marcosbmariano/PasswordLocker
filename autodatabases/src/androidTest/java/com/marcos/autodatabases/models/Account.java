package com.marcos.autodatabases.models;

import com.marcos.autodatabases.annotations.Column;
import com.marcos.autodatabases.annotations.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 3/13/15.
 */


@Table(name = "Account")
public class Account extends Model{

    @Column( name = "name")
    String name;

    @Column (name = "lastName")
    String lastName;


    public Account(){ //All the Model subclasses must have a zero argument constructor or default constructor
    }

    public Account(String name, String lastName){
        this.name = name;
        this.lastName = lastName;
    }

    public static List<Account> getAllAccounts(){
        List<Model> models = Model.getModels(Account.class);
        List<Account> accounts = new ArrayList<>(models.size());

        for(Model model :models){
            accounts.add((Account)model);
        }
        return accounts;
    }

    @Override
    public void save() {
        super.save();
    }

    public String getName(){
        return name;
    }

}
