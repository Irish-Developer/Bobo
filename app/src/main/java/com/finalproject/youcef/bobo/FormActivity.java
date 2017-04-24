package com.finalproject.youcef.bobo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.models.User;

import java.lang.ref.Reference;

/**
 * Created by Youcef on 02/03/2017.
 */

public class FormActivity extends AppCompatActivity {

    private EditText fname, lname, age1, emerName, emerEmail;
    private ProgressBar progressBar3;
    private Button continBtn;
    private DatabaseReference mDatabaseRef;
    private String userId;
    private FirebaseAuth auth;
    private String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userform);

        auth = FirebaseAuth.getInstance();
        Uid = auth.getCurrentUser().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(Uid);      //Instance of DatabaseReference needed to read and write to Firebase
            Log.d("MyTag", "uid: " +Uid);


        userId = mDatabaseRef.push().getKey();
//            Log.d("MyTag", "user id: " +userId);
//        FirebaseDatabase = mDatabaseRef.getReference("users");

        fname = (EditText) findViewById(R.id.firstName);
        lname = (EditText) findViewById(R.id.lastName);
        emerName = (EditText) findViewById(R.id.emerConName);
        emerEmail = (EditText) findViewById(R.id.emerConEmail);
        age1 = (EditText) findViewById(R.id.ageET);
        continBtn = (Button) findViewById(R.id.continueBtn);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);


        continBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = fname.getText().toString().trim();
                String lastName = lname.getText().toString().trim();
                String eName = emerName.getText().toString().trim();
                String eEmail = emerEmail.getText().toString().trim();
                String age = age1.getText().toString().trim();
                String ePattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


                if (TextUtils.isEmpty(firstName)) {
                    Toast.makeText(getApplicationContext(), "Add your first name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(lastName)) {
                    Toast.makeText(getApplicationContext(), "Add your last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(age)) {
                    Toast.makeText(getApplicationContext(), "Add your age", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(eName)) {
                    Toast.makeText(getApplicationContext(), "Add your emergency contact name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(eEmail)) {
                    Toast.makeText(getApplicationContext(), "Add your emergency contacts email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // onClick of button perform this simplest code.
                if (eEmail.matches(ePattern))
                {
                    Log.d("myTag","valid Email Address " + eEmail);
//                    Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.d("myTag","Invalid Email Address " + eEmail);
                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar3.setVisibility(View.VISIBLE);


                if (!TextUtils.isEmpty(eEmail))
                    mDatabaseRef.child(userId).child("contacts").child("emerEmail").setValue(eEmail);

                if (!TextUtils.isEmpty(eName))
                    mDatabaseRef.child(userId).child("contacts").child("emerName").setValue(eName);

                if (!TextUtils.isEmpty(age))
                    mDatabaseRef.child(userId).child("age").setValue(age);

                if (!TextUtils.isEmpty(lastName))
                    mDatabaseRef.child(userId).child("lname").setValue(lastName);


                if (!TextUtils.isEmpty(firstName))
                Log.d("MyTag", "firstName");

                //Confirmation that from Firebase Realtime database of upload success
                DatabaseReference dataRef = mDatabaseRef.child(userId).child("fname");
                dataRef.setValue(firstName, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Log.d("MyTag","databaseError");
                            Toast.makeText(FormActivity.this, "Data update failed", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(FormActivity.this, MainActivity.class));
                            Toast.makeText(FormActivity.this, "Welcome to Bobo!", Toast.LENGTH_SHORT).show();
                            Log.d("MyTag","database works!");
                            finish();
                        }

                    }

                });

            }
        });
    }
}
