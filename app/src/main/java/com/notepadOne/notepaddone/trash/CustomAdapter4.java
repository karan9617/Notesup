package com.notepadOne.notepaddone.trash;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.notepadOne.notepaddone.R;

import java.util.ArrayList;

public class CustomAdapter4 extends ArrayAdapter<String> {
    ArrayList<String> title;
    ArrayList<String> notes;
    Context context;
    TextView dropsummary,droptitlename;
    public CustomAdapter4(Context context, ArrayList<String> title) {
        super(context, -1, title);
        this.title = title;
        this.context = context;
        //  this.notes = notes;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.list_item_example2, parent, false);
        final String cn = title.get(position);
        final String st[] = cn.split(":");
        droptitlename = rowView.findViewById(R.id.droptitlename);
        dropsummary = rowView.findViewById(R.id.dropsummary);
        droptitlename.setText(st[0]);
        dropsummary.setText(st[1]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Main4Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("title",st[0]);
                view.getContext().startActivity(intent);
            }
        });
        return rowView;
    }
}