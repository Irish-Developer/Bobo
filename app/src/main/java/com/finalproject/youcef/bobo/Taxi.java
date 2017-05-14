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


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Taxi {

    public String first_name, last_name, license_exp, license_no, car_reg;

    //This is needed for DataSanapshot.getValue(Taxi.class)
    public Taxi() {
    }

    //Declares taxi details for MainActivity (onChildAdded)
    public Taxi(String first_name, String last_name, String license_exp, String license_no, String car_reg) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.license_no = license_no;
        this.license_exp = license_exp;
        this.car_reg = car_reg;
    }

}
