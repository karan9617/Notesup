package com.notepadOne.notepaddone;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// MAIN DATABASE FOR THE MAIN WORD LIST
//
//


public class DBHandler21 extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager211";
    private static final String TABLE_CONTACTS = "contacts211";
    private static final String KEY_ID = "id";
    private static final String KEY_WORD_NAME = "text";
    private static final String KEY_MEANING = "image";
    private static final String KEY_DATE = "datename";

    public DBHandler21(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_WORD_NAME + " TEXT,"
                + KEY_MEANING + " BLOB,"+ KEY_DATE+" TEXT"+")";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    public void onUpgrade(SQLiteDatabase db, int j, int i) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addContact(Contacts21 contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,contact.getID());
        values.put(KEY_WORD_NAME, contact.getWordName()); // Contact title
        values.put(KEY_MEANING, contact.getMean());
        values.put(KEY_DATE, contact.getDate()); // Contact date

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    // code to get all contacts in a list view
    public List<Contacts21> getAllContacts() {
        List<Contacts21> contactList = new ArrayList<Contacts21>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contacts21 contact = new Contacts21();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setWordName(cursor.getString(1));
                contact.setMean(cursor.getBlob(2));
                contact.setDate(cursor.getString(3));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
    // code to update the single contact
    public int updateContact(Contacts21 contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WORD_NAME, contact.getWordName());
        values.put(KEY_MEANING, contact.getMean());
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    public String getAllDetailsOfWord(String wordName){
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS +" WHERE "+ KEY_WORD_NAME+" = "+'"'+wordName+'"';

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int x = cursor.getCount();
        if(x==0){
            return "1";
        }else {
            String s = cursor.getString(0)+":" + cursor.getString(1)+":" + cursor.getString(2)+":" + cursor.getString(3)+":" +
                    cursor.getString(4)+":"+cursor.getString(5);
            return s;
        }

    }

    public void deleteItemFromListByTitle(String titlename){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "DELETE FROM "+TABLE_CONTACTS+ " WHERE "+KEY_WORD_NAME+" = "+'"'+titlename+'"';
        db.execSQL(selectQuery);
        db.close();
    }
    public int ifTitleExists(String title){
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS +" WHERE "+ KEY_WORD_NAME+" = "+'"'+title+'"';

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int x = cursor.getCount();
        if(x==1){
            return 1;
        }
        else{
            return 0;
        }
    }
    public void updateExistingTitle(String titlename, byte[] note){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "UPDATE "+TABLE_CONTACTS +" SET " + KEY_MEANING+ " = "+'"'+note+'"'+ " WHERE "+KEY_WORD_NAME+ " = "+'"'+titlename+'"';
        db.execSQL(selectQuery);
        db.close();
    }

    public byte[] getNoteImageinBytesFromTitle(String title) {
        String selectQuery = "SELECT " + KEY_MEANING + " FROM " + TABLE_CONTACTS + " WHERE " + KEY_WORD_NAME + " = " + '"' + title + '"';
        byte[] image = null;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            image = cursor.getBlob(0);
            return image;
        } else {
            return null;
        }
    }

}