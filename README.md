TWO-FACTOR AUTHENTICATION (2FA) PROJECT

This project demonstrates a simple 2FA implementation in Android Studio. The system enhances security by requiring users to verify their login using a one-time password (OTP) sent via email.

Features:

Secure email-based OTP generation and delivery.
User enters OTP in the app to complete authentication.
No data storage for enhanced privacy.

How it works:

User enters their email to request login.
The app generates an OTP and sends it to the user's email.
User inputs the OTP in the app for verification.
Upon successful OTP match, access is granted.

Requirements:

Android Studio
A valid SMTP email server for sending OTPs

Setup:

Clone the repository.
Configure email credentials in the project for SMTP.
Build and run the app on your Android device or emulator.
