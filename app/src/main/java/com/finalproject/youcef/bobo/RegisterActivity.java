package com.finalproject.youcef.bobo;

/**************************************************************************************************************************
 * References:
 *
 * @uthor = Ravi Tamada | Website= Android Hive | Web page = Android Getting Started with Firebase – Login and Registration Authentication | URL= http://www.androidhive.info/2016/06/android-getting-started-firebase-simple-login-registration-auth/
 * @uthor = mkyong | Website= MKYONG | Web page = How to validate password with regular expression | URL=https://www.mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression/
 * @uthor = Firebase | Website = Firebase | Web page = Authenticate with Firebase using Password-Based Accounts on Android | URL = https://firebase.google.com/docs/auth/android/password-auth
 * @uthor = Google | Website = Android Developers | Web page = The Activity Lifecycle | URL = https://developer.android.com/guide/components/activities/activity-lifecycle.html
 *
 *******************************************************************************************************************************/


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

        //Instance of Firebase
        final FirebaseAuth fireAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.addEmail);
        pass = (EditText) findViewById(R.id.createPass);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar);
        loginLink = (Button) findViewById(R.id.alreadyRegBtn);
        signupBtn = (Button) findViewById(R.id.regBtn);


        //Login link
        loginLink.setOnClickListener(new View.OnClickListener() {           //when the login link is press, the user is taken to the login screen
            @Override
            public void onClick(View v) {
            finish();
            }
        });

        //Register Button
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String password = pass.getText().toString().trim();
                final String addEmail = email.getText().toString().trim();

                //Regular expression for password
                String passPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,30})";

                //Checks to make sure password is not empty
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Create password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Lets the user know if there password is too short
                if (password.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password needs a minimum of 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Checks the pattern of the password
                if (password.matches(passPattern))
                {
                    //Proceed
                }
                else
                {

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
                fireAuth.createUserWithEmailAndPassword(addEmail, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterActivity.this, "Account Created", Toast.LENGTH_SHORT).show();

                                progressBar2.setVisibility(View.GONE);                  //Stop running the progressbar

                                // Registration is not successful, display message
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                                } else {
                                    //take the user to user form
                                    startActivity(new Intent(RegisterActivity.this, FormActivity.class));
                                    finish();
                                }
                            }

                        });


            }
        });
    }
            //Keep RegisterActivity active / open until the user changes Activity or is interrupted
            @Override
            protected void onResume() {
                super.onResume();
                progressBar2.setVisibility(View.GONE);
            }

}
