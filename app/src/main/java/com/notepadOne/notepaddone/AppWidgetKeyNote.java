package com.notepadOne.notepaddone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RemoteViews;
import static com.notepadOne.notepaddone.AppWidgetConfig.*;
import static com.notepadOne.notepaddone.AppWidgetConfig.SHARED_PREFS;

public class AppWidgetKeyNote extends AppWidgetProvider {


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            String buttonText = prefs.getString(KEY_BUTTON_TEXT + appWidgetId, "Press me");

            String contenttext = prefs.getString(KEY_BUTTON_TEXT2 + appWidgetId, "Press me");
            String color = prefs.getString(KEY_BUTTON_TEXT3 + appWidgetId, "Press me");



            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_layout);
            views.setOnClickPendingIntent(R.id.mainlayout, pendingIntent);
            views.setCharSequence(R.id.title, "setText", buttonText);
            views.setCharSequence(R.id.content, "setText", contenttext);

            try{
                views.setInt(R.id.mainlayout,"setBackgroundColor", Color.parseColor(color));
            }
            catch (Exception e)
            {
                views.setInt(R.id.mainlayout,"setBackgroundColor", Color.parseColor("#FFEC8B"));
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


}


