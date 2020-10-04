package com.notepadOne.notepaddone;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import androidx.annotation.*;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;


import com.notepadOne.notepaddone.trash.Contact2;
import com.notepadOne.notepaddone.trash.DBHandler2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win-8 on 05-02-2017.
 */

public class CustomAdapter extends ArrayAdapter<Contacts> {
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
    SaveSharedPreferences sharedPreferences;
    final DBHandler21  dbHandler21;   //database for main list

    final DBHandler2 dbHandler2;
    public CustomAdapter(Context context, ArrayList<Contacts> array2, String colorid) {
        super(context, -1, array2);
        this.context = context;
        this.array = array2;
        this.colorid = colorid;
        arrayList = new ArrayList<>();
        dbHandler = new DBHandler(context);
        dbHandler2 = new DBHandler2(context);
        dbHandler21 = new DBHandler21(context);

    }
    public void getObject(CustomAdapter customAdapter){
        this.customAdapter = customAdapter;
    }
    //DIALOG TO VERIFY TO DELETE NOTE OR NOT
    public AlertDialog.Builder buildDialog(Context c, final String title, final String summary, final String date) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Notes");
        builder.setMessage("Do you want to delete the note..?");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences = new SaveSharedPreferences();

                if (SaveSharedPreference.getIntegerCount(context) == 0) {
                    dbHandler2.addContact(new Contact2(sharedPreferences.getIntegerCount(context), "" + title, "" + summary,""+date));
                    sharedPreferences.setIntegerCount(context,sharedPreferences.getIntegerCount(context)+1);

                    dbHandler.deleteItemFromListByTitle(title);
                    dbHandler21.deleteItemFromListByTitle(title);

                    Intent intent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    customAdapter.notifyDataSetChanged();

                    dialog.dismiss();
                } else {
                    dbHandler2.addContact(new Contact2(sharedPreferences.getIntegerCount(context) + 1, "" + title, "" + summary,""+date));
                    sharedPreferences.setIntegerCount(context, sharedPreferences.getIntegerCount(context) + 1);
                    dbHandler.deleteItemFromListByTitle(title);
                    dbHandler21.deleteItemFromListByTitle(title);

                    Intent intent = new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    customAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder;
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
        //rowView.setPadding(50,50,50,50);

        //Random rnd = new Random();
        //int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        //rowView.setBackgroundColor(color);
        imageButton = rowView.findViewById(R.id.crossbutton);
       imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDialog(context,cn.getWordName(),cn.getMean(),cn.getDate()).show();

                }
        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAdapter.notifyDataSetChanged();

                Intent intent=new Intent(context,Main2Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title",cn.getWordName());
                view.getContext().startActivity(intent);
            }

        });
        //rowView.setBackgroundColor(Color.argb(190,238	,238	,0	));
        rowView.setBackgroundColor(Color.parseColor(colorid));
        return rowView;
    }
    @Override
    public void remove(@Nullable Contacts object) {

    }
    static class SaveSharedPreferences
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
}