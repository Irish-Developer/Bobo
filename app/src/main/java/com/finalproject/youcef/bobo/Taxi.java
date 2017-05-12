package com.finalproject.youcef.bobo;

/**
 * Created by Youcef on 07/05/2017.
 * This was created after testing to solve the error with using a Map to store the data
 */

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Taxi {

    public String first_name, last_name, license_exp, license_no, car_reg;

    //This is needed for DataSanapshot.getValue(Taxi.class)
    public Taxi(){
    }

    public Taxi(String first_name, String last_name, String license_exp, String license_no, String car_reg ){
        this.first_name = first_name;
        this.last_name = last_name;
        this.license_no = license_no;
        this.license_exp = license_exp;
        this.car_reg = car_reg;
    }

}
