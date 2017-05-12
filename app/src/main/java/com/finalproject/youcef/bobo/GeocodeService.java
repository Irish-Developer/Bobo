package com.finalproject.youcef.bobo;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Youcef on 05/05/2017.
 */



public class GeocodeService extends IntentService {

    private  final static String TAG = "GEOCODER_SERVICE";
    protected ResultReceiver mReceiver;
    protected String resultMsg = "";

    public GeocodeService() { super("GeocodeService");}

    @Override
    protected void onHandleIntent(Intent intent){
        Log.d("myTag", "Intent Received");

        //get location and receiver from the intent
        mReceiver = intent.getParcelableExtra(Constants.RECEIVER_KEY);
        final String action = intent.getAction();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        Location location = null;

        List<Address> addresses = null;

        try {
            if (action.equals(Constants.ACTION_ADDRESS_FROM_LOC)) {
                location = intent.getParcelableExtra(Constants.LOCATION_KEY);
                Log.d("myTag","Location Key: " + location);
                Log.d("myTag","Receiver Key: " + mReceiver);


                addresses = geocoder.getFromLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        1);

            } else if (action.equals(Constants.ACTION_LOC_FROM_ADDR)) {
                String locName = intent.getStringExtra(Constants.PLACE_NAME_KEY);
            }
        }catch (IOException e){
            resultMsg = "IOException Occurred";
            Log.d("myTag", "Couldn't get result" + e.getLocalizedMessage());

            deliverResult(Constants.RESULT_ERROR, resultMsg);
        }
        catch (IllegalArgumentException illegalArgumentException) {
            resultMsg = "Passed the Illegal args to Geocoder";
            Log.d("myTag", "The illegal args: " + illegalArgumentException.getLocalizedMessage());
        }

        if(addresses == null || addresses.isEmpty()){
            resultMsg = "No Addresses Found for this Location";
            deliverResult(Constants.RESULT_ERROR, resultMsg);
        }else
        {
            Address addr = addresses.get(0);
            String addrString = "";

            // Gets the first line of address
            for(int i = 0; i < addr.getMaxAddressLineIndex(); i++) {
                addrString += addr.getAddressLine(i) + "\n     "; //The spaces are used to indent the Address
            }
            //Get addition address information
            if (addr.getCountryName() !=null) addrString += addr.getCountryName() + "\n     ";

            Log.d("myTag","Address Found:)");
            deliverResult(Constants.RESULT_SUCCESS, addrString);

        }
    }

    protected void deliverResult(int resultCode, String result){
        Bundle b = new Bundle();
        b.putString(Constants.ADDRESS_RESULT_KEY, result);
        mReceiver.send(resultCode, b);
    }
}
