package com.notepadOne.notepaddone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

/*
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

 */
import com.google.android.material.snackbar.Snackbar;
import com.notepadOne.notepaddone.Database.DBHandlerForLock;
import com.notepadOne.notepaddone.Database.DBHandlerForSession;

import java.util.ArrayList;
import java.util.List;

public class LockChange extends AppCompatActivity implements PatternLockViewListener {

    LinearLayout linearPasswordInitial, linearPatternInitial;
    final DBHandlerForLock dbHandlerForLock = new DBHandlerForLock(this);
    final DBHandlerForSession dbHandlerForSession = new DBHandlerForSession(this);
    PatternLockView mPatternLockView,pattern_lock_view_final;
    EditText passwordText;
    TextView errorMessages,errorMessagesForPattern,errorMessagesForPatternFinal,errorMessagesFinal;
    Button click,reset;
    RelativeLayout relative;
    ImageView backArrow;
    List<String> firstAttemp = new ArrayList<>();
    //final var
    //AdView adView;
    LinearLayout linearPatternFinal,linearPasswordFinal,linearDecision;
    Button clickFinal,patternOption,passwordOption;
    EditText passwordTextFinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_change);
/*
        MobileAds.initialize(this,"ca-app-pub-8206113478901010~2151283403");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);



 */




        linearPatternInitial = findViewById(R.id.linearPatternInitial);
        linearPasswordInitial =findViewById(R.id.linearPasswordInitial);
        linearPasswordFinal = findViewById(R.id.linearPasswordFinal);
        linearPatternFinal = findViewById(R.id.linearPatternFinal);
        linearDecision = findViewById(R.id.linearDecision);

        backArrow = findViewById(R.id.backArrow);
        patternOption = findViewById(R.id.patternOption);
        passwordOption = findViewById(R.id.passwordOption);
        passwordTextFinal = findViewById(R.id.passwordTextFinal);
        clickFinal = findViewById(R.id.clickFinal);
        linearDecision.setVisibility(View.INVISIBLE);
        errorMessagesFinal = findViewById(R.id.errorMessagesFinal);
        errorMessagesForPatternFinal = findViewById(R.id.errorMessagesForPatternFinal);
        pattern_lock_view_final = findViewById(R.id.pattern_lock_view_final);
        pattern_lock_view_final.addPatternLockListener(this);
        errorMessagesForPattern = findViewById(R.id.errorMessagesForPattern);
        errorMessagesForPattern.setVisibility(View.INVISIBLE);
        errorMessages = findViewById(R.id.errorMessages);
        mPatternLockView = findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(this);
        passwordText = findViewById(R.id.passwordText);
        click = findViewById(R.id.click);
        relative = findViewById(R.id.relative);
        reset = findViewById(R.id.reset);
        //visibility
        linearPasswordInitial.setVisibility(View.INVISIBLE);
        linearPatternInitial.setVisibility(View.INVISIBLE);

        if(dbHandlerForLock.getAllContacts().get(0).getDate().equalsIgnoreCase("1")){
            linearPatternInitial.setVisibility(View.VISIBLE);
            linearPasswordInitial.setVisibility(View.INVISIBLE);
        }
        else{
            linearPatternInitial.setVisibility(View.INVISIBLE);
            linearPasswordInitial.setVisibility(View.VISIBLE);

        }
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstAttemp.clear();
                hideSoftKeyboard(relative);
                reset.setVisibility(View.INVISIBLE);
                pattern_lock_view_final.clearPattern();
                errorMessagesForPatternFinal.setText("Enter pattern again:");
            }
        });
        passwordOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                linearPasswordFinal.setVisibility(View.VISIBLE);
                linearDecision.setVisibility(View.INVISIBLE);
                errorMessagesFinal.setVisibility(View.INVISIBLE);
                backArrow.setVisibility(View.INVISIBLE);
            }
        });
        patternOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                errorMessagesForPatternFinal.setVisibility(View.INVISIBLE);
                linearPatternFinal.setVisibility(View.VISIBLE);
                reset.setVisibility(View.INVISIBLE);
                backArrow.setVisibility(View.VISIBLE);
                linearDecision.setVisibility(View.INVISIBLE);
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(view);
                linearDecision.setVisibility(View.VISIBLE);
                linearPatternFinal.setVisibility(View.INVISIBLE);
                backArrow.setVisibility(View.INVISIBLE);
                linearPasswordFinal.setVisibility(View.INVISIBLE);
            }
        });
        clickFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass1 = passwordTextFinal.getText().toString();
                hideSoftKeyboard(view);
                if(pass1.length() > 0){
                        message("Password set successfully");
                        String email = dbHandlerForLock.getAllContacts().get(0).getMean();
                        dbHandlerForLock.droptable();
                        dbHandlerForLock.addContact(new ContactsForLock(pass1,email,"0"));
                        Intent i = new Intent(LockChange.this, MainActivity.class);
                        startActivity(i);
                }
                else{
                    Animation shake = AnimationUtils.loadAnimation(LockChange.this, R.anim.shake);
                    passwordTextFinal.startAnimation(shake);
                    errorMessagesFinal.setText("Enter correct password");
                }
            }
        });
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(view);
                String pass = passwordText.getText().toString();
                if(pass.length() > 0){
                    if(pass.equalsIgnoreCase(dbHandlerForLock.getAllContacts().get(0).getWordName())){

                        linearPasswordInitial.setVisibility(View.INVISIBLE);
                        linearDecision.setVisibility(View.VISIBLE);
                        //linearPasswordFinal.setVisibility(View.VISIBLE);
                    }
                    else{
                        Animation shake = AnimationUtils.loadAnimation(LockChange.this, R.anim.shake);
                        passwordText.startAnimation(shake);
                        errorMessages.setText("Please enter the correct password");
                    }
                }
                else{
                    Animation shake = AnimationUtils.loadAnimation(LockChange.this, R.anim.shake);
                    passwordText.startAnimation(shake);
                    errorMessages.setText("Please enter the correct password");
                }
            }
        });

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {

        //for providing the user with choice to add pattern or password

        if(linearPatternFinal.getVisibility() == View.INVISIBLE) {
            String result = PatternLockUtils.patternToString(mPatternLockView, pattern);

            if (dbHandlerForLock.getAllContacts().get(0).getWordName().equalsIgnoreCase(result)) {
                mPatternLockView.setVisibility(View.INVISIBLE);

                //linearPatternFinal.setVisibility(View.VISIBLE);
                linearDecision.setVisibility(View.VISIBLE);

            } else {
                errorMessagesForPattern.setVisibility(View.VISIBLE);
                errorMessagesForPattern.setText("Incorrect password. Please try again.");
            }

        }
        else{
            String result = PatternLockUtils.patternToString(pattern_lock_view_final,pattern);


            if(firstAttemp.size() ==0 ){

                firstAttemp.add(result);
                errorMessagesForPatternFinal.setVisibility(View.VISIBLE);
                errorMessagesForPatternFinal.setText("Re-enter the pattern..");
                pattern_lock_view_final.clearPattern();
                reset.setVisibility(View.VISIBLE);
            }
            else{

                if(firstAttemp.get(0).equalsIgnoreCase(result)) {
                    String email = dbHandlerForLock.getAllContacts().get(0).getMean();
                    dbHandlerForLock.droptable();
                    dbHandlerForLock.addContact(new ContactsForLock(result, email, "1"));
                    mPatternLockView.clearPattern();
                    Intent i = new Intent(LockChange.this, MainActivity.class);
                    startActivity(i);
                }
                else{
                    errorMessagesForPatternFinal.setText("Pattern does not match, try again");
                }
            }
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


}