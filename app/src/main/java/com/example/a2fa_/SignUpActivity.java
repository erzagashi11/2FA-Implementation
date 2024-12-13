package com.example.a2fa_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameEditText, surnameEditText, emailEditText, passwordEditText;
    private Button signUpButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DBHelper(this);

        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String surname = surnameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(SignUpActivity.this, "Invalid Email Format", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(password)) {
                    Toast.makeText(SignUpActivity.this, "Password must be at least 6 characters long, contain an uppercase letter, a lowercase letter, and a number", Toast.LENGTH_LONG).show();
                } else {
                    // Ruaj fjalëkalimin si hash përpara shtimit në DB
                    String hashedPassword = hashPasswordWithBcrypt(password);

                    // Shto përdoruesin në DB përmes DBHelper
                    boolean isAdded = dbHelper.addUser(name, surname, email, hashedPassword);

                    if (isAdded) {
                        Toast.makeText(SignUpActivity.this, "Sign-Up Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Sign-Up Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    // Validates email format using Android's Patterns API
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Validates password strength
    private boolean isValidPassword(String password) {
        // Minimum 6 characters, at least 1 uppercase, 1 lowercase, and 1 number
        return password.length() >= 6 &&
                password.matches(".*[A-Z].*") && // At least 1 uppercase letter
                password.matches(".*[a-z].*") && // At least 1 lowercase letter
                password.matches(".*\\d.*");    // At least 1 number
    }

    private String hashPasswordWithBcrypt(String password) {
        return org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
    }

}
