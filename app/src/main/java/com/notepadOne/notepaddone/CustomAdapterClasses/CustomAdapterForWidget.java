package com.notepadOne.notepaddone.CustomAdapterClasses;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.notepadOne.notepaddone.Contacts;
import com.notepadOne.notepaddone.CustomAdapter;
import com.notepadOne.notepaddone.DBHandler;
import com.notepadOne.notepaddone.DBHandler21;
import com.notepadOne.notepaddone.R;
import com.notepadOne.notepaddone.Restore;
import com.notepadOne.notepaddone.trash.DBHandler2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win-8 on 05-02-2017.
 */

public class CustomAdapterForWidget extends ArrayAdapter<Contacts> {
    private final Context context;
    private final ArrayList<Contacts> array;
    TextView wordName,summary,date;
    Switch s;
    CustomAdapter customAdapter;
    final DBHandler dbHandler;
    List<Contacts> arrayList;
    LinearLayout linearLayout;
    String colorid;
    ImageButton imageButton;
    Restore restore;
    final DBHandler21 dbHandler21;   //database for main list
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    View lastselectView = null;
    final DBHandler2 dbHandler2;
    public CustomAdapterForWidget(Context context, ArrayList<Contacts> array2, String colorid, Restore restore) {
        super(context, -1, array2);
        this.context = context;
        this.array = array2;
        this.restore = restore;
        this.colorid = colorid;
        arrayList = new ArrayList<>();
        dbHandler = new DBHandler(context);
        dbHandler2 = new DBHandler2(context);
        dbHandler21 = new DBHandler21(context);

    }

    //GET VIEW METHOD
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.list_item_example, parent, false);
        final Contacts cn = array.get(position);
        wordName = rowView.findViewById(R.id.titlename);
        wordName.setText(cn.getWordName());

        summary = rowView.findViewById(R.id.summary);
        summary.setText(cn.getMean());
        date = rowView.findViewById(R.id.date);
        date.setText("Modified on:"+cn.getDate());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(lastselectView != null)
              {
                  lastselectView.setBackgroundColor(Color.parseColor("#FFEC8B"));
              }
              rowView.setBackgroundColor(Color.parseColor("#FFA500"));
              lastselectView = rowView;
              restore.innerMethod(cn.getWordName(), cn.getMean());
            }
        });
        rowView.setBackgroundColor(Color.parseColor(colorid));
        return rowView;
    }

}