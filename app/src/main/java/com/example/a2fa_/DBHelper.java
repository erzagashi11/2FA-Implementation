package com.example.a2fa_;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, surname TEXT, email TEXT UNIQUE, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM users WHERE email = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            String storedHash = cursor.getString(0); // Merr hash-in e ruajtur
            cursor.close();
            db.close();
            // Kontrollo nëse fjalëkalimi përputhet me hash-in
            return org.mindrot.jbcrypt.BCrypt.checkpw(password, storedHash);
        } else {
            cursor.close();
            db.close();
            return false; // Përdoruesi nuk ekziston
        }
    }

    public boolean addUser(String name, String surname, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("surname", surname);
        values.put("email", email);
        values.put("password", password);

        long result = db.insert("users", null, values);
        db.close();
        return result != -1; // Kthe true nëse shtimi në DB ishte i suksesshëm
    }



}
