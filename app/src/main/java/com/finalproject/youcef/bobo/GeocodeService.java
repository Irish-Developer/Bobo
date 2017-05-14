package com.finalproject.youcef.bobo;

/**************************************************************************************************************************
 * References:
 *
 * @uthor= Joe Mairini| Website= Linda.com | Web page= Convert lat long to address with geocoder | URL= https://www.lynda.com/Google-Play-Services-tutorials/Convert-lat-long-address-geocoder/474086/503689-4.html
 *
 * @uthor= Google| Website= Android Developers | Web page= Displaying a Location Address | URL= https://developer.android.com/training/location/display-address.html
 * @uthor= Google| Website= Android Developers | Web page= IntentService | URL= https://developer.android.com/reference/android/app/IntentService.html
 *
 *******************************************************************************************************************************/

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @uthor: Youcef O'Connor
 * Date: 25/04/2017.
 * Student No: x13114557
 */


public class GeocodeService extends IntentService {

    protected ResultReceiver mReceiver;
    protected String resultMsg = "";

    public GeocodeService() { super("GeocodeService");}

    //This is where all the geocode is handled when the intent is activated from the getAddressFromLoc method
    @Override
    protected void onHandleIntent(Intent intent) {

        //get location and receiver from the intent
        mReceiver = intent.getParcelableExtra(Constants.RECEIVER_KEY);
        final String action = intent.getAction();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        //location is Longitude and latitude
        Location location = null;

        //holds the address found by geocoder
        List<Address> addresses = null;

        try {
            if (action.equals(Constants.ACTION_ADDRESS_FROM_LOC)) {
                location = intent.getParcelableExtra(Constants.LOCATION_KEY);

                //Gets list of addresses what geocoder thinks is at that location
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            }
        } catch (IOException e) {
            resultMsg = "IOException Occurred";

            deliverResult(Constants.ERROR_RESULT, resultMsg);
        } catch (IllegalArgumentException illegalArgumentException) {
            resultMsg = "Passed the Illegal args to Geocoder";
        }

        //If there is no address to return then show error message
        if (addresses == null || addresses.isEmpty()) {
            resultMsg = "Sorry, NO Addresses found! ";
            deliverResult(Constants.ERROR_RESULT, resultMsg);
        } else {
            //get the first line of address and work from that.
            Address addr = addresses.get(0);
            String addrString = "";

            //First line of address
            for (int i = 0; i < addr.getMaxAddressLineIndex(); i++) {
                //The spaces are used to indent the Address in the message sent to contacts
                addrString += addr.getAddressLine(i) + "\n     ";
            }
            //Get addition address information such as country name
            if (addr.getCountryName() != null) addrString += addr.getCountryName() + "\n     ";

            //Passes the address back to the MainActivity
            deliverResult(Constants.SUCCESS_RESULT, addrString);
        }
    }

    //takes a resultCode and String that has the address and stores them into mReceiver
    protected void deliverResult(int resultCode, String result) {
        Bundle b = new Bundle();
        b.putString(Constants.ADDRESS_KEY, result);
        //activates the onReceiveResult method that's in the AddressReceiver class in MainActivity
        mReceiver.send(resultCode, b);
    }
}
