package com.notepadOne.notepaddone;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {


    public interface ReturningValues{
        public void returned(String color);
    }

    public Activity c;
    public Dialog d;
    public Button btn_no1, btn_no2,btn_no3, btn_no4,btn_no5,btn_no6;

    public ReturningValues returningValues;

    public CustomDialogClass(Activity a, ReturningValues returningValues) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.returningValues =returningValues;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        btn_no1 = (Button) findViewById(R.id.btn_no1);
        btn_no2 = (Button) findViewById(R.id.btn_no2);
        btn_no3 = (Button) findViewById(R.id.btn_no3);
        btn_no4 = (Button) findViewById(R.id.btn_no4);
        btn_no5 = (Button) findViewById(R.id.btn_no5);
        btn_no6 = (Button) findViewById(R.id.btn_no6);

        btn_no1.setOnClickListener(this);
        btn_no2.setOnClickListener(this);
        btn_no3.setOnClickListener(this);
        btn_no4.setOnClickListener(this);
        btn_no5.setOnClickListener(this);
        btn_no6.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_no1:
                returningValues.returned("#FFEC8B");
                dismiss();
                break;
            case R.id.btn_no2:
                returningValues.returned("#00ff00");
                dismiss();
                break;
            case R.id.btn_no3:
                returningValues.returned("#FFC0CB");
                dismiss();
                break;
            case R.id.btn_no4:
                returningValues.returned("#AB82FF");
                dismiss();
                break;
            case R.id.btn_no5:
                returningValues.returned("#FFC125");
                dismiss();
                break;
            case R.id.btn_no6:
                returningValues.returned("#FF4040");
                dismiss();
                break;

            default:
                break;
        }
        dismiss();
    }
}