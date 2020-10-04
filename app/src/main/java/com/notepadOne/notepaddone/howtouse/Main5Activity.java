package com.notepadOne.notepaddone.howtouse;

import android.content.Intent;
import android.os.Bundle;

import com.notepadOne.notepaddone.MainActivity;
import com.notepadOne.notepaddone.R;

import androidx.appcompat.app.AppCompatActivity;


public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
