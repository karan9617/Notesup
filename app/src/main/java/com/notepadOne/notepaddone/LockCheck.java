package com.notepadOne.notepaddone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.google.android.material.snackbar.Snackbar;
import com.notepadOne.notepaddone.Database.DBHandlerForLock;
import com.notepadOne.notepaddone.Database.DBHandlerForSession;

import java.util.List;
import java.util.concurrent.locks.Lock;

public class LockCheck extends AppCompatActivity implements PatternLockViewListener {

    final DBHandlerForLock dbHandlerForLock = new DBHandlerForLock(this);
    RelativeLayout relative;
    LinearLayout linearOfPassword,linearPattern,linearForgot,linearPassSetup;
    PatternLockView mPatternLockView;
    ImageView backArrow;
    Button click,forgotClick,setupClick;
    EditText passwordText,edittextForEmail,edittextForSetup1,edittextForSetup2;
    TextView passwordmessage,patternmessage,forgot,messageForget,setupText,forgotPattern;
    final DBHandlerForSession dbHandlerForSession = new DBHandlerForSession(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_check);
        relative = findViewById(R.id.relative);
        linearPassSetup = findViewById(R.id.linearPassSetup);
        linearPassSetup.setVisibility(View.INVISIBLE);
        forgotPattern = findViewById(R.id.forgotPattern);
        messageForget = findViewById(R.id.messageForget);
        edittextForSetup1 = findViewById(R.id.edittextForSetup1);
        edittextForSetup2 = findViewById(R.id.edittextForSetup2);
        setupText = findViewById(R.id.setupText);
        forgotClick= findViewById(R.id.forgotClick);
        linearForgot = findViewById(R.id.linearForgot);
        linearForgot.setVisibility(View.INVISIBLE);
        edittextForEmail = findViewById(R.id.edittextForEmail);
        patternmessage = findViewById(R.id.patternmessage);
        forgot = findViewById(R.id.forgot);
        setupClick = findViewById(R.id.setupClick);
        backArrow = findViewById(R.id.backArrow);
        backArrow.setVisibility(View.INVISIBLE);
        passwordmessage = findViewById(R.id.passwordmessage);
        passwordmessage.setVisibility(View.INVISIBLE);
        mPatternLockView = findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(this);
        passwordText = findViewById(R.id.passwordText);
        linearPattern = findViewById(R.id.linearPattern);
        linearOfPassword = findViewById(R.id.linearOfPassword);
        click = findViewById(R.id.click);


        if(dbHandlerForLock.getAllContacts().size() != 0) {
            final String lockType = dbHandlerForLock.getAllContacts().get(0).getDate()+"";
            //pattern lock
            if (lockType.equalsIgnoreCase("1")) {
                linearOfPassword.setVisibility(View.INVISIBLE);
                linearPattern.setVisibility(View.VISIBLE);
                passwordmessage.setVisibility(View.INVISIBLE);
            } else {
                linearOfPassword.setVisibility(View.VISIBLE);
                linearPattern.setVisibility(View.INVISIBLE);
                passwordmessage.setVisibility(View.INVISIBLE);
                //password lock

            }


            click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideSoftKeyboard(view);
                    String str = passwordText.getText().toString();
                    if (str.length() == 0) {
                        Animation shake = AnimationUtils.loadAnimation(LockCheck.this, R.anim.shake);
                        passwordText.startAnimation(shake);
                        passwordmessage.setVisibility(View.VISIBLE);
                        passwordmessage.setText("Please enter the password");
                        message("Please enter the password");
                    } else {
                        if (str.equalsIgnoreCase(dbHandlerForLock.getAllContacts().get(0).getWordName())) {
                            dbHandlerForSession.droptable();
                            dbHandlerForSession.addContact(new ContactsSession("add"));
                            Intent intent = new Intent(LockCheck.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Animation shake = AnimationUtils.loadAnimation(LockCheck.this, R.anim.shake);
                            passwordText.startAnimation(shake);
                            passwordmessage.setVisibility(View.VISIBLE);
                            passwordmessage.setText("Wrong password entered..");
                            message("Wrong password entered..");
                        }
                    }

                }
            });
            forgotPattern.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideSoftKeyboard(view);
                    backArrow.setVisibility(View.VISIBLE);
                    messageForget.setVisibility(View.INVISIBLE);
                    linearForgot.setVisibility(View.VISIBLE);
                    linearOfPassword.setVisibility(View.INVISIBLE);
                    linearPattern.setVisibility(View.INVISIBLE);
                }
            });
            setupClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pass11 = edittextForSetup1.getText().toString();
                    String pass12 = edittextForSetup2.getText().toString();

                    if(pass11.length() > 0 && pass12.length() > 0){
                        if(pass11.equals(pass12)){
                            hideSoftKeyboard(view);
                            message("Password successfully set.");
                            String email = dbHandlerForLock.getAllContacts().get(0).getMean();
                            dbHandlerForLock.droptable();
                            dbHandlerForLock.addContact(new ContactsForLock(pass11,email,"0"));
                            Intent ii = new Intent(LockCheck.this, MainActivity.class);
                            startActivity(ii);
                        }
                        else{
                            setupText.setVisibility(View.VISIBLE);
                            setupText.setText("Password do not match");
                            message("password do not match");
                        }
                    }
                    else{
                        setupText.setVisibility(View.VISIBLE);
                        setupText.setText("Please enter both the fields");
                        message("Please enter both the fields");
                    }
                }
            });
            forgotClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String emailTextForgot = edittextForEmail.getText().toString();
                    if (emailTextForgot.length() > 0) {
                        if (emailTextForgot.equalsIgnoreCase(dbHandlerForLock.getAllContacts().get(0).getMean())) {

                            linearPassSetup.setVisibility(View.VISIBLE);
                            linearForgot.setVisibility(View.INVISIBLE);
                            linearOfPassword.setVisibility(View.INVISIBLE);
                            setupText.setVisibility(View.INVISIBLE);

                        } else {
                            Animation shake = AnimationUtils.loadAnimation(LockCheck.this, R.anim.shake);
                            edittextForEmail.startAnimation(shake);
                            messageForget.setVisibility(View.VISIBLE);
                            messageForget.setText("Incorrect Email address");
                        }
                    } else {
                        Animation shake = AnimationUtils.loadAnimation(LockCheck.this, R.anim.shake);
                        edittextForEmail.startAnimation(shake);
                        messageForget.setVisibility(View.VISIBLE);
                        messageForget.setText("Please enter an email address");
                    }
                }
            });
            edittextForEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            forgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    backArrow.setVisibility(View.VISIBLE);
                    messageForget.setVisibility(View.INVISIBLE);
                    linearForgot.setVisibility(View.VISIBLE);
                    linearOfPassword.setVisibility(View.INVISIBLE);
                    linearPattern.setVisibility(View.INVISIBLE);
                }
            });
            backArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    backArrow.setVisibility(View.INVISIBLE);
                    linearForgot.setVisibility(View.INVISIBLE);
                    linearPassSetup.setVisibility(View.INVISIBLE);

                    if (lockType.equalsIgnoreCase("1")) {
                        linearOfPassword.setVisibility(View.INVISIBLE);
                        linearPattern.setVisibility(View.VISIBLE);
                        passwordmessage.setVisibility(View.INVISIBLE);
                    } else {
                        linearOfPassword.setVisibility(View.VISIBLE);
                        linearPattern.setVisibility(View.INVISIBLE);
                        passwordmessage.setVisibility(View.INVISIBLE);
                        //password lock

                    }

                }
            });

        }else{
            Intent intent = new Intent(LockCheck.this,StarterActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        String result = PatternLockUtils.patternToString(mPatternLockView,pattern);

        if(dbHandlerForLock.getAllContacts().get(0).getWordName().equalsIgnoreCase(result)){
            dbHandlerForSession.droptable();
            dbHandlerForSession.addContact(new ContactsSession("add"));

            Intent intent = new Intent(LockCheck.this,MainActivity.class);
            startActivity(intent);

        }
        else{
            mPatternLockView.clearPattern();
            patternmessage.setText("Incorrect pattern. Try again.");
        }
    }

    @Override
    public void onCleared() {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbHandlerForSession.droptable();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}