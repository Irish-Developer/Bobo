package com.finalproject.youcef.bobo;

/**
 * Created by Youcef on 07/05/2017.
 */

public class UsernameClass {
    public String fname, lname;

    //This is needed for DataSanapshot.getValue(User.class)
    public UsernameClass(){
    }

    public UsernameClass (String fname, String lname){
        this.fname = fname;
        this.lname = lname;
    }
}
