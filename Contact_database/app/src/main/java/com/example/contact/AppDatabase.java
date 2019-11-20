package com.example.contact;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Basic database class for the application
 * The only class that should use this is AppProvider
 */

class AppDatabase extends SQLiteOpenHelper {
    private static final String TAG = "AppDatabase";

    public static final String DATABASE_NAME = "Contacts.db";
    public static final String DATABASE_TABLE = "Contacts";
    public static final int DATABASE_VERSION = 2;

    //Implement AppDatabase as a Singleton
    private static AppDatabase instance = null;

    // set up the constructor to private
    // design pattern: singleton -> a singleton class ensure that there's only one instance of the class
    private AppDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "AppDatabase: constructor");
    }

    /**
     * Get an instance of the app's singleton database helper object
     * @param context the content provides context.
     * @return a SQLite database helper object
     */
    static AppDatabase getInstance(Context context){
        if (instance == null){
            Log.d(TAG, "getInstance: creating new instance");
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: starts");
        String sSQL; // use a String variable to facilitate logging
    //    sSQL = "CREATE TABLE Contacts (_id INTEGER PRIMARY KEY NOT NULL, FirstName TEXT NOT NULL, LastName TEXT, PhoneNumber INTEGER);";
        sSQL = "CREATE TABLE " + ContactsContract.TABLE_NAME + "("
                + ContactsContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL,"
                + ContactsContract.Columns.CONTACTS_FIRSTNAME + " TEXT NOT NULL,"
                + ContactsContract.Columns.CONTACTS_LASTNAME + " TEXT,"
                + ContactsContract.Columns.CONTACTS_PHONENUMBER + " INTEGER"
        + ");";
        Log.d(TAG,sSQL);
        sqLiteDatabase.execSQL(sSQL);
        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG, "onUpgrade: starts");
        switch (i){
            case 1:
                //upgrade logic from version 1
                break;
                default:
                    throw new IllegalStateException("onUpgrade() with unknown newVersion: " + i1);
        }
        Log.d(TAG, "onUpgrade: ends");

    }


}
