package com.notepadOne.notepaddone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;


import com.notepadOne.notepaddone.CustomAdapterClasses.CustomAdapterForWidget;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class AppWidgetConfig extends AppCompatActivity implements Restore, CustomDialogClass.ReturningValues {
    public static final String SHARED_PREFS = "prefs";
    public static final String KEY_BUTTON_TEXT = "keyButtonText";
    public static final String KEY_BUTTON_TEXT2 = "keyButtonText2";
    public static final String KEY_BUTTON_TEXT3 = "keyButtonText3";

    public String title = "";
    public String content = "";
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    CustomAdapterForWidget customAdapter;
    ImageView imageView;
    ListView listView;
    final DBHandler  dbHandler  = new DBHandler (this);      //database for main list
    TextView text2;
    String color = "";
    CustomDialogClass.ReturningValues returningValues = new CustomDialogClass.ReturningValues() {
        @Override
        public void returned(String colorr) {

            color = colorr;
        }
    };
    @Override
    public void innerMethod(String titler, String contentr) {
        title = titler;
        content = contentr;
    }


    Restore restore = new Restore() {
        @Override
        public void innerMethod(String titler, String contentr) {
            content = contentr;
            title = titler;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_widget_config);


        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        text2 = findViewById(R.id.text2);
        text2.setVisibility(View.INVISIBLE);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        listView = findViewById(R.id.listview);
        imageView = findViewById(R.id.colorsoption);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogClass cdd=new CustomDialogClass(AppWidgetConfig.this,returningValues );
                cdd.show();
            }
        });
        List<Contacts> list = dbHandler.getAllContacts();
        final ArrayList<Contacts> arr = new ArrayList<>();
        for(Contacts contacts: list){
            arr.add(contacts);
        }
        if(list.size() == 0){
            text2.setVisibility(View.VISIBLE);
        }

        customAdapter = new CustomAdapterForWidget(AppWidgetConfig.this, arr,"#FFEC8B",restore);
        listView.setAdapter(customAdapter);
    }
    public void confirmConfiguration(View v) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        //update in the widget layout
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.example_layout);
        //setting the title of widget
        views.setOnClickPendingIntent(R.id.title, pendingIntent);
        views.setCharSequence(R.id.title, "setText", title);

        //setting the content of widget
        views.setOnClickPendingIntent(R.id.content, pendingIntent);
        views.setCharSequence(R.id.content, "setText", content);

        views.setOnClickPendingIntent(R.id.mainlayout, pendingIntent);
        try{
            views.setInt(R.id.mainlayout,"setBackgroundColor", Color.parseColor(color));
        }catch (Exception e){
            views.setInt(R.id.mainlayout,"setBackgroundColor", Color.parseColor("#FFEC8B"));

        }


        appWidgetManager.updateAppWidget(appWidgetId, views);

        //saves in the shared preference
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_BUTTON_TEXT + appWidgetId, title);
        editor.putString(KEY_BUTTON_TEXT2 + appWidgetId, content);
        editor.putString(KEY_BUTTON_TEXT3 + appWidgetId, color);

        editor.apply();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public void returned(String colorr) {
        color = colorr;
        /*
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_BUTTON_TEXT3 + appWidgetId, color);
        editor.apply();

         */
    }
}
