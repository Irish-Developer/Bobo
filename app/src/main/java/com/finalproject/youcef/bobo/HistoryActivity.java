package com.finalproject.youcef.bobo;


/**************************************************************************************************************************
 * References:
 *
 * @uthor= Google | Website= Udacity | Web page= Firebase in a Weekend: Android | URL= https://www.udacity.com/course/firebase-in-a-weekend-by-google-android--ud0352
 *
 *
 *******************************************************************************************************************************/


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * @uthor: Youcef O'Connor
 * Date: 10/04/2017.
 * Student No: x13114557
 */


public class HistoryActivity extends AppCompatActivity {


    private String Uid, historyId;
    private HistoryAdapter historyAdapter;
    private ListView historyListView;


    //////////using classes from the FirebaseDatabase API //////////
    //////////These are Firebase instance variables //////////

    //Entry point for the app to access the database
    private FirebaseDatabase mFirebaseDatabase;
    //DatabaseReference object is a class that references a specific part of the database
    private DatabaseReference driverRef, timeRef, userRef;
    //Authentication Instance variables
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //read from the taxi node on the database
    private ChildEventListener mChildEventListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Firebase instances
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        //Get user ID
        Uid = auth.getCurrentUser().getUid();

        //Get reference to user ID in "users" node
        userRef = FirebaseDatabase.getInstance().getReference("users").child(Uid);

        //Get history ID for every taxi used
        historyId = userRef.push().getKey();

        //Get reference to "history" in "users" node
        driverRef = mFirebaseDatabase.getReference("users").child(Uid).child("history");

        historyListView = (ListView) findViewById(R.id.historyLV);

        //Creating ArrayList and connecting it with the History Adapter
        final List<HistoryData> historyDatas = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, R.layout.item_history, historyDatas);
        historyListView.setAdapter(historyAdapter);

        // this listener will be called when there is change in firebase user session - https://firebase.google.com/docs/auth/android/password-auth
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {                                                             //User is signed out
                    startActivity(new Intent(HistoryActivity.this, LoginActivity.class));          //Go to Login screen
                    finish();
                }
            }
        };

        //This is called when data has been found
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //Uses HistoryData class to sort the data and then gets data returned
                    HistoryData historyData = dataSnapshot.getValue(HistoryData.class);
                    //Sends data from HistoryData to HistoryAdapter
                    historyAdapter.add(historyData);
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
            };

            driverRef.addChildEventListener(mChildEventListener);
        }

    }//Close onCreate


    //Display options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bobo_menu, menu);
        return true;
    }

    //On HistoryActivity startup
    @Override
    public void onStart() {
        //Check authentication on startup
        super.onStart();
        auth.addAuthStateListener(mAuthStateListener);
        Log.d("myTag", "onStart " + mAuthStateListener);
    }

    //Stopping HistoryActivity
    @Override
    public void onStop() {
        super.onStop();

        //This listens to see if the user has signed out
        if (mAuthStateListener != null) {
            auth.removeAuthStateListener(mAuthStateListener);
        }
    }

    //sign out method
    public void signOut() {
        auth.signOut();
    }

    //Main menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_option:
                //Sign out
                signOut();
                return true;
            case R.id.home:
                //Go to Verify page
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}//Close HistoryActivity
