package com.finalproject.youcef.bobo;

/**************************************************************************************************************************
 * References:
 *
 * @uthor=  Ravi Tamada | Website= Android Hive | Web page= Android Getting Started with Firebase – Login and Registration Authentication | URL= http://www.androidhive.info/2016/06/android-getting-started-firebase-simple-login-registration-auth/
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

/**
 * Name: Youcef O'Connor
 * Number: x13114557
 * Date: 14 Feb 2017
 * Class: LoginActivity
 */

public class LoginActivity extends AppCompatActivity {

    private EditText pass, email;
    private ProgressBar progressBar;
    private Button loginBtn, registerLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Firebase Authentication Instance variables
        final FirebaseAuth fireAuth = FirebaseAuth.getInstance();

        //If the user is already signed in then continue onto the MainActivity
        if (fireAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        email = (EditText) findViewById(R.id.emailInput);
        pass = (EditText) findViewById(R.id.passInput);
        registerLink = (Button) findViewById(R.id.signupBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Register Link
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        //Login Button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get email and password from EditText fields to string
                String emailInput = email.getText().toString();
                String password = pass.getText().toString();

                //If fields are empty then display Toast message
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(emailInput)) {
                    Toast.makeText(getApplicationContext(), "Please enter email address", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);                                                            //When the email & password have been entered and the Login button has been pressed, display progress bar

                //Check the user input with FirebaseAuth to authenticate user
                fireAuth.signInWithEmailAndPassword(emailInput, password).addOnCompleteListener(LoginActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);                                                       //stop running progress bar

                                if (!task.isSuccessful()) {
                                    if (pass.length() < 8) {                                                                //Minimum length of password characters is 8
                                        pass.setError("Password is not valid");                                             //If something is wrong, let the user know via toast
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Email address not valid", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    //If authentication was successful then take the user to MainActivity

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); //close activity
                                }

                            } //close onClick method
                        });
            }
        });

    }//end of onCreate
}
