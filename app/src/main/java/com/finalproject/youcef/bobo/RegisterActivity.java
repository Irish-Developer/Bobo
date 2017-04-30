package com.finalproject.youcef.bobo;

/**************************************************************************************************************************
 * References for regular expression:
 *
 * Password:    @uthor=mkyong | Website= MKYONG | Web page= How to validate password with regular expression | URL=https://www.mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression/
 *
 *******************************************************************************************************************************/


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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;


/**
 * Name: Youcef O'Connor
 * Number:x13114557
 * Date: 14 Feb 2017
 * Registration Page.
 *
 */

public class RegisterActivity extends AppCompatActivity implements Serializable {

    private EditText pass, email;
    private ProgressBar progressBar2;
    private Button loginLink, signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final FirebaseAuth fireAuth = FirebaseAuth.getInstance();         //Get FirebaseAuth instance https://firebase.google.com/docs/auth/android/password-auth                         //Instance of Firebase Auth
        Log.d("MyTag", "get FirebaseAut instance");

        email = (EditText) findViewById(R.id.addEmail);
        pass = (EditText) findViewById(R.id.createPass);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar);
        loginLink = (Button) findViewById(R.id.alreadyRegBtn);
        signupBtn = (Button) findViewById(R.id.regBtn);


        loginLink.setOnClickListener(new View.OnClickListener() {           //when the login link is press, the user is taken to the login screen
            @Override
            public void onClick(View v) {
            finish();
                Log.d("myTag","login link");
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String password = pass.getText().toString().trim();
                final String addEmail = email.getText().toString().trim();
                String passPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,30})";

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Create password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password needs a minimum of 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Checks the pattern of the password
                if (password.matches(passPattern))
                {
                    Log.d("myTag","valid Password " + password);
                }
                else
                {
                    Log.d("myTag","Invalid password " + password);
                    Toast.makeText(getApplicationContext(),"Invalid Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(addEmail)) {
                    Toast.makeText(getApplicationContext(), "Add Email address", Toast.LENGTH_SHORT).show();
                    return;
                }



                //When the entered details have met the standard, create user
                //Display progress bar while creating user until auth result has returned
                progressBar2.setVisibility(View.VISIBLE);

                //Creating user with email and password
                fireAuth.createUserWithEmailAndPassword(addEmail, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "Create User with email:onComplete" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        Log.d("myTag", "Create User " + email + password);
                        progressBar2.setVisibility(View.GONE);                  //Stop running the progressbar

                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(RegisterActivity.this, FormActivity.class));
                            finish();
                        }
                    }

                });


            }
        });
    }
            @Override
            protected void onResume() {
                super.onResume();
                progressBar2.setVisibility(View.GONE);
            }

}
