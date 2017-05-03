package com.finalproject.youcef.bobo;

import android.util.Log;

/**
 * Created by Youcef on 10/04/2017.
 */

public class HistoryData {

    private String fname, lname, expDate, lNumber, regNumber;

    public HistoryData(){

    }

    public HistoryData(String driver_fname, String driver_lname, String license_expDate, String license_number, String reg_number){
        this.fname = driver_fname;
        this.lname = driver_lname;
        expDate = license_expDate;
        lNumber = license_number;
        regNumber = reg_number;
    }

    public String getFname(){
        Log.d("myTag", "EventListener: " + fname);
        return fname;
    }
    public void setFname(String driver_fname){
        this.fname = driver_fname;
    }
    //Get and Set
    public String getLname(){
        return lname;
    }
    public void setLname(String driver_lname){
        lname = driver_lname;
    }
    //Get and Set
    public String getExpDate(){
        return expDate;
    }
    public void setExpDate(String license_expDate){
        expDate = license_expDate;
    }
    //Get and Set license number
    public String getlNumber(){
        return lNumber;
    }
    public void setlNumber(String license_number){
        lNumber = license_number;
    }
    //Get and Set license number
    public String getRegNumber(){
        return regNumber;
    }
    public void setRegNumber(String reg_number){
        regNumber = reg_number;
    }
}
