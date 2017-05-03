package com.finalproject.youcef.bobo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Youcef on 10/04/2017.
 */

public class HistoryActivity extends AppCompatActivity{



    private String Uid, historyId, taxiFname, lname, expDate, lNumber, regNumber;
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

//



    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //Firebase instances
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        Uid = auth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(Uid);
        historyId = userRef.push().getKey();


        driverRef = mFirebaseDatabase.getReference("users").child(Uid).child("history");

//        TODO Get data to HistoryData class
//        HistoryData historyData = new HistoryData(driverRef.getText().toString() null);


        Log.d("myTag","");

        Log.d("myTag", "driverRef: " +driverRef);
        historyListView = (ListView) findViewById(R.id.historyLV);

        //Creating ArrayList and connecting it with the History Adapter
        final List<HistoryData> historyDatas = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, R.layout.item_history, historyDatas);
        historyListView.setAdapter(historyAdapter);
        Log.d("myTag", "driverRef: " +historyAdapter);


        Log.d("myTag", "EventListener: " +mChildEventListener);



        // this listener will be called when there is change in firebase user session - https://firebase.google.com/docs/auth/android/password-auth
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                Log.d("myTag","StateListener user ID");
                if (user == null) {                                                             //User is signed out

                    Log.d("MyTag", "User session has ended");
                    startActivity(new Intent(HistoryActivity.this, LoginActivity.class));          //Go to Login screen
                    Log.d("MyTag", "Signing out user");
                    finish();
                }
            }
        };


            if (mChildEventListener == null){
                Log.d("myTag", "EventListener2: " +mChildEventListener);
                mChildEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        HistoryData historyData = dataSnapshot.getValue(HistoryData.class);
                        historyAdapter.add(historyData);
                        Log.d("myTag", "List View Driver first name: " + dataSnapshot);

                        Log.d("myTag", "List View Driver first name: " + historyData);
//
////
//                        Log.d("myTag", "Map " + dataSnapshot);
//                        Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
//                        Log.d("myTag", "Map " + dataSnapshot);


                        //HistoryData historyData = new HistoryData(firstName.getFname().toString(), null);

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
                        Log.d("myTag", "List View Error");

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



    //sign out method
    public void signOut() {
        auth.signOut();
        Log.d("myTag","SignOut activated");
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_option:
                //Sign out
                signOut();
                Log.d("myTag","Sign out");
                return true;
            case R.id.home:
                //Go to Verify page
                startActivity(new Intent(HistoryActivity.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}//Close HistoryActivity
