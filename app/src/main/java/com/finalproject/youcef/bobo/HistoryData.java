package com.finalproject.youcef.bobo;

import android.util.Log;

/**
 * Created by Youcef on 10/04/2017.
 */

public class HistoryData {

    public String date, driver_fname, driver_lname, license_expDate, license_number, reg_number, time;

    double longitude, latitude ;

    public HistoryData(){

    }

    public String getDriver_lname() {
        return driver_lname;
    }

    public void setDriver_lname(String driver_lname) {
        this.driver_lname = driver_lname;
    }

    public String getLicense_expDate() {
        return license_expDate;
    }

    public void setLicense_expDate(String license_expDate) {
        this.license_expDate = license_expDate;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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

    @Override
    public String toString() {
        return "HistoryData{" +
                "date='" + date + '\'' +
                ", driver_fname='" + driver_fname + '\'' +
                ", driver_lname='" + driver_lname + '\'' +
                ", license_expDate='" + license_expDate + '\'' +
                ", license_number='" + license_number + '\'' +
                ", reg_number='" + reg_number + '\'' +
                ", time='" + time + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
