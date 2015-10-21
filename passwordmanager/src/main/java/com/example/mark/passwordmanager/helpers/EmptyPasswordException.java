package com.example.mark.passwordmanager.helpers;

/**
 * Created by mark on 8/11/15.
 */
public class EmptyPasswordException extends Exception {
    public EmptyPasswordException(){
        super("Password cannot be null or empty!");
    }

}
