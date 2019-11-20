package com.example.contact;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.se.omapi.SEService;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * provider for the contact app. This is the only that know about {@link AppProvider}
 *
 */

public class AppProvider extends ContentProvider {
    private static final String TAG = "AppProvider";
    private AppDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    // uri -> "uniform resource identifier"
    static final String CONTENT_AUTHORITY = "com.example.contact.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int CONTACTS = 100;
    private static final int CONTACTS_ID = 101;

    private static UriMatcher buildUriMatcher (){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // eg. content://com.example.contact.provider/Contacts
        matcher.addURI(CONTENT_AUTHORITY, ContactsContract.TABLE_NAME, CONTACTS);

        // eg. content://com.example.contact.provider/Contacts/7
        matcher.addURI(CONTENT_AUTHORITY, ContactsContract.TABLE_NAME + "/#", CONTACTS_ID);
        return matcher;
    }



    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Log.d(TAG, "query: called with URI" + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "query: match is" + match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (match){
            case CONTACTS:
                queryBuilder.setTables(ContactsContract.TABLE_NAME);
                break;
            case CONTACTS_ID:
                queryBuilder.setTables(ContactsContract.TABLE_NAME);
                long contactId = ContactsContract.getContactsId(uri);
                queryBuilder.appendWhere(ContactsContract.Columns._ID + " = " + contactId);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, strings, s, strings1, null, null, s1);
        return null;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case CONTACTS:
                return ContactsContract.CONTENT_TYPE;
            case CONTACTS_ID:
                return ContactsContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.d(TAG, "Entering insert, called with uri: " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;

        Uri returnUri;
        long recordId;
        switch(match){
            case CONTACTS:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(ContactsContract.TABLE_NAME, null, contentValues);
                if (recordId >= 0){
                    returnUri = ContactsContract.buildContactsUri(recordId);
                }else{
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
        Log.d(TAG, "Exiting insert, returning " + returnUri);
        return returnUri;

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG, "update called with uri " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;
        switch(match){
            case CONTACTS:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(ContactsContract.TABLE_NAME, s, strings);
                break;
            case CONTACTS_ID:
                db = mOpenHelper.getWritableDatabase();
                long contactsId = ContactsContract.getContactsId(uri);
                selectionCriteria = ContactsContract.Columns._ID + " = " + contactsId;

                if((s != null) && s.length()>0){
                    selectionCriteria += "AND (" + s + ")";
                }
                count = db.delete(ContactsContract.TABLE_NAME,s,strings);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
        return count;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG, "update called with uri " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;

        String selectionCriteria;
        switch(match){
            case CONTACTS:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(ContactsContract.TABLE_NAME, contentValues, s, strings);
                break;
            case CONTACTS_ID:
                db = mOpenHelper.getWritableDatabase();
                long contactsId = ContactsContract.getContactsId(uri);
                selectionCriteria = ContactsContract.Columns._ID + " = " + contactsId;

                if((s != null) && s.length()>0){
                    selectionCriteria += "AND (" + s + ")";
                }
                count = db.update(ContactsContract.TABLE_NAME,contentValues,s,strings);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }
        return count;
    }
}
