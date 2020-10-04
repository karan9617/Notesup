package com.notepadOne.notepaddone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import androidx.annotation.*;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

 */
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.notepadOne.notepaddone.Database.DBHandlerForLock;
import com.notepadOne.notepaddone.Database.DBHandlerForSession;
import com.notepadOne.notepaddone.howtouse.Main5Activity;
import com.notepadOne.notepaddone.trash.Main3Activity;
import com.notepadOne.notepaddone.wallet.DBHandlerForColors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

// MAIN ACTIVITY WHERE THE NOTES ARE DISPLAYED AND GRID VIEW IS DISPLAYED

public class MainActivity extends AppCompatActivity implements CustomDialogClass.ReturningValues {
    ListView listView;
    TextView addanote,noteshead;
    final DBHandler  dbHandler  = new DBHandler (this);      //database for main list

    ArrayList<Contacts> arr = new ArrayList<>();
    EditText mainedit;
    final DBHandlerForColors dbHandlerForColors  = new DBHandlerForColors(this);      //database for main list
    // EDIT VIEW FOR SEARCHING THE ELEMENTS
    ImageView mainlogo;
    CustomAdapter customAdapter;                                    //fav list custom adapter
    GridView gridview;
    SaveSharedPreferenceGridList s;
  // AdView adView;

    final DBHandlerForSession dbHandlerForSession = new DBHandlerForSession(this);

    final DBHandlerForLock dbHandlerForLock  = new DBHandlerForLock(this);
    CustomDialogClass.ReturningValues returningValues = new CustomDialogClass.ReturningValues() {
        @Override
        public void returned(String color) {
            if(SaveSharedPreferenceGridList.getUserName(MainActivity.this).length() == 0){

                //      s = new SaveSharedPreferenceGridList();
                //    s.setUserName(getApplicationContext(), "d");

                gridview.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                dbHandlerForColors.droptable();
                dbHandlerForColors.add(new Contacts(1,color,"",""));

                customAdapter = new CustomAdapter(MainActivity.this, arr,color);
                customAdapter.notifyDataSetChanged();
                customAdapter.getObject(customAdapter);

                listView.setAdapter(customAdapter);
            }
            else {
                gridview.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                dbHandlerForColors.droptable();
                dbHandlerForColors.add(new Contacts(1,color,"",""));

                customAdapter = new CustomAdapter(MainActivity.this, arr,color);
                customAdapter.notifyDataSetChanged();
                customAdapter.getObject(customAdapter);
                gridview.setAdapter(customAdapter);
            }
        }
    };
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
/*
        MobileAds.initialize(this,"ca-app-pub-8206113478901010~2151283403");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);



 */



        mainedit = findViewById(R.id.mainedit);
        mainedit.setVisibility(View.INVISIBLE);
        noteshead = findViewById(R.id.noteshead);
        if(dbHandlerForLock.getAllContacts().size() != 0){
            if(dbHandlerForSession.getAllContacts().size() != 0)
            {
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                listView = findViewById(R.id.listview);
                addanote = findViewById(R.id.addanote);
                mainlogo = findViewById(R.id.mainlogo);
                gridview = (GridView) findViewById(R.id.gridview);
                gridview.setVisibility(View.INVISIBLE);

                //GETTING ALL NOTES FROM THE DATABASE IF PRESENT
                List<Contacts> contacts = dbHandler.getAllContacts();
                for (Contacts cn : contacts) {
                    arr.add(cn);
                }
                //CHECKING IF THE DATABASE HAS NOTES OF NOT
                if (arr.isEmpty() == true) {
                    addanote.setVisibility(View.VISIBLE);
                    mainlogo.setVisibility(View.VISIBLE);
                } else {
                    mainlogo.setVisibility(View.INVISIBLE);
                    addanote.setVisibility(View.INVISIBLE);
                }

                /*****          SETTING UP THE CUSTOM ADAPTER IN THE LISTVIEW  OR GRIDVIEW         ********/


                /*  Initially the length of username is zero so the list view is displayed on the screen
                 *       When the user taps the grid view then the string is inserted with a string and the length is greater the 0 */

                if (SaveSharedPreferenceGridList.getUserName(MainActivity.this).length() == 0) {

                    //      s = new SaveSharedPreferenceGridList();
                    //    s.setUserName(getApplicationContext(), "d");


                    if (dbHandlerForColors.getAllContacts().size() == 0) {
                        dbHandlerForColors.add(new Contacts(1, "#FFEC8B", "", ""));
                    }

                    String inputcolor = dbHandlerForColors.getAllContacts().get(0).getWordName();

                    gridview.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);

                    customAdapter = new CustomAdapter(MainActivity.this, arr, inputcolor);
                    customAdapter.notifyDataSetChanged();
                    customAdapter.getObject(customAdapter);

                    listView.setAdapter(customAdapter);
                } else {

                    gridview.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);

                    if (dbHandlerForColors.getAllContacts().size() == 0) {
                        dbHandlerForColors.add(new Contacts(1, "#FFEC8B", "", ""));
                    }

                    String inputcolor = dbHandlerForColors.getAllContacts().get(0).getWordName();

                    customAdapter = new CustomAdapter(MainActivity.this, arr, inputcolor);
                    customAdapter.notifyDataSetChanged();
                    customAdapter.getObject(customAdapter);

                    gridview.setAdapter(customAdapter);
                }
                /**** FAV BUTTON FOR ADDING NEW ELEMENT    *****/

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                        intent.putExtra("title", "0");
                        startActivity(intent);
                    }
                });
                /* FOR CHANGING THE LISTVIEW OF THE SEARCHED ELEMENTS IN THE EDITTEXT*/

                mainedit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        ArrayList<String> type_name_filter = new ArrayList<String>();

                        String text = editable.toString();
                        for (int i = 0; i < arr.size(); i++) {
                            if ((arr.get(i).getWordName().toString().toLowerCase()).contains(text.toLowerCase())) {
                                type_name_filter.add(arr.get(i).getWordName() + ":" + arr.get(i).getMean());
                            }
                        }
                        listUpdate(type_name_filter);
                    }
                });
            }
            else{
                Intent intent = new Intent(getApplicationContext(), LockCheck.class);
                startActivity(intent);
            }
    }
        else{
            Intent intent = new Intent(getApplicationContext(), StarterActivity.class);
            startActivity(intent);
        }
    }
    /* METHOD FOR CHANGIN GTHE LIST VIEW OF THE EDITTEXT*/
    public void listUpdate(ArrayList<String> data) {

        if(SaveSharedPreferenceGridList.getUserName(MainActivity.this).length() == 0) {

            CustomAdapter2 customAdapter2 = new CustomAdapter2(MainActivity.this, data);
            listView.setAdapter(customAdapter2);
            // listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, data));
        }
        else{
            CustomAdapter2 customAdapter2 = new CustomAdapter2(MainActivity.this, data);
            gridview.setAdapter(customAdapter2);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id== R.id.action_email_change){
            Intent intent = new Intent(getApplicationContext(), ChangeEmailActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_lock){
            Intent intent = new Intent(getApplicationContext(), LockChange.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_trash){
            Intent intent = new Intent(getApplicationContext(), Main3Activity.class);
            startActivity(intent);
            return true;
        }

        if(id == R.id.action_howtouse){
            Intent intent = new Intent(getApplicationContext(), Main5Activity.class);
            startActivity(intent);
        }

        if(id == R.id.action_search){
            View view = findViewById(R.id.action_search);
            view.setVisibility(View.INVISIBLE);
            noteshead.setVisibility(View.INVISIBLE);
            mainedit.setVisibility(View.VISIBLE);
            showSoftKeyboard(mainedit);
        }

        if(id == R.id.action_privacy){
            Intent intent = new Intent(getApplicationContext(),Main7Activity.class);
            startActivity(intent);
        }

        if(id == R.id.grid_view){
            s = new SaveSharedPreferenceGridList();
            s.setUserName(getApplicationContext(), "D");
            gridview.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);

            gridview.setAdapter(customAdapter);

        }
        if(id == R.id.list_view){
            s = new SaveSharedPreferenceGridList();
               s.setUserName(getApplicationContext(), "");
            gridview.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(customAdapter);
        }
       if(id == R.id.colors){
            CustomDialogClass cdd=new CustomDialogClass(MainActivity.this,returningValues );
            cdd.show();
        }
        return super.onOptionsItemSelected(item);
    }
    //DIALOG BOZ FOR AKSING THE USER FOR MICROPHONE PERMISSION
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if(mainedit.getVisibility() == View.VISIBLE)
        {
            View view = findViewById(R.id.action_search);
            view.setVisibility(View.VISIBLE);
            mainedit.setVisibility(View.INVISIBLE);
            hideSoftKeyboard(mainedit);
            noteshead.setVisibility(View.VISIBLE);
            listView.setAdapter(customAdapter);

        }else {
            finishAndRemoveTask();
            dbHandlerForSession.droptable();
        }
        this.finishAffinity();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHandlerForSession.droptable();
        finishAndRemoveTask();
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

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    private void requestAudioPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO))

            {
                Toast.makeText(this, "Please grant permissions to record audio", Toast.LENGTH_LONG).show();
                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);
            }
            else
            {
                // Show user dialog to grant permission to record audio
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_RECORD_AUDIO);
            }

        }
        //If permission is granted, then go ahead recording audio
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            //Go ahead with recording audio now
        }
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permissions Denied, Please select okay to ask questions.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void returned(String color) {
        if(SaveSharedPreferenceGridList.getUserName(MainActivity.this).length() == 0){

            //      s = new SaveSharedPreferenceGridList();
            //    s.setUserName(getApplicationContext(), "d");
            dbHandlerForColors.droptable();

            dbHandlerForColors.add(new Contacts(0,color,"",""));
            gridview.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);

            customAdapter = new CustomAdapter(MainActivity.this, arr,color);
            customAdapter.notifyDataSetChanged();
            customAdapter.getObject(customAdapter);

            listView.setAdapter(customAdapter);
        }
        else {
            gridview.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
            dbHandlerForColors.droptable();
            dbHandlerForColors.add(new Contacts(0,color,"",""));

            customAdapter = new CustomAdapter(MainActivity.this, arr,color);
            customAdapter.notifyDataSetChanged();
            customAdapter.getObject(customAdapter);

            gridview.setAdapter(customAdapter);
        }
    }
}

class SaveSharedPreferenceGridList
{
    static final String PREF_USER_NAME= "initial";
    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }
    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }
}
