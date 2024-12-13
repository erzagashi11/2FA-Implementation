package com.example.a2fa_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpText;
    private DBHelper dbHelper;
    private String generatedOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpText = findViewById(R.id.signUpText); // TextView to redirect to Sign-Up

        // Redirect to Sign-Up
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        // Login logic
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    if (dbHelper.validateUser(email, password)) {
                        // If credentials are valid, send OTP
                        generatedOtp = generateOtp();
                        sendEmail(email, generatedOtp); // Sends OTP to the user's email
                        Intent intent = new Intent(LoginActivity.this, OtpVerificationActivity.class);
                        intent.putExtra("otp", generatedOtp);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter Email and Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Validates user credentials by checking the database


    // Generates a random 4-digit OTP
    private String generateOtp() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    // Sends the OTP via email
    private void sendEmail(String email, String otp) {
        String subject = "Your OTP Code";
        String message = "Your OTP is: " + otp;

        new EmailSender(email, subject, message).execute();
        Toast.makeText(this, "OTP sent to " + email, Toast.LENGTH_SHORT).show();
    }
}
