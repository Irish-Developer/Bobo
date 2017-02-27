package com.finalproject.youcef.bobo;

import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.util.Log;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity implements ValueEventListener, ChildEventListener {

    public static final int DEFAULT_INPUT_LIMIT = 12;

    private Button mCheckButton;
    private EditText taxireg;
    private ProgressBar progressBar;

    //connection variables
    private String usern, pass;
    private String ip, db;
    private Connection con;

    //////////using classes from the FirebaseDatabase API //////////
    //////////These are Firebase instance variables //////////

    //Entry point for the app to access the database
    private FirebaseDatabase mFirebaseDatabase;
    //DatabaseReference object is a class that references a specific part of the database
    private DatabaseReference mTaxiDatabaseReference;
    //read from the taxi node on the database
    private ChildEventListener mChildEventListener;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mTaxiDatabaseReference = mFirebaseDatabase.getReference().child("taxi_data2");

        //Retrieving values from edit text & button
        taxireg = (EditText) findViewById(R.id.checkTF);
        mCheckButton = (Button) findViewById(R.id.button);

//        //need to declare connection attributes
//        ip = "server ip";
//        db = "name of database";
//        usern = "db username";
//        pass = "db password";

        //Check button
        mCheckButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String taxi_detail = taxireg.getText().toString();
                //DatabaseReference ref = mTaxiDatabaseReference.child("1");
                //DatabaseReference name = ref.child("last_name");
                Query a = mTaxiDatabaseReference.orderByChild("car_reg").equalTo(taxi_detail);
                Log.d("myTag","car reg"+mTaxiDatabaseReference);
                Query b = mTaxiDatabaseReference.orderByChild("license_no").equalTo(taxi_detail); //new 24/02

                a.addChildEventListener(MainActivity.this);
                b.addChildEventListener(MainActivity.this); //new
                //name.addListenerForSingleValueEvent(MainActivity.this);
//                Toast.makeText(getApplicationContext(), name.getKey(), Toast.LENGTH_LONG).show();
//                CheckTaxi checkTaxi = new CheckTaxi();
//                checkTaxi.execute("");
            }

        });

    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
//        Toast.makeText(getApplicationContext(), dataSnapshot.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Map<String, Object> newPost = (Map<String, Object>) dataSnapshot.getValue();
        Log.d("myTag","Map "+dataSnapshot);
        Toast.makeText(getApplicationContext(), newPost.get("first_name").toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), newPost.get("last_name").toString(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), newPost.get("license_exp").toString(), Toast.LENGTH_SHORT).show();
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
}
