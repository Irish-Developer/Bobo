package com.finalproject.youcef.bobo;

/**************************************************************************************************************************
 * References:
 *
 * @uthor= Joe Mairini| Website= Linda.com | Web page= Convert lat long to address with geocoder | URL= https://www.lynda.com/Google-Play-Services-tutorials/Convert-lat-long-address-geocoder/474086/503689-4.html
 *
 * @uthor = Google| Website= Android Developers | Web page= ResultReceiver | URL= https://developer.android.com/reference/android/os/ResultReceiver.html
 * @uthor = Firebase | Website = Firebase | Web page = Read and Write Data on the Web | URL = https://firebase.google.com/docs/database/web/read-and-write
 * @uthor = Firebase | Website = Firebase | Web page = Work with Lists of Data on Android | URL = https://firebase.google.com/docs/database/android/lists-of-data
 * @uthor = Google | Website = Android Developers | Web page = Requesting Permissions at Run Time | URL = https://developer.android.com/training/permissions/requesting.html
 * @uthor = WhatsApp Inc | Website = WhatsApp | Web page = I'm an Android developer, how can I integrate WhatsApp with my app? | URL = https://www.whatsapp.com/faq/en/android/28000012
 * @uthor = Anoop M | Website = StackOverflow | Web page = How can i get current date and time in android and store it into string | URL = http://stackoverflow.com/questions/31286213/how-can-i-get-current-date-and-time-in-android-and-store-it-into-string
 *
 *******************************************************************************************************************************/


import android.content.pm.PackageManager;
import android.location.Location;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.Manifest;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * Name: Youcef O'Connor
 * Number: x13114557
 * Date: 16 Jan 2017
 * Class: MainActivity
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ValueEventListener, ChildEventListener {

    //Declaring variables
    private Button mCheckButton, noButton, yesButton, usingNotReg, noButton2;
    private EditText taxireg;
    private ProgressBar progressBar;

    //Declaring TextView & Taxi driver
    private TextView taxiFname, taxiLname, licenseNum, taxiLexp, taxiRegNum, regOk, regNotOk, areYou;

    private String historyId;
    private String firstName;
    private String lastName;
    private String lnumber;
    private String lexpirary;
    private String regNum;
    private String dateNow;
    private String timeNow;
    private String mAddress;
    private String registered;
    private String taxiNumber;
    private String userFname, userLname;
    private GoogleApiClient googleApiClient;
    private Boolean gotLocPermission;
    private Location mLastLocation;

    Timer t_ime = new Timer();

    //Obtains address from the GeocodeService class
    protected AddressReceiver mAddressReceiver;


    //////////using classes from the FirebaseDatabase API //////////
    //////////These are Firebase instance variables //////////

    //Entry point for the app to access the database
    private FirebaseDatabase mFirebaseDatabase;
    //DatabaseReference object is a class that references a specific part of the database
    private DatabaseReference mTaxiDatabaseReference, userRef, userNameRef;
    //Authentication Instance variables
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase instances
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        //Get user ID
        String uid = auth.getCurrentUser().getUid();

        //Get reference to "name" in "users" node
        userNameRef = mFirebaseDatabase.getReference().child("users").child(uid).child("name");
        //Get reference to user ID in "users" node
        userRef = FirebaseDatabase.getInstance().getReference("users").child(uid);

        //Create historyID
        historyId = userRef.push().getKey();

        // Create an instance of GoogleAPIClient.
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        //Get the current date
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateNow = dateFormat.format(date);

        //Get the current time
        Date time = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        timeNow = timeFormat.format(time);

        //Retrieving values from edit text & button
        taxireg = (EditText) findViewById(R.id.checkTF);
        taxiFname = (TextView) findViewById(R.id.driverFname);
        taxiLname = (TextView) findViewById(R.id.textView3);
        licenseNum = (TextView) findViewById(R.id.textView4);
        taxiLexp = (TextView) findViewById(R.id.textView6);
        taxiRegNum = (TextView) findViewById(R.id.textView7);
        mCheckButton = (Button) findViewById(R.id.button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        noButton = (Button) findViewById(R.id.noBtn);
        noButton2 = (Button) findViewById(R.id.noBtn2);
        yesButton = (Button) findViewById(R.id.yesBtn);
        usingNotReg = (Button) findViewById(R.id.yesBtn2);
        regOk = (TextView) findViewById(R.id.isRegTF);
        regNotOk = (TextView) findViewById(R.id.notRegTf);
        areYou = (TextView) findViewById(R.id.questionTF);
        mLastLocation = null;
        gotLocPermission = false;
        mAddress = "";

        mAddressReceiver = new AddressReceiver(new android.os.Handler());


        //////////////////////////////////////////////////////////////////   Check button   ////////////////////////////////////////////////////////////

        mCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Activates the gtAddressFromLoc method when the Continue button is pressed
                getAddressFromLoc();

                //Check to make sure the EditText is not empty
                String userInput = taxireg.getText().toString().trim();
                if (TextUtils.isEmpty(userInput)) {
                    Toast.makeText(getApplicationContext(), "Please input taxi Registration or License number.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                //Get reference to "taxi_details" in the "taxi_data" node
                mTaxiDatabaseReference = mFirebaseDatabase.getReference().child("taxi_data").child("taxi_details");

                //Get user input
                String taxi_detail = taxireg.getText().toString().trim();

                //Check "car_reg" and "license_no" for users input
                Query a = mTaxiDatabaseReference.orderByChild("car_reg").equalTo(taxi_detail);
                Query b = mTaxiDatabaseReference.orderByChild("license_no").equalTo(taxi_detail); //new 24/02

                // a - is the query for checking car registration
                // b - is the query for checking taxi license number
                // They are called a and b to prevent them from getting mixed up as there a lot
                // of variables with the names reg and license being used in this Activity.
                a.addChildEventListener(MainActivity.this);
                b.addChildEventListener(MainActivity.this); //adds license number query to

                //The queries have 6 seconds
                t_ime = new Timer();
                t_ime.schedule(new Task(b), 6000, 1);

                progressBar.setVisibility(View.VISIBLE);
            }
        });/////////////////////////////////////////////////////////////////////// End of Check Button  ///////////////////////////////////////////////////////////


        // this listener will be called when there is change in firebase user session - https://firebase.google.com/docs/auth/android/password-auth
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {                                                             //User is signed out
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));          //Go to Login screen
                    finish();
                }
            }
        };

        //Gets the username
        userNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Uses the UsernameClass.java to sort Snapshot values
                UsernameClass user = dataSnapshot.getValue(UsernameClass.class);
                userFname = user.fname;
                userLname = user.lname;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Auto generated. Activated if there is authentication errors
            }
        });
    }

    //This class is activated once the timer runs out (taxi number was not found).
    //Only need to do this for one of the queries as bought didn't find anything,
    //which produce the same results (NOT REGISTERED)
    class Task extends TimerTask {
        private final Query a;

        public Task(Query a) {
            this.a = a;
        }


        public void run() {
            //Timer stops after 6 seconds and removes event listener otherwise the query will keep running
            a.removeEventListener((ChildEventListener) MainActivity.this);
            t_ime.cancel();
            //Rerun the event listener
            MainActivity.this.runOnUiThread(new Runnable() {

                //Displaying NOT REGISTERED details and option buttons
                @Override
                public void run() {
                    //Stop progressbar
                    progressBar.setVisibility(View.GONE);
                    //Let user know by using toast
                    Toast.makeText(MainActivity.this, "Not Registered", Toast.LENGTH_SHORT).show();

                    //Take the number entered by the user
                    taxiNumber = taxireg.getText().toString().trim();

                    //Set entered number as Taxi Number
                    taxiRegNum.setText("Taxi Number: " + taxiNumber);

                    //Insert null values into the empty fields
                    taxiFname.setText("Name : " + "NOT REGISTERED");
                    taxiLname.setText("");
                    licenseNum.setText("");
                    taxiLexp.setText("");


                    //Display NOT REGISTERED message
                    registered = "NOT REGISTERED";


                    //Displays buttons and "Registered" message
                    noButton2.setVisibility(View.VISIBLE);
                    usingNotReg.setVisibility(View.VISIBLE);
                    regNotOk.setVisibility(View.VISIBLE);
                    areYou.setVisibility(View.VISIBLE);

                    noButton.setVisibility(View.GONE);
                    regOk.setVisibility(View.GONE);
                }
            });
        }
    }


    //These methods were auto generated from a & b addChildEventListeners
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        //Uses the Taxi.java to sort Snapshot values
        Taxi taxi = dataSnapshot.getValue(Taxi.class);

        //Presents driver details
        taxiFname.setText("First Name: " + taxi.first_name);
        taxiLname.setText("Last Name: " + taxi.last_name);
        licenseNum.setText("License Number: " + taxi.license_no);
        taxiLexp.setText("License Expiry Date : " + taxi.license_exp);
        taxiRegNum.setText("Car Reg Number : " + taxi.car_reg);

        //Temporary store details so they be stored in history and sent to contact through WhatsApp
        firstName = taxi.first_name;
        lastName = taxi.last_name;
        lnumber = taxi.license_no;
        lexpirary = taxi.license_exp;
        regNum = taxi.car_reg;

        //Stops Progress bar
        progressBar.setVisibility(View.GONE);


        //Displays buttons and "Registered" message
        noButton.setVisibility(View.VISIBLE);
        yesButton.setVisibility(View.VISIBLE);
        regOk.setVisibility(View.VISIBLE);
        areYou.setVisibility(View.VISIBLE);

        //Don't display the not registered message
        regNotOk.setVisibility(View.GONE);


        //Stops the timer from activating the "Not Registered" message to the user if data is found
        if (dataSnapshot.hasChild("first_name")) {
            t_ime.cancel();
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
    }

    //////////////////////////////////////////////////////////////////////////////////////When the NO button is pressed///////////////////////////////////////////////////////////////////////////////

    //This method is called for noBtn and noBtn2 as they are bought using this for their onClickListener
    public void noBtn(View v) {
        //Refresh screen
        recreate();
        //Clear EditText field
        taxireg.setText("");
    }

    //////////////////////////////////////////////////////////////////////////////////////When the YES button is pressed///////////////////////////////////////////////////////////////////////////////
    public void yesBtn(View v) {

        //Storing taxi driver details to History table / node
        userRef.child("history").child(historyId).child("driver_lname").setValue(lastName);
        userRef.child("history").child(historyId).child("license_number").setValue(lnumber);
        userRef.child("history").child(historyId).child("license_expDate").setValue(lexpirary);
        userRef.child("history").child(historyId).child("reg_number").setValue(regNum);

        userRef.child("history").child(historyId).child("date").setValue(dateNow);
        userRef.child("history").child(historyId).child("time").setValue(timeNow);

        //Confirmation from Firebase Realtime database of upload success
        DatabaseReference dataRef = userRef.child("history").child(historyId).child("driver_fname");
        dataRef.setValue(firstName, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast.makeText(MainActivity.this, "Data update failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Stored to history", Toast.LENGTH_SHORT).show();
                }
                recreate();
            }

        });


        //This intent lets the user send their message to there WhatsApp contacts
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        //This is where the message sent is built
        sendIntent.putExtra(Intent.EXTRA_TEXT, userFname + " " + userLname + " is using a taxi.\n\n"
                + "Driver details: \n Name: " + firstName + " " + lastName + "\n Reg No: " + regNum +
                "\n Licence No: " + lnumber + "\n\nPick up location:\n     " + mAddress + "\n\nThis message was sent using the Bobo App");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }//End of yesBtn (Tax is REGISTERED)


    //////////////////////////////////////////////////////////////////////////////////////When the YES button (for NOT REGISTERED) is pressed///////////////////////////////////////////////////////////////////////////////
    public void yesBtn2(View v) {

        //Storing taxi driver details to History table / node

        userRef.child("history").child(historyId).child("driver_lname").setValue("");
        userRef.child("history").child(historyId).child("license_number").setValue("");
        userRef.child("history").child(historyId).child("license_expDate").setValue("");
        userRef.child("history").child(historyId).child("reg_number").setValue(taxiNumber);

        userRef.child("history").child(historyId).child("date").setValue(dateNow);
        userRef.child("history").child(historyId).child("time").setValue(timeNow);


        //Confirmation from Firebase Realtime database of upload success
        DatabaseReference dataRef = userRef.child("history").child(historyId).child("driver_fname");
        dataRef.setValue("NOT REGISTERED", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.d("MyTag", "databaseError");
                    Toast.makeText(MainActivity.this, "Data update failed", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(MainActivity.this, "Stored to history", Toast.LENGTH_SHORT).show();
                    Log.d("MyTag", "database works!");
                }
                recreate();
            }


        });


        //This intent lets the user send their message to there WhatsApp contacts
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, userFname + " " + userLname + " is using a taxi.\n\n"
                + "Driver details: \n\n      NOT REGISTERED " + "\n\n  Reg Plate No: " + taxiNumber +
                "\n\nPick up location:\n     " + mAddress + "\n\nThis message was sent using the Bobo App");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }//End of yesBtn2


    //The method is activated when the check button is pressed
    protected void getAddressFromLoc() {
        if (googleApiClient.isConnected() && gotLocPermission) {
            try {
                //Gets last location (longitude & latitude) from Google Play Services API
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            } catch (SecurityException e) {

            }
            if (mLastLocation != null) {
                //Intent that will get the address of users location
                //and send it to GeocodeService
                Intent intent = new Intent(this, GeocodeService.class);
                //Tell Constants to get address from the location
                intent.setAction(Constants.ACTION_ADDRESS_FROM_LOC);
                //Then store mAddressReceiver object and mLastLocation into the intent
                intent.putExtra(Constants.RECEIVER_KEY, mAddressReceiver);
                intent.putExtra(Constants.LOCATION_KEY, mLastLocation);

                //startService is called to start getting the Address
                startService(intent);
            }
        }
    }


    //Requests permission to use devices location
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //Checks to make sure the Bobo has permission
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                gotLocPermission = true;
            }
        }
    }

    //Automatically generated methods
    @Override
    public void onConnected(Bundle connectionHint) {
        //Check to see if Bobo has permission to access location
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //If not then ask for permission
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //Else set gotlocPermission boolean to true
            gotLocPermission = true;
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    //Get main menu layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bobo_menu, menu);
        return true;
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    //On Start
    @Override
    public void onStart() {
        //Connect to Google API
        googleApiClient.connect();

        //Start MainActivity
        super.onStart();
        auth.addAuthStateListener(mAuthStateListener);
    }

    //On Stop
    @Override
    public void onStop() {
        //Disconnect from Google API
        googleApiClient.disconnect();
        super.onStop();

        //This listens to see if the user has signed out
        if (mAuthStateListener != null) {
            auth.removeAuthStateListener(mAuthStateListener);
        }
    }

    //Main menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_option:
                //Sign out
                signOut();
                return true;
            case R.id.history:
                //Go to History page
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //extends the ResultsReceiver class
    class AddressReceiver extends ResultReceiver {
        public AddressReceiver(android.os.Handler handler) {
            super(handler);
        }


        //Generic interface for receiving a callback result from ADDRESS_KEY
        //Bundle contains the address from the GeocodeService
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            //get address or display error message (from intent)
            mAddress = resultData.getString(Constants.ADDRESS_KEY);
            Log.d("myTag", "Location:\n     " +mAddress);
        }

    }

}
