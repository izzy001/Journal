package com.example.android.journal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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



public class MainActivity extends AppCompatActivity {

    //defining new objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;
    private Button buttonSignin;
    private ProgressBar mLoadingIndicator;


    //firebaseauth object
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing firebase Auth object
        mAuth = FirebaseAuth.getInstance();

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);
        buttonSignin = (Button) findViewById(R.id.buttonSignin);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        //attaching listener to buttons

        buttonSignin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //calling signinUser method on click
                signinUser();
            }
        });

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calling signupUser method on click
                signupUser();
            }
        });
    }

    private void signinUser() {

        //getting email and password from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //display progressbar
        mLoadingIndicator.setVisibility(View.VISIBLE);


        //if user exists

       mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent = new Intent(MainActivity.this, ChildActivity.class);

                        if (task.isSuccessful()) {

                            startActivity(intent);
                            finish();
                            //Sign in success
                            Toast.makeText(MainActivity.this, "Sign In successful", Toast.LENGTH_SHORT).show();


                        }else {
                            //Sign in error
                            Toast.makeText(MainActivity.this, "Error Signing In", Toast.LENGTH_SHORT).show();
                        }

                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                    }
                });

    }


    private void signupUser() {

        //getting email and password from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        mLoadingIndicator.setVisibility(View.VISIBLE);



         //creating a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Intent intent = new Intent(MainActivity.this, ChildActivity.class);
                        //checking if success
                        if (task.isSuccessful()) {
                            startActivity(intent);
                            finish();
                            //display some message here
                            Toast.makeText(MainActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();


                        }else{
                            //display error message
                            Toast.makeText(MainActivity.this, "Registration error", Toast.LENGTH_SHORT).show();
                        }
                        mLoadingIndicator.setVisibility(View.INVISIBLE);
                    }

                });

    }



}
