package com.finalproject.youcef.bobo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by Youcef on 02/03/2017.
 */

public class FormActivity extends AppCompatActivity {

    private EditText fname, lname, age1, emerName, emerEmail;
    private ProgressBar progressBar3;
    private Button continBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userform);

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

                final String firstName = fname.getText().toString().trim();
                final String lastName = lname.getText().toString().trim();
                final String eName = emerName.getText().toString().trim();
                final String eEmail = emerEmail.getText().toString().trim();
                final String age = age1.getText().toString().trim();

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
                progressBar3.setVisibility(View.VISIBLE);
            }

        });

    }
}


