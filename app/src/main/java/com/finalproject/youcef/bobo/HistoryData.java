package com.finalproject.youcef.bobo;


/**************************************************************************************************************************
 * References:
 *
 * @uthor= Google | Website= Udacity | Web page= Firebase in a Weekend: Android | URL= https://www.udacity.com/course/firebase-in-a-weekend-by-google-android--ud0352
 *
 *
 *******************************************************************************************************************************/

/**
 * @uthor: Youcef O'Connor
 * Date: 10/04/2017.
 * Student No: x13114557
 */

public class HistoryData {

    public String date, driver_fname, driver_lname, license_number, reg_number, time;


    //This is needed for DataSanapshot.getValue(HistorydActivity.class)
    public HistoryData() {

    }

    //Setters and Getters
    public String getDriver_lname() {
        return driver_lname;
    }

    public void setDriver_lname(String driver_lname) {
        this.driver_lname = driver_lname;
    }


    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public String getReg_number() {
        return reg_number;
    }

    public void setReg_number(String reg_number) {
        this.reg_number = reg_number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDriver_fname() {
        return driver_fname;
    }

    public void setDriver_fname(String driver_fname) {
        this.driver_fname = driver_fname;
    }

}
