package com.finalproject.youcef.bobo;


import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ValueEventListener, ChildEventListener {

    //Declaring variables
    private Button mCheckButton, noButton, yesButton, usingNotReg, noButton2;
    private EditText taxireg;
    private ProgressBar progressBar;
    //Declaring TextView & Taxi driver
    private TextView taxiFname, taxiLname, licenseNum, taxiLexp, taxiRegNum, regOk, regNotOk, areYou;
    //Declaring History data
    private String historyId, Uid, firstName, lastName, lnumber, lexpirary, regNum, dateNow, timeNow, mAddress, mPlaceName, registered;
    private String taxiNumber;
    //Time and date
    private Date date, time;
    //User
    private String userFname, userLname;
    private GoogleApiClient googleApiClient;
    private Boolean haveLocPerm;
    private Location mLastLocation;
    Timer t = new Timer();

    protected AddressReceiver mAddressReceiver;

    //The method is activateed
    protected void getAddressFromLoc(){
        if(googleApiClient.isConnected() && haveLocPerm) {
          try{
              mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
          }
          catch (SecurityException e){
              Log.d("myTag", "No Location Access");
          }
          if(mLastLocation != null){
              //Intent that will get the address of users location
              Intent intent = new Intent(this, GeocodeService.class);
              intent.setAction(Constants.ACTION_ADDRESS_FROM_LOC);
              intent.putExtra(Constants.RECEIVER_KEY,mAddressReceiver);
              intent.putExtra(Constants.LOCATION_KEY, mLastLocation);

              startService(intent);
          }
        }
    }

    protected void getAddressFromName(String name){
        if (name !=null && !name.isEmpty()){
            // gets the location name
            Intent intent = new Intent(this, GeocodeService.class);

            intent.setAction(Constants.ACTION_LOC_FROM_ADDR);
            intent.putExtra(Constants.RECEIVER_KEY, mAddressReceiver);
            intent.putExtra(Constants.PLACE_NAME_KEY, name);

            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //Checks to make sure the Bobo has permission
        if (requestCode == 1) {
            if (grantResults.length>0 && grantResults [0] == PackageManager.PERMISSION_GRANTED) {

                haveLocPerm = true;
            }
        }
    }

    //Automatically generated methods for Google Play Servicess
    @Override
    public void onConnected(Bundle connectionHint) {
        //Check to see if Bobo has permission to access location
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //If not then ask for permission
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //Else set have location boolean to true
            haveLocPerm = true;
        }

    }

    @Override
    public void onConnectionSuspended(int cause) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {
        Log.d("myTag","Connection Failed: " +connectionResult.getErrorCode());

    }


    //////////using classes from the FirebaseDatabase API //////////
    //////////These are Firebase instance variables //////////

    //Entry point for the app to access the database
    private FirebaseDatabase mFirebaseDatabase;
    //DatabaseReference object is a class that references a specific part of the database
    private DatabaseReference mTaxiDatabaseReference, userRef, userNameRef;
    //Authentication Instance variables
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //read from the taxi node on the database
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase instances
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        Uid = auth.getCurrentUser().getUid();


        Log.d("myTag", "FireAuth user ID" + Uid);
        userNameRef = mFirebaseDatabase.getReference().child("users").child(Uid).child("name");
        userRef = FirebaseDatabase.getInstance().getReference("users").child(Uid);
        historyId = userRef.push().getKey();
//        username = userRef.child("name").child("fname").toString();
//        Log.d("myTag", "username: " + username);


        // Create an instance of GoogleAPIClient.
        googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
                    Log.d("myTag", "Google API Connection: " + googleApiClient);


        //Get the current date
        date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateNow = dateFormat.format(date);
        Log.d("myTag", "Current date: " + dateNow);

        //Get the current date
        time = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        timeNow = timeFormat.format(time);
        Log.d("myTag", "Current time: " + timeNow);

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
        haveLocPerm = false;
        mAddress = "";

        mAddressReceiver = new AddressReceiver(new android.os.Handler());

//        Retrieving user details



        // TODO Calculate users age




        //Check button
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

                mTaxiDatabaseReference = mFirebaseDatabase.getReference().child("taxi_data").child("taxi_details");

                String taxi_detail = taxireg.getText().toString().trim();
                Query a = mTaxiDatabaseReference.orderByChild("car_reg").equalTo(taxi_detail);
                Log.d("myTag", "car reg " + mTaxiDatabaseReference);
                Query b = mTaxiDatabaseReference.orderByChild("license_no").equalTo(taxi_detail); //new 24/02

                a.addChildEventListener(MainActivity.this);
                b.addChildEventListener(MainActivity.this); //adds license number query to
                t = new Timer();
                t.schedule(new Task(b), 6000, 1);

                progressBar.setVisibility(View.VISIBLE);
            }
        });

        // this listener will be called when there is change in firebase user session - https://firebase.google.com/docs/auth/android/password-auth
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {                                                             //User is signed out
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));          //Go to Login screen
                    Log.d("MyTag", "Signing out user");
                    finish();
                }
            }
        };

        //Gets the username
        userNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> post = (Map<String, Object>) dataSnapshot.getValue();
                userFname = post.get("fname").toString();
                userLname = post.get("lname").toString();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Auto generated. Activated if there is authentication errors
            }
        });
    }


    class Task extends TimerTask {
        private final Query a;

        public Task(Query a) {
            this.a = a;
        }


        public void run() {
            //Timer stops after 5 seconds and removes event listener otherwise the query will keep running
            a.removeEventListener((ChildEventListener) MainActivity.this);
            t.cancel();
            //Rerun the event listener
            MainActivity.this.runOnUiThread(new Runnable() {

                //Displaying NOT REGISTERED details and option buttons
                @Override
                public void run() {
                    //Stop progressbar
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Not Registered", Toast.LENGTH_SHORT).show();

                    taxiNumber = taxireg.getText().toString().trim();

                    taxiRegNum.setText("Taxi Number: " + taxiNumber.toString());

                    //Insert null values into the empty fields
                    taxiFname.setText("Name : " + "NOT REGISTERED".toString());
                    taxiLname.setText("");
                    licenseNum.setText("");
                    taxiLexp.setText("");


                    registered = "NOT REGISTERED";
                    Log.d("myTag", "Is " + registered);


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

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
//        Toast.makeText(getApplicationContext(), dataSnapshot.toString(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.d("myTag", "Map " + dataSnapshot);
        Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
        Log.d("myTag", "Map " + dataSnapshot);

//        Stops the timer from activating the "Not Registered" message to the user
        if (dataSnapshot.hasChild("first_name")) {
            t.cancel();
        }
        //Presents driver details
        taxiFname.setText("First Name: " + newPost.get("first_name").toString());
        taxiLname.setText("Last Name: " + newPost.get("last_name").toString());
        licenseNum.setText("License Number: " + newPost.get("license_no").toString());
        taxiLexp.setText("License Expiry Date : " + newPost.get("license_exp").toString());
        taxiRegNum.setText("Car Reg Number : " + newPost.get("car_reg").toString());

        //Temporary store details so they be stored in history and sent to contact through WhatsApp
        firstName = newPost.get("first_name").toString();
        lastName = newPost.get("last_name").toString();
        lnumber = newPost.get("license_no").toString();
        lexpirary = newPost.get("license_exp").toString();
        regNum = newPost.get("car_reg").toString();
        Log.d("myTag", "stored first name: " + firstName);
        Log.d("myTag", "first name: " + newPost.get("first_name"));
        //Stops Progress bar
        progressBar.setVisibility(View.GONE);

        registered = "REGISTERED".toString();
        Log.d("myTag", "Is: " + registered);


        //Displays buttons and "Registered" message
        noButton.setVisibility(View.VISIBLE);
        yesButton.setVisibility(View.VISIBLE);
        regOk.setVisibility(View.VISIBLE);
        areYou.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.d("myTag", "onClickChanged " + dataSnapshot);
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

    //This method is called for noBtn and noBtn as they are bought using this for their onClickListener
    public void noBtn(View v) {
        recreate();
        //Clear EditText field
        taxireg.setText("");
    }

    //////////////////////////////////////////////////////////////////////////////////////When the YES button is pressed///////////////////////////////////////////////////////////////////////////////
    public void yesBtn(View v) {

            //Storing taxi driver details to History table / node
            Log.d("myTag", "Send to History " + firstName);
            Log.d("myTag", "first name: ");
            userRef.child("history").child(historyId).child("driver_lname").setValue(lastName);
            userRef.child("history").child(historyId).child("license_number").setValue(lnumber);
            userRef.child("history").child(historyId).child("license_expDate").setValue(lexpirary);
            userRef.child("history").child(historyId).child("reg_number").setValue(regNum);

            userRef.child("history").child(historyId).child("date").setValue(dateNow);
            userRef.child("history").child(historyId).child("time").setValue(timeNow);
            Log.d("myTag", "Sent to History");

            //Confirmation from Firebase Realtime database of upload success
            DatabaseReference dataRef = userRef.child("history").child(historyId).child("driver_fname");
            dataRef.setValue(firstName, new DatabaseReference.CompletionListener() {
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


        //This intent lets the user send their message to there contacts
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, userFname +" " + userLname + " is using a taxi.\n\n" +"Driver details: \n Name: " +firstName + " "+lastName +"\n Reg No: " +regNum+
                "\n Licence No: " + lnumber+ "\n\nPick up location:\n     " +mAddress);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }//End of yesBtn (Tax is REGISTERED


    //////////////////////////////////////////////////////////////////////////////////////When the YES button (for NOT REGISTERED) is pressed///////////////////////////////////////////////////////////////////////////////
    public void yesBtn2(View v) {

        //Storing taxi driver details to History table / node
        Log.d("myTag", "Send to History " );
        Log.d("myTag", "first name: ");
        userRef.child("history").child(historyId).child("driver_lname").setValue("");
        userRef.child("history").child(historyId).child("license_number").setValue("");
        userRef.child("history").child(historyId).child("license_expDate").setValue("");
        userRef.child("history").child(historyId).child("reg_number").setValue(taxiNumber);
//        userRef.child("history").child(historyId).child("is_Registered").setValue("NOT ");


        userRef.child("history").child(historyId).child("date").setValue(dateNow);
        userRef.child("history").child(historyId).child("time").setValue(timeNow);
        Log.d("myTag", "Sent to History");

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


        //This intent lets the user send their message to there contacts
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, userFname +" " + userLname + " is using a taxi.\n\n" +"Driver details: \n\n      NOT REGISTERED " + "\n\n  Reg Plate No: " +taxiNumber+"\n\nPick up location:\n     " +mAddress);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }//End of yesBtn

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bobo_menu, menu);
        return true;
    }

    //sign out method
    public void signOut() {
        auth.signOut();
        Log.d("myTag", "SignOut activated");
    }

    @Override
    public void onStart() {
        //Connect to Google API
        googleApiClient.connect();


        //Start MainActivity
        super.onStart();
        auth.addAuthStateListener(mAuthStateListener);
        Log.d("myTag", "onStart " + mAuthStateListener);
    }

    @Override
    public void onStop() {
        //Disconnect from Google API
        googleApiClient.disconnect();
        Log.d("myTag", "Connected" + googleApiClient);
        super.onStop();

        //This listens to see if the user has signed out
        if (mAuthStateListener != null) {
            auth.removeAuthStateListener(mAuthStateListener);
            Log.d("myTag", "onStop ");
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_option:
                //Sign out
                signOut();
                Log.d("myTag", "Sign out");
                return true;
            case R.id.history:
                //Go to History page
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    class AddressReceiver extends ResultReceiver{
        public AddressReceiver (android.os.Handler handler) { super(handler);}


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData){
            //get address or display error message (from intent)
            mAddress = resultData.getString(Constants.ADDRESS_RESULT_KEY);
            Log.d("myTag","Address: -- " +mAddress);
        }

    }

}
