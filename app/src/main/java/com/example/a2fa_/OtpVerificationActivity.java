package com.example.a2fa_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class OtpVerificationActivity extends AppCompatActivity {
    private EditText otpEditText;
    private Button verifyOtpButton, resendOtpButton;

    private String correctOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        otpEditText = findViewById(R.id.otpEditText);
        verifyOtpButton = findViewById(R.id.verifyOtpButton);
        resendOtpButton = findViewById(R.id.resendOtpButton);

        // Merr OTP fillestare nga intent
        correctOtp = getIntent().getStringExtra("otp");

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = otpEditText.getText().toString().trim();

                if (enteredOtp.equals(correctOtp)) {
                    Intent intent = new Intent(OtpVerificationActivity.this, VerifiedActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(OtpVerificationActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                correctOtp = generateNewOtp();
                String email = getIntent().getStringExtra("email");
                sendEmail(email, correctOtp);
            }
        });
    }

    // Metodë për të gjeneruar një OTP të re
    private String generateNewOtp() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    // Sends the OTP via email
    private void sendEmail(String email, String otp) {
        String subject = "Your OTP Code";
        String message = "Your new OTP is: " + otp;

        new EmailSender(email, subject, message).execute();
        Toast.makeText(this, "OTP sent to " + email, Toast.LENGTH_SHORT).show();
    }
}
