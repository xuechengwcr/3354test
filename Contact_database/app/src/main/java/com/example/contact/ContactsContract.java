package com.example.contact;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.contact.AppProvider.CONTENT_AUTHORITY;
import static com.example.contact.AppProvider.CONTENT_AUTHORITY_URI;

public class ContactsContract {
    static final String TABLE_NAME = "Contacts";

    // Contacts fields
    public static class Columns{
        public static final String _ID = BaseColumns._ID;
        public static final String CONTACTS_FIRSTNAME = "FirstName";
        public static final String CONTACTS_LASTNAME = "LastName";
        public static final String CONTACTS_PHONENUMBER = "PhoneNumber";

        private Columns(){
            //private constructor to prevent instantiation
        }
    }

    /**
     * The URI to access the Contacts table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI,TABLE_NAME);

    static final String CONTENT_TYPE = "vnd.android,cursor.dir/vnd." + CONTENT_AUTHORITY + "." + TABLE_NAME;
    static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + " . " + TABLE_NAME;

    static Uri buildContactsUri (long contactsId){
        return ContentUris.withAppendedId(CONTENT_URI, contactsId);
    }

    static long getContactsId (Uri uri){
        return ContentUris.parseId(uri);
    }

}
