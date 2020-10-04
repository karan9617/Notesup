package com.notepadOne.notepaddone;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
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
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.google.android.material.snackbar.Snackbar;
import com.notepadOne.notepaddone.Database.DBHandlerForLock;
import com.notepadOne.notepaddone.Database.DBHandlerForSession;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class StarterActivity extends AppCompatActivity implements PatternLockViewListener {

    LinearLayout linear,linearOfPassword,linearPattern,linearEmail;
    Button password, pattern,click,reset,submitEmail;
    PatternLockView mPatternLockView;
    RelativeLayout relative;
    ImageView backArrow;
    EditText passwordText, emailtext,emailtextInEmail,emailtextInEmail2;
    TextView caution,patternmessage,intro,heading,errorMessages,or,emailMessage;

    List<String> resultList = new ArrayList<>();
    final DBHandlerForLock dbHandlerForLock = new DBHandlerForLock(this);
    final DBHandlerForSession dbHandlerForSession = new DBHandlerForSession(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        linear=  findViewById(R.id.linear);
        or = findViewById(R.id.or);

        submitEmail = findViewById(R.id.submitEmail);

        errorMessages = findViewById(R.id.errorMessages);
        emailtextInEmail = findViewById(R.id.emailtextInEmail);
        emailMessage = findViewById(R.id.emailMessage);
        emailtextInEmail2 = findViewById(R.id.emailtextInEmail2);
        backArrow = findViewById(R.id.backArrow);
        errorMessages.setVisibility(View.INVISIBLE);
        linearEmail = findViewById(R.id.linearEmail);
        backArrow.setVisibility(View.INVISIBLE);
        relative = findViewById(R.id.relative);
        password = findViewById(R.id.password);
        reset = findViewById(R.id.reset);
        reset.setVisibility(View.INVISIBLE);
        linearEmail.setVisibility(View.INVISIBLE);
        linearOfPassword = findViewById(R.id.linearOfPassword);
        pattern = findViewById(R.id.pattern);
        linearPattern = findViewById(R.id.linearPattern);
        linearPattern.setVisibility(View.INVISIBLE);
        heading = findViewById(R.id.heading);
        mPatternLockView = findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(this);
        emailtext = findViewById(R.id.emailtext);
        passwordText = findViewById(R.id.passwordText);
        caution = findViewById(R.id.caution);
        click = findViewById(R.id.click);
        intro = findViewById(R.id.intro);
        linearOfPassword.setVisibility(View.INVISIBLE);
        patternmessage = findViewById(R.id.patternmessage);


        linear.setVisibility(View.INVISIBLE);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultList.clear();
                reset.setVisibility(View.INVISIBLE);


            }
        });
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String passwordMessage = passwordText.getText().toString().trim();
                String emailText = emailtext.getText().toString().trim();

                if(passwordMessage.length() > 4){
                   if(emailText.length() > 0){
                       if(emailText.contains("@")){
                           dbHandlerForLock.droptable();
                           dbHandlerForLock.addContact(new ContactsForLock(passwordMessage,emailText,"0"));
                           Intent intent = new Intent(StarterActivity.this, MainActivity.class);
                           startActivity(intent);
                       }
                       else{
                           errorMessages.setVisibility(View.VISIBLE);
                           errorMessages.setText("Enter valid email address.");
                           message("Enter valid email address.");
                       }


                   }
                   else{
                       errorMessages.setVisibility(View.VISIBLE);
                       errorMessages.setText("Please enter a valid email ID for password recovery.");
                       message("Please enter a valid email ID for password recovery.");
                   }
                }
                else{
                    errorMessages.setVisibility(View.VISIBLE);
                    errorMessages.setText("Please enter a password of length greater than 4");
                    message("Please enter a password of length greater than 4");
                }
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(view);
                linear.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.INVISIBLE);
                linearPattern.setVisibility(View.INVISIBLE);
                linearEmail.setVisibility(View.INVISIBLE);
                linearOfPassword.setVisibility(View.INVISIBLE);
            }
        });
        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearPattern.setVisibility(View.INVISIBLE);
                linearOfPassword.setVisibility(View.VISIBLE);
                errorMessages.setVisibility(View.INVISIBLE);
                linear.setVisibility(View.INVISIBLE);
                patternmessage.setVisibility(View.INVISIBLE);
                backArrow.setVisibility(View.VISIBLE);
            }
        });
        pattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear.setVisibility(View.INVISIBLE);
                errorMessages.setVisibility(View.INVISIBLE);
                linearOfPassword.setVisibility(View.INVISIBLE);

                patternmessage.setText("Enter the pattern for password.");
                reset.setVisibility(View.INVISIBLE);

                linearPattern.setVisibility(View.VISIBLE);


                backArrow.setVisibility(View.VISIBLE);

            }
        });

        final Handler handler2 = new Handler(Looper.getMainLooper());
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {

                ObjectAnimator fadeOut23 = ObjectAnimator.ofFloat(heading, "alpha", 1f, 0f);
                fadeOut23.setDuration(1000);
                fadeOut23.start();
            }
        }, 2400);

        submitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(view);
                String emailFirst = emailtextInEmail.getText().toString().trim();
                String emailSecond = emailtextInEmail2.getText().toString().trim();

                if(emailFirst.length() > 0 && emailSecond.length() > 0){
                    if(emailFirst.equalsIgnoreCase(emailSecond)){
                        dbHandlerForLock.droptable();
                        dbHandlerForLock.addContact(new ContactsForLock(resultList.get(0),emailFirst,"1"));
                        dbHandlerForSession.addContact(new ContactsSession("add"));
                        Intent i = new Intent(StarterActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                    else{
                        emailMessage.setVisibility(View.VISIBLE);
                        emailMessage.setText("The email do not match");
                        Animation shake = AnimationUtils.loadAnimation(StarterActivity.this, R.anim.shake);
                        emailtextInEmail2.startAnimation(shake);
                        message("The email do not match");
                    }
                }
                else{
                    emailMessage.setVisibility(View.VISIBLE);
                    emailMessage.setText("Please fill all the email input");
                    Animation shake = AnimationUtils.loadAnimation(StarterActivity.this, R.anim.shake);
                    emailtextInEmail.startAnimation(shake);
                    message("Please fill all the email input");
                }
            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                linear.setVisibility(View.VISIBLE);


                ObjectAnimator fadeIn1 = ObjectAnimator.ofFloat(password, "alpha", 0f, 1f);
                fadeIn1.setDuration(1300);
                fadeIn1.start();

                ObjectAnimator fadeIn3 = ObjectAnimator.ofFloat(pattern, "alpha", 0f, 1f);
                fadeIn3.setDuration(1300);
                fadeIn3.start();

                ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(intro, "alpha", 0f, 1f);
                fadeIn2.setDuration(1300);
                fadeIn2.start();


                ObjectAnimator fadeIn552 = ObjectAnimator.ofFloat(click, "alpha", 0f, 1f);
                fadeIn552.setDuration(1300);
                fadeIn552.start();

                ObjectAnimator fadeIn5523= ObjectAnimator.ofFloat(or, "alpha", 0f, 1f);
                fadeIn5523.setDuration(1300);
                fadeIn5523.start();
            }
        }, 3500);
    }

    @Override
    public void onStarted() {
        showSoftKeyboard(mPatternLockView);
    }

    @Override
    public void onProgress(List<PatternLockView.Dot> progressPattern) {

    }

    @Override
    public void onComplete(List<PatternLockView.Dot> pattern) {
        String result = PatternLockUtils.patternToString(mPatternLockView,pattern).trim();

        if(resultList.size() == 0){

                    patternmessage.setVisibility(View.VISIBLE);
                    mPatternLockView.clearPattern();
                    patternmessage.setText("Enter pattern again");
                    reset.setVisibility(View.VISIBLE);
                    linearEmail.setVisibility(View.INVISIBLE);
                    resultList.add(result);
              //      dbHandlerForLock.addContact(new ContactsForLock(result, emailtextInPattern.getText().toString(),"1"));

        }
        else{
            if(resultList.get(0).equalsIgnoreCase(result))
            {

                message("Pattern saved successfully..");
                linearEmail.setVisibility(View.VISIBLE);
                linearOfPassword.setVisibility(View.INVISIBLE);
                emailMessage.setVisibility(View.INVISIBLE);
                linearPattern.setVisibility(View.INVISIBLE);

             /*   Intent intent = new Intent(StarterActivity.this, MainActivity.class);
                startActivity(intent);
                */

            }
            else{
                message("Password do not match. Please try again..");
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbHandlerForLock.droptable();

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
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}