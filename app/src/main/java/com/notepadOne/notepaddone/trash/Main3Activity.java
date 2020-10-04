package com.notepadOne.notepaddone.trash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.notepadOne.notepaddone.MainActivity;
import com.notepadOne.notepaddone.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Main3Activity extends AppCompatActivity {
    EditText mainedit;
    final DBHandler2 dbHandler2  = new DBHandler2 (this);      //database for main list
    ArrayList<Contact2> arr = new ArrayList<>();
    ListView trashlistview;
    boolean searched=false;
    TextView emptytrash,emptytrashbutton;
    CustomAdapter3 customAdapter;

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    public AlertDialog.Builder buildDialog(Context c) {                                                     //DIALOG FOR EMPTY TRASH
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Notes");
        builder.setMessage("Do you want to empty trash..?");

        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        }).setNegativeButton("Empty Trash", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHandler2.emptyTrash();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Trash Emptied", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setIcon(R.drawable.logo);
        return builder;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        mainedit = findViewById(R.id.mainedit);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);         //HIDE KEYBOARD

        trashlistview = findViewById(R.id.trashlistview);
        emptytrash = findViewById(R.id.emptytrash);
        emptytrashbutton = findViewById(R.id.emptytrashbutton);                        //EMPTY WHOLE TRASH BUTTON


        List<Contact2> contacts = dbHandler2.getAllContacts();
        for (Contact2 cn : contacts) {
            arr.add(cn);
        }
        customAdapter = new CustomAdapter3(Main3Activity.this, arr);
        trashlistview.setAdapter(customAdapter);                                //SET ADAPTER FOR LIST ITEMS
        if(arr.isEmpty() == true){emptytrash.setVisibility(View.VISIBLE);}else{emptytrash.setVisibility(View.INVISIBLE);}

        emptytrashbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDialog(Main3Activity.this).show();

            }
        });
        mainedit.addTextChangedListener(new TextWatcher() {                                         //MAIN EDIT EDITTEXT
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ArrayList<String> type_name_filter = new ArrayList<>();

                String text = editable.toString();

                for (int i = 0; i < arr.size(); i++) {

                    if ((arr.get(i).getWordName().toString().toLowerCase()).contains(text.toLowerCase())) {
                        type_name_filter.add(arr.get(i).getWordName()+":"+arr.get(i).getMean());

                    }
                }
                listUpdate(type_name_filter);
            }
        });
    }


    public void listUpdate(ArrayList<String> data) {
        searched = true;
        CustomAdapter4 customAdapter4 = new CustomAdapter4(Main3Activity.this, data);
        trashlistview.setAdapter(customAdapter4);
        // listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, data));

    }

    @Override
    public void onBackPressed() {
        if(searched){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

}
