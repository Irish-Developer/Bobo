package com.finalproject.youcef.bobo;

import android.app.IntentService;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

/**
 * Created by Youcef on 25/04/2017.
 */

public class Constants {

    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_ERROR = 1;

    public static final String PACKAGE_NAME =
            "com.finalproject.youcef.bobo";
    public static final String ADDRESS_RESULT_KEY = PACKAGE_NAME + ".ADDRESS-RESULT";

    public static final String RECEIVER_KEY = PACKAGE_NAME + ".RECEIVER";
    public static final String LOCATION_KEY = PACKAGE_NAME + ".LOC_DATA";
    public static final String PLACE_NAME_KEY = PACKAGE_NAME + ".PLACE-NAME";

    public static final String ACTION_ADDRESS_FROM_LOC = PACKAGE_NAME + ".ADDR-FROM-LOC";
    public static final String ACTION_LOC_FROM_ADDR = PACKAGE_NAME + ".LOC-FROM-ADDR";

//    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
//            ".RESULT_DATA_KEY";
//    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
//            ".LOCATION_DATA_EXTRA";
}
