package com.finalproject.youcef.bobo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.text.InputFilter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class

MainActivity extends AppCompatActivity implements ValueEventListener, ChildEventListener {

    public static final int DEFAULT_INPUT_LIMIT = 12;
    public static final int RC_SIGN_IN = 1; //RC stands for Request Code

    private Button mCheckButton, noButton, yesButton;
    private EditText taxireg;
    private ProgressBar progressBar;
    private TextView taxiFname, taxiLname, licenseNum, taxiLexp, taxiRegNum, regOk, regNotOk, areYou;
    private String historyId, Uid, firstName, lastName, lnumber, lexpirary, regNum,dateNow, timeNow;
    private Date date, time;



    //////////using classes from the FirebaseDatabase API //////////
    //////////These are Firebase instance variables //////////

    //Entry point for the app to access the database
    private FirebaseDatabase mFirebaseDatabase;
    //DatabaseReference object is a class that references a specific part of the database
    private DatabaseReference mTaxiDatabaseReference, userRef;
    //Authentication Instance variables
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //read from the taxi node on the database
    private ChildEventListener mChildEventListener;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fireba
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        Uid = auth.getCurrentUser().getUid();


        Log.d("myTag","FireAuth user ID" +Uid);

        userRef = FirebaseDatabase.getInstance().getReference("users").child(Uid);
        historyId = userRef.push().getKey();

        //Get the current date
        date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateNow = dateFormat.format(date);
        Log.d("myTag","Current date: " + dateNow);

        //Get the current date
        time = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        timeNow = timeFormat.format(time);
        Log.d("myTag","Current time: " + timeNow);

        //Retrieving values from edit text & button
        taxireg = (EditText) findViewById(R.id.checkTF);
        taxiFname = (TextView) findViewById(R.id.textView2);
        taxiLname = (TextView) findViewById(R.id.textView3);
        licenseNum = (TextView) findViewById(R.id.textView4);
        taxiLexp = (TextView) findViewById(R.id.textView6);
        taxiRegNum = (TextView) findViewById(R.id.textView7);
        mCheckButton = (Button) findViewById(R.id.button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        noButton = (Button) findViewById(R.id.noBtn);
        yesButton = (Button) findViewById(R.id.yesBtn);
        regOk = (TextView) findViewById(R.id.isRegTF);
        regNotOk = (TextView) findViewById(R.id.notRegTf);
        areYou = (TextView) findViewById(R.id.questionTF);


        //Calculate users age




        //Check button
        mCheckButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                mTaxiDatabaseReference = mFirebaseDatabase.getReference().child("taxi_data").child("taxi_details");

                String taxi_detail = taxireg.getText().toString().trim();
                Query a = mTaxiDatabaseReference.orderByChild("car_reg").equalTo(taxi_detail);
                Log.d("myTag","car reg"+mTaxiDatabaseReference);
                Query b = mTaxiDatabaseReference.orderByChild("license_no").equalTo(taxi_detail); //new 24/02

                a.addChildEventListener(MainActivity.this);
                b.addChildEventListener(MainActivity.this); //adds license number query to

                progressBar.setVisibility(View.VISIBLE);



            }

        });

//        get current user------ need this to get user and user details
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        // this listener will be called when there is change in firebase user session - https://firebase.google.com/docs/auth/android/password-auth
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                Log.d("myTag","StateListener user ID");
                if (user == null) {                                                             //User is signed out

                    Log.d("MyTag", "User session has ended");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));          //Go to Login screen
                    Log.d("MyTag", "Signing out user");
                    finish();
                }
            }
        };
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

        //Presents driver details
        taxiFname.setText("First Name: " + newPost.get("first_name").toString());
        taxiLname.setText("Last Name: " + newPost.get("last_name").toString());
        licenseNum.setText("License Number: " + newPost.get("license_no").toString());
        taxiLexp.setText("License Expiry Date : " + newPost.get("license_exp").toString());
        taxiRegNum.setText("Car Reg Number : " + newPost.get("car_reg").toString());

        //Temporary store details so they be stored in history and brought over to next activity
        firstName = newPost.get("first_name").toString();
        lastName = newPost.get("last_name").toString();
        lnumber = newPost.get("license_no").toString();
        lexpirary = newPost.get("license_exp").toString();
        regNum = newPost.get("car_reg").toString();
        Log.d("myTag","stored first name: " + firstName);
        Log.d("myTag","first name: " + newPost.get("first_name"));
        //Stops Progress bar
        progressBar.setVisibility(View.GONE);

        //Displays buttons and "Registered" message
        noButton.setVisibility(View.VISIBLE);
        yesButton.setVisibility(View.VISIBLE);
        regOk.setVisibility(View.VISIBLE);
        areYou.setVisibility(View.VISIBLE);

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.d("myTag","onClickChanged "+dataSnapshot);

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.w("myTag", "postComments:onCancelled", databaseError.toException());
        progressBar.setVisibility(View.GONE);

    }

    //Refreshes the Validation page once the NO button is pressed
    public void noBtn (View v){
        recreate();

        //Clear EditText field
        taxireg.setText("");
    }


    //When the YES button is pressed
    public void yesBtn (View v){

        //Storing taxi driver details to History table / node
        Log.d("myTag","Send to History "+firstName);
        Log.d("myTag","first name: " );
        userRef.child("history").child(historyId).child("driver_lname").setValue(lastName);
        userRef.child("history").child(historyId).child("license_number").setValue(lnumber);
        userRef.child("history").child(historyId).child("license_expDate").setValue(lexpirary);
        userRef.child("history").child(historyId).child("reg_number").setValue(regNum);

        userRef.child("history").child(historyId).child("date").setValue(dateNow);
        userRef.child("history").child(historyId).child("time").setValue(timeNow);
        Log.d("myTag","Sent to History");

        //Confirmation from Firebase Realtime database of upload success
        DatabaseReference dataRef = userRef.child("history").child(historyId).child("driver_fname");
        dataRef.setValue(firstName, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.d("MyTag","databaseError");
                    Toast.makeText(MainActivity.this, "Data update failed", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(MainActivity.this, "Stored to history", Toast.LENGTH_SHORT).show();
                    Log.d("MyTag","database works!");

                }

            }

        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bobo_menu, menu);
        return true;
    }

    //sign out method
    public void signOut() {
        auth.signOut();
        Log.d("myTag","SignOut activated");
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthStateListener);
        Log.d("myTag","onStart "+ mAuthStateListener);
    }

    @Override
    public void onStop() {
        Log.d("myTag","Start onStop method");
        super.onStop();
        if (mAuthStateListener != null) {
            auth.removeAuthStateListener(mAuthStateListener);
            Log.d("myTag","onStop ");
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_option:
                //Sign out
                signOut();
                Log.d("myTag","Sign out");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
