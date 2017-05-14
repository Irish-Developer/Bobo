package com.finalproject.youcef.bobo;


 /**************************************************************************************************************************
 * References:
 *
 * @uthor= Joe Mairini| Website= Linda.com | Web page= Convert lat long to address with geocoder | URL= https://www.lynda.com/Google-Play-Services-tutorials/Convert-lat-long-address-geocoder/474086/503689-4.html
 *
  * @uthor= Google| Website= Android Developers | Web page= Displaying a Location Address | URL= https://developer.android.com/training/location/display-address.html
 *
 *******************************************************************************************************************************/


/**
 * @uthor: Youcef O'Connor
 * Date: 25/04/2017.
 * Student No: x13114557
 */

public class Constants {

    //This class holds constant values for retrieving the address from location

    //SUCCESS_RESULT & ERROR_RESULT are numeric constants that indicate success or failure
    public static final int SUCCESS_RESULT = 0;
    public static final int ERROR_RESULT = 1;

    //Declaring strings to be passed between service & main activity
    public static final String PACKAGE_ID = "com.finalproject.youcef.bobo";
    public static final String RECEIVER_KEY = PACKAGE_ID + ".RECEIVER";
    public static final String ADDRESS_KEY = PACKAGE_ID + ".ADDRESS-RESULT";
    public static final String LOCATION_KEY = PACKAGE_ID + ".LOC_DATA";

    //This gets the Address
    public static final String ACTION_ADDRESS_FROM_LOC = PACKAGE_ID + ".ADDR-FROM-LOC";


}
