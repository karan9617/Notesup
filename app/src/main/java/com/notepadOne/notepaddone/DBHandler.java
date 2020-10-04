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


public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contactsManager2";
    private static final String TABLE_CONTACTS = "contacts2";
    private static final String KEY_ID = "id";
    private static final String KEY_WORD_NAME = "text";
    private static final String KEY_MEANING = "date";
    private static final String KEY_DATE = "datename";



    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_WORD_NAME + " TEXT,"
                + KEY_MEANING + " TEXT,"+ KEY_DATE+" TEXT"+")";

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
    public void addContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,contact.getID());
        values.put(KEY_WORD_NAME, contact.getWordName()); // Contact title
        values.put(KEY_MEANING, contact.getMean()); // Contact note
        values.put(KEY_DATE, contact.getDate()); // Contact date

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    // code to get the single contact
    Contacts getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{KEY_ID,
                        KEY_WORD_NAME, KEY_MEANING,KEY_DATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contacts contact = new Contacts(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return contact;
    }

    // code to get all contacts in a list view
    public List<Contacts> getAllContacts() {
        List<Contacts> contactList = new ArrayList<Contacts>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contacts contact = new Contacts();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setWordName(cursor.getString(1));
                contact.setMean(cursor.getString(2));
                contact.setDate(cursor.getString(3));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WORD_NAME, contact.getWordName());
        values.put(KEY_MEANING, contact.getMean());
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }

    // Deleting single contact
    public void deleteContact(Contacts contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "DELETE FROM "+TABLE_CONTACTS+ " WHERE "+KEY_WORD_NAME+" = "+'"'+contact.getWordName()+'"';
        db.execSQL(selectQuery);

        db.close();
        /*db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();*/
    }


    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // functon to get all voice list
/*
    public List<Contacts> getAllContactsVoice() {
        List<Contacts> contactList = new ArrayList<Contacts>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE "+KEY_VOICE +" = "+ "1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contacts contact = new Contacts();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setWordName(cursor.getString(1));
                contact.setMean(cursor.getString(2));

                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }*/
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
    public void updateExistingTitle(String titlename, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "UPDATE "+TABLE_CONTACTS +" SET " + KEY_MEANING+ " = "+'"'+note+'"'+ " WHERE "+KEY_WORD_NAME+ " = "+'"'+titlename+'"';
        db.execSQL(selectQuery);
        db.close();
    }
    public String getNoteFromTitle(String title) {
        String selectQuery = "SELECT " + KEY_MEANING + " FROM " + TABLE_CONTACTS + " WHERE " + KEY_WORD_NAME + " = " + '"' + title + '"';
        String s;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            s = cursor.getString(0);
            return s;
        } else {
            return "";
        }
    }
    /*
    public void updateStarTo1(String word)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "UPDATE "+TABLE_CONTACTS +" SET " + KEY_STAR+ " = "+'"'+1+'"'+ " WHERE "+KEY_WORD_NAME+ " = "+'"'+word+'"';
        db.execSQL(selectQuery);
        db.close();
    }
    public void updateStarTo0(String word)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "UPDATE "+TABLE_CONTACTS +" SET " + KEY_STAR+ " = "+'"'+0+'"'+ " WHERE "+KEY_WORD_NAME+ " = "+'"'+word+'"';
        db.execSQL(selectQuery);
        db.close();
    }
    public void updateVoiceTo1(String word)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "UPDATE "+TABLE_CONTACTS +" SET " + KEY_VOICE+ " = "+'"'+1+'"'+ " WHERE "+KEY_WORD_NAME+ " = "+'"'+word+'"';
        db.execSQL(selectQuery);
        db.close();
    }
    public void updateVoiceTo0(String word)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "UPDATE "+TABLE_CONTACTS +" SET " + KEY_VOICE+ " = "+'"'+0+'"'+ " WHERE "+KEY_WORD_NAME+ " = "+'"'+word+'"';
        db.execSQL(selectQuery);
        db.close();
    }
    public int wordExistsForMaven(String wordName){
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS +" WHERE "+ KEY_WORD_NAME+" = "+'"'+wordName+'"';

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
    */
}
