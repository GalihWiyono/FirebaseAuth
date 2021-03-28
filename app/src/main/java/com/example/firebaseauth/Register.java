package com.example.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    private EditText email, password, fullname, phonenumber;
    private Button register;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.emailregis);
        password = findViewById(R.id.passwordregis);
        fullname = findViewById(R.id.fullname);
        phonenumber = findViewById(R.id.phonenumber);

        register = findViewById(R.id.signup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registUser();
            }
        });

    }

    private void registUser() {

        //validation
        if(fullname.getText().toString().isEmpty()) {
            fullname.setError("Fullname is required!");
            fullname.requestFocus();
        }
        if(email.getText().toString().isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Please provide valid email address!");
            email.requestFocus();
        }
        if(password.getText().toString().isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
        }
        if(phonenumber.getText().toString().isEmpty()) {
            phonenumber.setError("Phonenumber is required!");
            phonenumber.requestFocus();
        }

        //adding new user
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Users user = new Users(email.getText().toString(),password.getText().toString(),fullname.getText().toString(),phonenumber.getText().toString());

                            //user userId to be reference and make new entry to db
                            FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        fullname.setText("");
                                        email.setText("");
                                        password.setText("");
                                        phonenumber.setText("");
                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(Register.this, "Data Registed, Please Check Your Email to Verify!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Register.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Register.this,"Error, Something Wrong Happened! Please Try Again, Thanks", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    }
                });
    }
}