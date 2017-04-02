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

    private Button mCheckButton;
    private String mUsername;
    private EditText taxireg;
    private ProgressBar progressBar;
    private TextView taxiFname, taxiLname, licenseNum, taxiLexp, taxiRegNum, taxiFname2;
//    final Context context = this;


    //////////using classes from the FirebaseDatabase API //////////
    //////////These are Firebase instance variables //////////

    //Entry point for the app to access the database
    private FirebaseDatabase mFirebaseDatabase;
    //DatabaseReference object is a class that references a specific part of the database
    private DatabaseReference mTaxiDatabaseReference;
    //Authentication Instance variables
    private FirebaseAuth fireAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //read from the taxi node on the database
    private ChildEventListener mChildEventListener;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        fireAuth = FirebaseAuth.getInstance();

        mTaxiDatabaseReference = mFirebaseDatabase.getReference().child("taxi_data2");

        //Retrieving values from edit text & button
        taxireg = (EditText) findViewById(R.id.checkTF);
        taxiFname = (TextView) findViewById(R.id.textView2);
        taxiLname = (TextView) findViewById(R.id.textView3);
        licenseNum = (TextView) findViewById(R.id.textView4);
        taxiLexp = (TextView) findViewById(R.id.textView6);
        taxiRegNum = (TextView) findViewById(R.id.textView7);
        mCheckButton = (Button) findViewById(R.id.button);


        //Check button
        mCheckButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

//                AlertDialog.Builder builder = new AlertDialog.Builder(context); // Declare new Alert Dialog
//                Log.d("myTab","declare new AlertDialog");
//
//                // Get the layout inflater
//                LayoutInflater inflater = getLayoutInflater();

                String taxi_detail = taxireg.getText().toString();
                Query a = mTaxiDatabaseReference.orderByChild("car_reg").equalTo(taxi_detail);
                Log.d("myTag","car reg"+mTaxiDatabaseReference);
                Query b = mTaxiDatabaseReference.orderByChild("license_no").equalTo(taxi_detail); //new 24/02

                a.addChildEventListener(MainActivity.this);
                b.addChildEventListener(MainActivity.this); //adds license number query to


//                builder.setView(inflater.inflate(R.layout.driver_details, null));
//                taxiFname2 = (TextView) findViewById(R.id.textView8);

                // set title
//                builder.setTitle("Your Title");

                // set dialog message
//                Builder
//                        .setMessage("Click yes to exit!")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,int id) {
//                                // if this button is clicked, close
//                                // current activity
//                                MainActivity.this.finish();
//                            }
//                        })
//                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,int id) {
//                                // if this button is clicked, just close
//                                // the dialog box and do nothing
//                                dialog.cancel();
//                            }
//                        });

//                // create alert dialog
//                AlertDialog alertDialog = builder.create();
//
//                // show it
//                alertDialog.show();


            }

        });

//        mAuthStateListener = new FirebaseAuth.AuthStateListener(){
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null){
//                    //user is signed in
//                    Toast.makeText(MainActivity.this, "You are now signed in. Welcome!", Toast.LENGTH_SHORT).show();
////                    onSignedIInitialize(user.getDisplayName());
//                } else {
//                    //user is not signed in
////                    onSignedOutCleanup();
//                    startActivityForResult(
//                            AuthUI.getInstance()
//                                    .createSignInIntentBuilder()
//                                    .setIsSmartLockEnabled(false)
//                                    .setProviders(
//                                            AuthUI.EMAIL_PROVIDER,
//                                            AuthUI.GOOGLE_PROVIDER)
//                                    .build(),
//                            RC_SIGN_IN);
//                }
//
//            }
//        };

        //get current user------ need this to get user and user details
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        // this listener will be called when there is change in firebase user session - https://firebase.google.com/docs/auth/android/password-auth
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {                                                             //User is signed in
                    Log.d("MyTag", "onAuthStateChanged:signed_in:");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));          //User is signed out
                    Log.d("MyTag", "onAuthStateChanged:signed_out");
                    finish();
                }
            }
        };
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == RC_SIGN_IN){
//            if (requestCode == RESULT_OK){
//                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
//            }else if (requestCode == RESULT_CANCELED){
//                Toast.makeText(this, "Sign in Canceled", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.bobo_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.sign_out_option:
//                //Sign out
//                AuthUI.getInstance().signOut(this);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    //this pauses the login procedure at the launch (if user is already logged in)
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mAuthStateListener != null){
//            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
//        }
//    }






//    @Override
//    public Dialog onClickDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        // Get the layout inflater
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(inflater.inflate(R.layout.driver_details, null))
//                // Add action buttons
//                .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // sign in the user ...
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        LoginDialogFragment.this.getDialog().cancel();
//                    }
//                });
//        return builder.create();
//    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
//        Toast.makeText(getApplicationContext(), dataSnapshot.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
        Log.d("myTag","Map "+dataSnapshot);
        taxiFname.setText("First Name: " + newPost.get("first_name").toString());
        taxiLname.setText("Last Name: " + newPost.get("last_name").toString());
        licenseNum.setText("License Number: " + newPost.get("license_no").toString());
        taxiLexp.setText("License Expiry Date : " + newPost.get("license_exp").toString());
        taxiRegNum.setText("Car Reg Number : " + newPost.get("car_reg").toString());

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
//        Log.e(TAG, "Failed to read user", error.toException());

    }

//    public Class CheckTaxi extends AsyncTask<String,String,String>
//    {
//        String y = "";
//        Boolean isSuccess = false;
//
//        @Override
//        protected void onPostExecute(String t){
//        Toast.makeText(MainActivity.this, t, Toast.LENGTH_SHORT).show();
//        if(isSuccess)
//        {
//            Toast.makeText(MainActivity.this , "Taxi is Registered", Toast.LENGTH_LONG).show();
//        }
//    }
//5
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bobo_menu, menu);
        return true;
    }

    //sign out method
    public void signOut() {
        fireAuth.signOut();
        Log.d("myTag","SignOut activated");
    }

    @Override
    public void onStart() {
        super.onStart();
        fireAuth.addAuthStateListener(mAuthStateListener);
        Log.d("myTag","onStart "+ mAuthStateListener);
    }

    @Override
    public void onStop() {
        Log.d("myTag","Start onStop method");
        super.onStop();
        if (mAuthStateListener != null) {
            fireAuth.removeAuthStateListener(mAuthStateListener);
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
