package com.notepadOne.notepaddone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.notepadOne.notepaddone.Database.DBHandlerForLock;
import com.notepadOne.notepaddone.Database.DBHandlerForSession;

public class ChangeEmailActivity extends AppCompatActivity {

    Button reset;
    EditText emailtextInPattern;
    RelativeLayout relative;
    TextView errorMessage,changeText;
    final DBHandlerForLock dbHandlerForLock = new DBHandlerForLock(this);
    final DBHandlerForSession dbHandlerForSession = new DBHandlerForSession(this);

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        reset = findViewById(R.id.reset);

        changeText = findViewById(R.id.changeText);
        errorMessage = findViewById(R.id.errorMessage);
        errorMessage.setVisibility(View.INVISIBLE);

        emailtextInPattern = findViewById(R.id.emailtextInPattern);
        relative = findViewById(R.id.relative);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = emailtextInPattern.getText().toString();

                if (!reset.getText().equals("SET PASSWORD")) {
                    if (dbHandlerForLock.getAllContacts().get(0).getMean().equalsIgnoreCase(pass)) {
                        changeText.setText("Enter new email ID:");
                        errorMessage.setVisibility(View.INVISIBLE);
                        reset.setText("SET PASSWORD");
                        emailtextInPattern.setText("");
                    } else {
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("Incorrect Email ID");
                        message("Incorrect Email ID");
                    }
                }
                else{
                    String pass2 = emailtextInPattern.getText().toString().trim();
                    if(pass2.length() > 0){
                        if(pass2.contains("@")){
                            errorMessage.setVisibility(View.INVISIBLE);
                            String passwordReal = dbHandlerForLock.getAllContacts().get(0).getWordName();
                            String type = dbHandlerForLock.getAllContacts().get(0).getDate();
                            dbHandlerForLock.droptable();
                            dbHandlerForLock.addContact(new ContactsForLock(passwordReal,pass2,type));

                            Intent i = new Intent(ChangeEmailActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                        else{
                            errorMessage.setVisibility(View.VISIBLE);
                            errorMessage.setText("Please enter a valid email");
                        }
                    }
                    else{
                        errorMessage.setVisibility(View.VISIBLE);
                        errorMessage.setText("Please enter a valid email");
                    }
                }
            }
        });

    }
    public void message(String message){
        Snackbar snackbar = Snackbar
                .make(relative, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void showSoftKeyboard(View view){
        if(view.requestFocus()){
            InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


}