package com.notepadOne.notepaddone.trash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.notepadOne.notepaddone.Contacts;
import com.notepadOne.notepaddone.DBHandler;
import com.notepadOne.notepaddone.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class Main4Activity extends AppCompatActivity {
    String tv1;
    LinearLayout innerlayout;
    EditText note,title;
    TextView restore,delete;

    final DBHandler2 dbHandler2  = new DBHandler2 (this);
    final DBHandler dbHandler  = new DBHandler(this);
    SaveSharedPreferencess s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Intent in = getIntent();
        tv1 = in.getExtras().getString("title");     //title string
        innerlayout = findViewById(R.id.innerlayout);

        restore =  findViewById(R.id.restore);
        delete = findViewById(R.id.delete);

        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String notes2 =  dbHandler2.getNoteFromTitle(tv1);
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);
                s = new SaveSharedPreferencess();

                if (SaveSharedPreferencess.getIntegerCount(Main4Activity.this) == 0) {


                    s.setIntegerCount(getApplicationContext(),0);

                    dbHandler.addContact(new Contacts(s.getIntegerCount(Main4Activity.this), "" + tv1, "" + notes2,""+formattedDate));
                    s.setIntegerCount(getApplicationContext(),s.getIntegerCount(Main4Activity.this)+1);
                    dbHandler2.deleteItemFromListByTitle(tv1);
                    Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    dbHandler.addContact(new Contacts(s.getIntegerCount(Main4Activity.this) + 1, "" + tv1, "" + notes2,""+formattedDate));
                    s.setIntegerCount(getApplicationContext(), s.getIntegerCount(Main4Activity.this) + 1);
                    dbHandler2.deleteItemFromListByTitle(tv1);
                    Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler2.deleteItemFromListByTitle(tv1);
                Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
                startActivity(intent);
                finish();
            }
        });
        title = (EditText) findViewById(R.id.title);
        note = (EditText) findViewById(R.id.note);
        title = (EditText) findViewById(R.id.title);

        innerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Main4Activity.this, "Restore the note to edit", Toast.LENGTH_SHORT).show();
            }
        });
        title.setText(tv1);
        note.setText(dbHandler2.getNoteFromTitle(tv1));      //getting notes from the title string
        title.setCursorVisible(false);
        note.setCursorVisible(false);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Main4Activity.this, "Restore the note to edit", Toast.LENGTH_SHORT).show();
            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setCursorVisible(true);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }
}

class SaveSharedPreferencess
{
    static final String PREF_USER_NAME= "initial";
    static final String INT_STRING = "initialInteger";
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);

        editor.commit();
    }
    public static void setIntegerCount(Context ctx, int c)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(INT_STRING, c);
        editor.commit();
    }
    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
    public static int getIntegerCount(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(INT_STRING,0);
    }
}
