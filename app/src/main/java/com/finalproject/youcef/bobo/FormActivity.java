package com.finalproject.youcef.bobo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * Created by Youcef on 02/04/2017.
 */

public class FormActivity extends AppCompatActivity {

    private EditText fname, lname, age;
    private ProgressBar progressBar2;
    private Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userform);
    }

}


