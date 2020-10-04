package com.notepadOne.notepaddone.trash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.*;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


import com.notepadOne.notepaddone.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter3 extends ArrayAdapter<Contact2> {

    private final Context context;
    private final ArrayList<Contact2> array;

    CustomAdapter3 customAdapter;
    final DBHandler2 dbHandler2;
    List<Contact2> arrayList;
    TextView titlename,summary,date;
    ImageButton imageButton;
    public CustomAdapter3(Context context, ArrayList<Contact2> array2) {
        super(context, -1, array2);
        this.context = context;
        this.array = array2;
        arrayList = new ArrayList<>();
        dbHandler2 = new DBHandler2(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.list_item_example, parent, false);
        final Contact2 cn = array.get(position);
        titlename = rowView.findViewById(R.id.titlename);
        titlename.setText(cn.getWordName());
        imageButton = rowView.findViewById(R.id.crossbutton);
        summary = rowView.findViewById(R.id.summary);
        summary.setText(cn.getMean());

        date = rowView.findViewById(R.id.date);
        date.setText(cn.getDate());

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDialog(context,cn.getWordName(),cn.getMean(),cn.getDate()).show();

            }
        });
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,Main4Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title",cn.getWordName());
                view.getContext().startActivity(intent);
            }
        });
        return rowView;
    }

    public AlertDialog.Builder buildDialog(Context c, final String title, final String summary, final String date) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Notes");
        builder.setMessage("Do you want to delete the note..?");

        builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHandler2.deleteItemFromListByTitle(title);
                Intent intent = new Intent(context, Main3Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                dialog.dismiss();
            }
        }).setIcon(R.drawable.logo);

        return builder;
    }

}
