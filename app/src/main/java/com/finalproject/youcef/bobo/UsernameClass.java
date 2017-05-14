package com.finalproject.youcef.bobo;

/**************************************************************************************************************************
 * References:
 *
 * @uthor = Firebase | Website= Firebase | Web page= Read and Write Data on Android | URL= https://firebase.google.com/docs/database/android/read-and-write
 *
 *
 *******************************************************************************************************************************/

/**
 * Name: Youcef O'Connor
 * Number: x13114557
 * Date: 07 May 2017
 * This was created after testing to solve the error with using a Map (storage Map not Google Map) to store the data
 */

public class UsernameClass {
    public String fname, lname;

    //This is needed for DataSanapshot.getValue(User.class)
    public UsernameClass() {
    }

    //Declares user data for onDataChanged method in the MainActivity
    public UsernameClass(String fname, String lname) {
        this.fname = fname;
        this.lname = lname;
    }
}
