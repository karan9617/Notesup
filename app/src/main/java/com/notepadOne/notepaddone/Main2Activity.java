package com.notepadOne.notepaddone;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import androidx.annotation.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;


import com.notepadOne.notepaddone.howtouse.Main5Activity;
import com.notepadOne.notepaddone.trash.Main3Activity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


// INSIDE ACTIVITY WHERE NOTES ARE WRITTEN WITH SPEECH RECOGNIZER
public class Main2Activity extends AppCompatActivity implements RecognitionListener {

    EditText note,title;
    String tv1;
    final DBHandler21  dbHandler21  = new DBHandler21 (this);      //database for main list
    public String text;
    ScrollView scrollview;
    RelativeLayout innerlayout;
    Button bluebutton,blackbutton,eraserButton,redbutton;
    ImageButton cancelbutton;
    LinearLayout imageLinearLayout;
    SaveSharedPreference s;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    boolean bold = false;
    PaintView paintView;
    int screenWidth = 500;
    int height =0;
    ImageView image;
    boolean donedrawing = false;
    Bitmap bitmap;

    final DBHandler  dbHandler  = new DBHandler (this);

    public void showSoftKeyboard(View view){
        if(view.requestFocus()){
            InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imageLinearLayout = findViewById(R.id.imageLinearLayout);
        note        = findViewById(R.id.note);
        title       = findViewById(R.id.title);
        note.setFocusable(false);
        title.setFocusable(false);
        Intent in   = getIntent();
        //innerlayout = findViewById(R.id.innerlayout);
        image = findViewById(R.id.image);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        scrollview       = findViewById(R.id.scrollview);

        screenWidth = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        paintView = (PaintView) findViewById(R.id.paintView);
        paintView.init(displayMetrics);
        paintView.setVisibility(View.INVISIBLE);

        cancelbutton = findViewById(R.id.cancelbutton);
        cancelbutton.setVisibility(View.INVISIBLE);

        bluebutton = findViewById(R.id.bluebutton);
        blackbutton = findViewById(R.id.blackbutton);
        eraserButton = findViewById(R.id.eraserButton);
        redbutton = findViewById(R.id.redbutton);

        blackbutton.setVisibility(View.INVISIBLE);
        bluebutton.setVisibility(View.INVISIBLE);
        redbutton.setVisibility(View.INVISIBLE);
        eraserButton.setVisibility(View.INVISIBLE);

        // voice
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener((RecognitionListener) this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);
        //
        tv1 = in.getExtras().getString("title");
       /* innerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.requestFocus();

                note.setFocusableInTouchMode(true);
                title.setFocusableInTouchMode(true);

                note.setCursorVisible(true);
                note.setSelection(note.getText().length());
                showSoftKeyboard(note);
            }
        });*/
        if(tv1.matches("0")){                   /* CHECK IF THE NOTE IS CREATED FOR THE FIRST TIME */
            note.setHint("Enter note here..");
            title.setHint("Enter a unique title..");
        }
        else {
            title.setText(tv1);
            note.setText(dbHandler.getNoteFromTitle(tv1));
            title.setCursorVisible(false);
            note.setCursorVisible(false);

            hideKeyboard(Main2Activity.this);
            if(dbHandler21.getNoteImageinBytesFromTitle(tv1) != null)
            {
                byte[] byteArray = dbHandler21.getNoteImageinBytesFromTitle(tv1);


                Bitmap bitmap2 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
               Bitmap bitmap8 = bitmap2.copy(Bitmap.Config.ARGB_8888, true);
                cancelbutton.setVisibility(View.VISIBLE);
                image.setVisibility(View.VISIBLE);
                image.setImageBitmap(Bitmap.createScaledBitmap(bitmap8, screenWidth,paintView.height, false));
            }
            else{
                paintView.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
                cancelbutton.setVisibility(View.INVISIBLE);
            }
        }
        imageLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setFocusableInTouchMode(true);
            }
        });
        if(image.getVisibility() == View.INVISIBLE && paintView.getVisibility() == View.INVISIBLE){
            note.setFocusableInTouchMode(true);
            showSoftKeyboard(note);
        }
        scrollview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setFocusableInTouchMode(true);
            }
        });
       note.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               note.setCursorVisible(true);
               note.setFocusableInTouchMode(true);
               showSoftKeyboard(note);
               paintView.setVisibility(View.INVISIBLE);
                if(donedrawing) {
                    cancelbutton.setVisibility(View.VISIBLE);

                    bluebutton.setVisibility(View.INVISIBLE);
                    redbutton.setVisibility(View.INVISIBLE);
                    blackbutton.setVisibility(View.INVISIBLE);
                    eraserButton.setVisibility(View.INVISIBLE);
                    paintView.setVisibility(View.INVISIBLE);

                    image.setVisibility(View.VISIBLE);
                    bitmap = Bitmap.createBitmap(screenWidth, paintView.height, Bitmap.Config.ARGB_8888);

                    Canvas canvas = new Canvas(bitmap);
                    paintView.draw(canvas);

                            image.setImageBitmap(bitmap);

                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                            int width = displayMetrics.widthPixels;
                            image.setMaxWidth(width);

                }
           }
       });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.setCursorVisible(true);
                title.setFocusableInTouchMode(true);
                showSoftKeyboard(title);
            }
        });
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cancelbutton.getVisibility() == View.VISIBLE){
                    if(tv1 != null) {
                        cancelImageDialog(tv1, Main2Activity.this).show();
                    }
                   else{
                        Toast.makeText(getApplicationContext(),"Please enter the previous title of this note.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluebutton.setVisibility(View.VISIBLE);
                eraserButton.setVisibility(View.VISIBLE);
                blackbutton.setVisibility(View.VISIBLE);
                redbutton.setVisibility(View.VISIBLE);

               /* byte[] byteArray = dbHandler21.getNoteImageinBytesFromTitle(title.getText().toString());
                Bitmap bitmap2 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                Bitmap bitmap8 = bitmap2.copy(Bitmap.Config.ARGB_8888, true);

                Canvas canvas = new Canvas(bitmap8);
                paintView.mCanvas = canvas;

                paintView.init(displayMetrics);
                paintView.draw(canvas);*/
                image.invalidate();
                BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

            //    Bitmap bitmap = Bitmap.createBitmap(screenWidth, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                paintView.draw(canvas);
                //image.setImageBitmap(bitmap);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                int width = displayMetrics.widthPixels;
                //image.setMaxWidth(width);




                image.setVisibility(View.INVISIBLE);
            /*    byte[] byteArray = dbHandler21.getNoteImageinBytesFromTitle(tv1);

                Bitmap bitmap2 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                Bitmap mutableBitmap = bitmap2.copy(Bitmap.Config.ARGB_8888, true);

                Canvas canvas = new Canvas(mutableBitmap);
                paintView.draw(canvas);
                */

                //image.setImageBitmap(mutableBitmap);

                paintView.setVisibility(View.VISIBLE);

                donedrawing = true;
                hideKeyboard(Main2Activity.this);
            }
        });
        bluebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {paintView.currentColor = Color.parseColor("#0000FF");
            }
        });
        blackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { paintView.currentColor = Color.parseColor("#000000");
            }
        });
        eraserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { paintView.currentColor = Color.parseColor("#ffffff");
            }
        });
        redbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paintView.currentColor = Color.parseColor("#DC143C");
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.howtouse) { Intent intent = new Intent(getApplicationContext(), Main5Activity.class);startActivity(intent);return true; }
        if(id == R.id.trash){ Intent intent = new Intent(getApplicationContext(), Main3Activity.class);startActivity(intent);return true; }
        //SAVE
        if (id == R.id.save) {

            //IF NOTE AND TITLE IS EMPTY OR NOT
            if(note.getText().toString().matches("")|| title.getText().toString().matches(""))
            {
                Toast.makeText(getApplicationContext(),"Enter a unique title and a note.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                s = new SaveSharedPreference();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

                /* GETTING DATE AND CALENDER */
                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);

                if (SaveSharedPreference.getIntegerCount(Main2Activity.this) == 0)
                {

                    s.setIntegerCount(getApplicationContext(),0);

                    dbHandler.addContact(new Contacts(s.getIntegerCount(Main2Activity.this), "" + title.getText().toString(), "" + note.getText().toString(),""+formattedDate));

    //CHRCKING SOMEHTING WAS DRAWN OF NOT

                    if(paintView.mX != 0) {
                        if (dbHandler21.ifTitleExists(title.getText().toString()) == 0) {
                            Bitmap bitmap = Bitmap.createBitmap(screenWidth, paintView.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            paintView.draw(canvas);
                            image.setImageBitmap(bitmap);

                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                            int width = displayMetrics.widthPixels;
                            image.setMaxWidth(width);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            byte[] img = bos.toByteArray();
                            dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, title.getText().toString(), img, ""));
                            SaveSharedPreference.setIntegerCount(Main2Activity.this,s.getIntegerCount(Main2Activity.this)+1);
                        }
                        else{
                            Bitmap bitmap = Bitmap.createBitmap(screenWidth, paintView.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            paintView.draw(canvas);
                            image.setImageBitmap(bitmap);

                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                            int width = displayMetrics.widthPixels;
                            image.setMaxWidth(width);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            byte[] img = bos.toByteArray();

                            dbHandler21.deleteItemFromListByTitle(title.getText().toString());
                            dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, title.getText().toString(), img, ""));
                            SaveSharedPreference.setIntegerCount(Main2Activity.this,s.getIntegerCount(Main2Activity.this)+1);
                            //Toast.makeText(getApplicationContext(), "somethin got painted2", Toast.LENGTH_SHORT).show();

                        }
                    }
                    // NOTHIN WAS DRAWN OR RE-UPDATED
                    else{
                        //IF A IMAGE ALREADY EXISTS
                        if(image.getVisibility() == View.VISIBLE){
                            image.setDrawingCacheEnabled(true);
                            Bitmap bitmap = image.getDrawingCache();
                            Canvas canvas = new Canvas(bitmap);
                            paintView.draw(canvas);
                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            byte[] img = bos.toByteArray();

                            dbHandler21.deleteItemFromListByTitle(title.getText().toString());
                            dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, title.getText().toString(), img, ""));
                            SaveSharedPreference.setIntegerCount(Main2Activity.this,s.getIntegerCount(Main2Activity.this)+1);

                        }
                        //dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this)+1,title.getText().toString(),null,""));
                    }

                   //SETTING INTERGER COUNT OF SHAREDPREFERENCE EQUAL TO 1
                    s.setIntegerCount(getApplicationContext(),s.getIntegerCount(Main2Activity.this)+1);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                else
                {
                    int titleExists = dbHandler.ifTitleExists(title.getText().toString());
                    /* CHECK TITLE EXISTS*/

                    if (titleExists == 1)
                    {
                        if(tv1.matches("0")){
                            Toast.makeText(getApplicationContext(),"Please enter a unique title.. title already exists", Toast.LENGTH_SHORT).show();
                        }else
                            {
                            if(paintView.mX != 0) {
                                if (dbHandler21.ifTitleExists(tv1) == 0)
                                {

                                    Bitmap bitmap = Bitmap.createBitmap(screenWidth, paintView.getHeight(), Bitmap.Config.ARGB_8888);
                                    Canvas canvas = new Canvas(bitmap);
                                    paintView.draw(canvas);
                                    image.setImageBitmap(bitmap);

                                    DisplayMetrics displayMetrics = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                                    int width = displayMetrics.widthPixels;
                                    image.setMaxWidth(width);

                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                    byte[] img = bos.toByteArray();
                                    dbHandler21.deleteItemFromListByTitle(title.getText().toString());
                                    SaveSharedPreference.setIntegerCount(Main2Activity.this,s.getIntegerCount(Main2Activity.this)+1);
                                    dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) +1, title.getText().toString(), img, ""));
                                   // Toast.makeText(getApplicationContext(), "somethin got painte3", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Bitmap bitmap = Bitmap.createBitmap(screenWidth, paintView.getHeight(), Bitmap.Config.ARGB_8888);
                                    Canvas canvas = new Canvas(bitmap);
                                    paintView.draw(canvas);

                                    image.setImageBitmap(bitmap);

                                    DisplayMetrics displayMetrics = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                                    int width = displayMetrics.widthPixels;
                                    image.setMaxWidth(width);



                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                    byte[] img = bos.toByteArray();

                                    dbHandler21.deleteItemFromListByTitle(title.getText().toString());
                                    dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 2, title.getText().toString(), img, ""));
                                    SaveSharedPreference.setIntegerCount(Main2Activity.this,s.getIntegerCount(Main2Activity.this)+1);
                                   // Toast.makeText(getApplicationContext(), "somethin got painted4", Toast.LENGTH_SHORT).show();

                                }
                            }
                            else{
                                if (image.getVisibility() == View.VISIBLE) {

                                    image.setDrawingCacheEnabled(true);
                                    Bitmap bitmap = image.getDrawingCache();
                                    byte[] b = dbHandler21.getNoteImageinBytesFromTitle(title.getText().toString());

                                    Canvas canvas = new Canvas(bitmap);
                                    paintView.draw(canvas);
                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                    byte[] img = bos.toByteArray();

                                    dbHandler21.deleteItemFromListByTitle(title.getText().toString());
                                    dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, title.getText().toString(), b, ""));
                                    SaveSharedPreference.setIntegerCount(Main2Activity.this,s.getIntegerCount(Main2Activity.this)+1);
                                } //  dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this)+1,title.getText().toString(),null,""));
                             else{
                                        if(dbHandler21.ifTitleExists(title.getText().toString()) == 1){
                                            dbHandler21.deleteItemFromListByTitle(title.getText().toString());
                                            dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 2, title.getText().toString(), null, ""));

                                        }
                                        else {
                                            dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 2, title.getText().toString(), null, ""));
                                        }
                                    }
                                }
                            dbHandler.updateExistingTitle(title.getText().toString(), note.getText().toString());

                            Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(mainActivityIntent);
                            finish();
                        }
                    } else
                        {

                        dbHandler.addContact(new Contacts(s.getIntegerCount(Main2Activity.this) + 1, ""
                                + title.getText().toString(), "" + note.getText().toString(),""+formattedDate));
                        if(paintView.mX != 0)
                        {

                            //DRAW WITH NO TITLE IS THERE IN THE DATABASE

                            if (dbHandler21.ifTitleExists(title.getText().toString()) == 0)
                            {

                                Bitmap bitmap = Bitmap.createBitmap(screenWidth, paintView.getHeight(), Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(bitmap);
                                paintView.draw(canvas);
                                image.setImageBitmap(bitmap);

                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                                int width = displayMetrics.widthPixels;
                                image.setMaxWidth(width);

                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                byte[] img = bos.toByteArray();
                                dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 2, title.getText().toString(), img, ""));
                                SaveSharedPreference.setIntegerCount(Main2Activity.this,s.getIntegerCount(Main2Activity.this)+1);

                              //  Toast.makeText(getApplicationContext(), "somethin got painte5", Toast.LENGTH_SHORT).show();
                            }


                            /* DRAW THIS IF there is some title that is there in DATABASE*/
                            else{
                                Bitmap bitmap = Bitmap.createBitmap(screenWidth, paintView.getHeight(), Bitmap.Config.ARGB_8888);
                                Canvas canvas = new Canvas(bitmap);
                                paintView.draw(canvas);
                                image.setImageBitmap(bitmap);

                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                                int width = displayMetrics.widthPixels;
                                image.setMaxWidth(width);

                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                byte[] img = bos.toByteArray();
                                dbHandler21.deleteItemFromListByTitle(title.getText().toString());
                                dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, title.getText().toString(), img, ""));
                                SaveSharedPreference.setIntegerCount(Main2Activity.this,s.getIntegerCount(Main2Activity.this)+1);
                              // Toast.makeText(getApplicationContext(), "somethin got painted6", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else {
                            if (image.getVisibility() == View.VISIBLE)
                            {
                                image.setDrawingCacheEnabled(true);

                                byte[] b = dbHandler21.getNoteImageinBytesFromTitle(tv1);

                                Bitmap bitmap = image.getDrawingCache();
                                Canvas canvas = new Canvas(bitmap);
                                paintView.draw(canvas);
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                byte[] img = bos.toByteArray();

                               // dbHandler21.deleteItemFromListByTitle(title.getText().toString());
                                dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, title.getText().toString(), b, ""));
                                SaveSharedPreference.setIntegerCount(Main2Activity.this,s.getIntegerCount(Main2Activity.this)+1);

                                // dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this)+1,title.getText().toString(),null,""));
                            }
                            else{
                                if(dbHandler21.ifTitleExists(title.getText().toString()) == 1){
                                    dbHandler21.deleteItemFromListByTitle(title.getText().toString());
                                    dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, title.getText().toString(), null, ""));
                                    SaveSharedPreference.setIntegerCount(Main2Activity.this,s.getIntegerCount(Main2Activity.this)+1);

                                }
                                else {
                                    dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, title.getText().toString(), null, ""));
                                }
                            }
                        }
                        s.setIntegerCount(getApplicationContext(), s.getIntegerCount(Main2Activity.this) + 1);
                        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(mainActivityIntent);

                        //for quitting the activity
                        finish();
                    }
                }
            }
            return true;
        }

        if(id == R.id.voice)
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(Main2Activity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        101);

            }
            speech.startListening(recognizerIntent);
            Toast.makeText(getApplicationContext(),"Speak", Toast.LENGTH_SHORT).show();
            return true;
        }
        if(id == R.id.share){
            Intent intent = new Intent(Intent.ACTION_SEND);
                if (note.getText().toString() != null && title.getText().toString() != null) {
                    intent.setType("text/plain");

                    String shareBody = note.getText().toString();
                    String shareTitle = title.getText().toString();

                    intent.putExtra(Intent.EXTRA_SUBJECT, shareTitle);
                    intent.putExtra(Intent.EXTRA_TEXT, shareBody);

                    startActivity(Intent.createChooser(intent, "Share using.."));
            }
        }
        if(id == R.id.brush){
            paintView.setVisibility(View.VISIBLE);
            image.setVisibility(View.INVISIBLE);

            bluebutton.setVisibility(View.VISIBLE);
            eraserButton.setVisibility(View.VISIBLE);
            blackbutton.setVisibility(View.VISIBLE);
            redbutton.setVisibility(View.VISIBLE);

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            paintView.init(metrics);
            hideKeyboard(Main2Activity.this);
            donedrawing = true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void onBackPressed() {
        String t = title.getText().toString();
        String n = note.getText().toString();

        //checking if back button was pressed without entering the title
            if(t.matches(""))
            {
                Toast.makeText(getApplicationContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            else {
                /* CHECKS IF NOTES ARE SAME OR NOT IF YES THEN EXIT THE ACTIVITY WITHOUT DOING NOTHING*/
                    if (dbHandler.getNoteFromTitle(t).matches(n)) {
                        if(image.getVisibility() == View.VISIBLE || paintView.getVisibility() == View.VISIBLE)
                        {
                            if(paintView.mX != 0){
                                if (dbHandler21.ifTitleExists(title.getText().toString()) == 0) {
                                    Bitmap bitmap = Bitmap.createBitmap(screenWidth, paintView.getHeight(), Bitmap.Config.ARGB_8888);
                                    Canvas canvas = new Canvas(bitmap);
                                    paintView.draw(canvas);
                                    image.setImageBitmap(bitmap);

                                    DisplayMetrics displayMetrics = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                                    int width = displayMetrics.widthPixels;
                                    image.setMaxWidth(width);

                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                    byte[] img = bos.toByteArray();
                                    dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, title.getText().toString(), img, ""));
                                    SaveSharedPreference.setIntegerCount(Main2Activity.this,s.getIntegerCount(Main2Activity.this)+1);
                                }
                                else{
                                    Bitmap bitmap = Bitmap.createBitmap(screenWidth, paintView.getHeight(), Bitmap.Config.ARGB_8888);
                                    Canvas canvas = new Canvas(bitmap);
                                    paintView.draw(canvas);
                                    image.setImageBitmap(bitmap);

                                    DisplayMetrics displayMetrics = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                                    int width = displayMetrics.widthPixels;
                                    image.setMaxWidth(width);

                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                    byte[] img = bos.toByteArray();

                                    dbHandler21.deleteItemFromListByTitle(title.getText().toString());
                                    dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, title.getText().toString(), img, ""));
                                    SaveSharedPreference.setIntegerCount(Main2Activity.this,s.getIntegerCount(Main2Activity.this)+1);
                                }
                            }else{

                            }
                        }else{

                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {

                        /* IF THE NOTES ARE NOT SAME */
                        buildDialog(Main2Activity.this).show();
                        Toast.makeText(getApplicationContext(), "new note", Toast.LENGTH_SHORT).show();
                    }
                }
    }

    //save or dicard  notes
    public AlertDialog.Builder buildDialog(Context c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Notes");
        builder.setMessage("Save your changes or discard them..?");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String titlename = title.getText().toString();
                String notename = note.getText().toString();

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);

                if (titlename.matches("") || notename.matches(""))
                {
                    Toast.makeText(getApplicationContext(), "Please enter some title and note..", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    /* IF TITLE EXISTS  THEN UPDATE THE NOTE ONLY*/
                    if (dbHandler.ifTitleExists(titlename) == 1)
                    {
                        dbHandler.updateExistingTitle(titlename, notename);

                       if(image.getVisibility() == View.VISIBLE || paintView.strokeWidth != 0){
                           Bitmap bitmap = Bitmap.createBitmap(screenWidth, height, Bitmap.Config.ARGB_8888);
                           Canvas canvas = new Canvas(bitmap);
                           paintView.draw(canvas);
                           image.setImageBitmap(bitmap);

                           DisplayMetrics displayMetrics = new DisplayMetrics();
                           getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                           int width = displayMetrics.widthPixels;
                           image.setMaxWidth(width);

                           ByteArrayOutputStream bos = new ByteArrayOutputStream();
                           bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                           byte[] img = bos.toByteArray();
                            if(dbHandler21.ifTitleExists(titlename) == 1){

                                dbHandler21.deleteItemFromListByTitle(titlename);
                                dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, titlename, img, ""));

                            }else{
                                dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, titlename, img, ""));

                            }
                        }else{
                           dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, titlename, null, ""));

                       }
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    /* ELSE CREATE A NEW NOTE WITH A NEW TITLE */
                    else {
                        dbHandler.addContact(new Contacts(s.getIntegerCount(Main2Activity.this) + 1, "" + title.getText().toString(), "" + note.getText().toString(), "" + formattedDate));
                        s.setIntegerCount(getApplicationContext(), s.getIntegerCount(Main2Activity.this) + 1);

                        if(image.getVisibility() == View.VISIBLE || paintView.strokeWidth != 0){
                            Bitmap bitmap = Bitmap.createBitmap(screenWidth, height, Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            paintView.draw(canvas);
                            image.setImageBitmap(bitmap);

                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                            int width = displayMetrics.widthPixels;
                            image.setMaxWidth(width);

                            ByteArrayOutputStream bos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            byte[] img = bos.toByteArray();
                            if(dbHandler21.ifTitleExists(titlename) == 1){

                                dbHandler21.deleteItemFromListByTitle(titlename);
                                dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, titlename, img, ""));

                            }else{
                                dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, titlename, img, ""));

                            }
                        }else{
                            dbHandler21.addContact(new Contacts21(s.getIntegerCount(Main2Activity.this) + 1, titlename, null, ""));

                        }

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Toast.makeText(getApplicationContext(), "New note created", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                }
                dialog.dismiss();
            }

        }).setNegativeButton("Discard", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        }).setIcon(R.drawable.logo);
        return builder;
    }



    public AlertDialog.Builder cancelImageDialog(String title, Context c){
        final String titletobedeleted = title;
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("KeyNotes");
        builder.setMessage("Do you want to delete the image..?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHandler21.deleteItemFromListByTitle(titletobedeleted);
                image.setVisibility(View.INVISIBLE);
                paintView.setVisibility(View.INVISIBLE);
                cancelbutton.setVisibility(View.INVISIBLE);
                eraserButton.setVisibility(View.INVISIBLE);
                bluebutton.setVisibility(View.INVISIBLE);
                blackbutton.setVisibility(View.INVISIBLE);
                redbutton.setVisibility(View.INVISIBLE);


                dialog.dismiss();
            }

        }).setNegativeButton("Discard", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Okay", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        }).setIcon(R.drawable.logo);
        return builder;
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }
    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
    }
    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording was incomplete";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Oops something went wrong.";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Please provide sufficient permissions from settings";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Some Network error.. Please try again";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server.. Please try again";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "Please speak something";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
    }
    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }
    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
    }
    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        Toast.makeText(getApplicationContext(),errorMessage, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        text = matches.get(0).trim().toString();
        if (title.isFocused() == true) {
            title.append(text + "");
        } else {
            note.append(text + "");
        }
    }
    @Override
    public void onPartialResults(Bundle bundle) {
    }
    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}

class SaveSharedPreference
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
