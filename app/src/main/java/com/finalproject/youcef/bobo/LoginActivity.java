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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Name: Youcef O'Connor
 * Number:x13114557
 * Date: 14 Feb 2017
 * Registration Page.
 *
 */

public class LoginActivity extends AppCompatActivity {
    private EditText pass, email;
    private ProgressBar progressBar;
    private Button loginBtn, registerLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FirebaseAuth fireAuth = FirebaseAuth.getInstance();                                  //Instance of Firebase Auth
        Log.d("MyTag", "get FirebaseAut instance");

        setContentView(R.layout.activity_login);
        Log.d("MyTag", "SetContentView- activity_Login");

        //If the user is already signed in then continue onto the MainActivity
        if (fireAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            Log.d("MyTag", "User already logged in!");
        }

        email = (EditText) findViewById(R.id.emailInput);
        pass = (EditText) findViewById(R.id.passInput);
        registerLink = (Button) findViewById(R.id.signupBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                Log.d("myTag", "Register link");
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MyTag", "Login button onClick");
                String emailInput = email.getText().toString();
                final String password = pass.getText().toString();
                Log.d("MyTag", "retrieve edit text input");
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(emailInput)) {
                    Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);                                                            //When the email & password have been entered and the Login button has been pressed, display progress bar

                //Check the user in put with FirebaseAuth to authenticate user
                fireAuth.signInWithEmailAndPassword(emailInput, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("myTag", "Login user " + email + pass);
                        progressBar.setVisibility(View.GONE);                                                       //stop running progress bar

                        if (!task.isSuccessful()) {
                            if (pass.length() < 8) {                                                                //Minimum length of password characters is 8
                                pass.setError("Password to short, enter a minimum of 8 characters");                //If something is wrong, let the user know via toast
                            } else {
                                Toast.makeText(LoginActivity.this, "Something wrong! Check your email address", Toast.LENGTH_LONG).show();
                            }
                        } else {                                                                                    //If authentication was successful then take the user to MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }


                    } //close onClick method
                });
            }
        });

    }//end of onCreate
}
