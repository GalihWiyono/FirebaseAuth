package com.example.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private TextView forgotpassword, register;
    private EditText email, password;
    private Button signin;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forgotpassword = findViewById(R.id.forgotpassword);
        register = findViewById(R.id.register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);

        firebaseAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setText("");
                password.setText("");
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void loginUser() {
        if(email.getText().toString().isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Please Provide Valid Email!");
            email.requestFocus();
        }
        if(password.getText().toString().isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
        }

        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser checkemail = FirebaseAuth.getInstance().getCurrentUser();
                            if(checkemail.isEmailVerified()) {
                                Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog builder = new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Account Unverified")
                                        .setMessage("Your Account is Unverified,\nPlease Check Your Email \nOr Send New Verification?")
                                        .setPositiveButton("Yes", null)
                                        .setNegativeButton("No", null)
                                        .show();
                                Button positiveBtn = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                positiveBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        checkemail.sendEmailVerification();
                                        Toast.makeText(MainActivity.this, "Email Sent!", Toast.LENGTH_SHORT).show();
                                        builder.dismiss();
                                    }
                                });
                                Button negativBtn = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                                negativBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(MainActivity.this, "Please Check Your Email To Verify Your Account!", Toast.LENGTH_SHORT).show();
                                        builder.dismiss();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed! Please Check Your Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}