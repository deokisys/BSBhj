package com.bussystemforblind.bsb;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.database.Cursor;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    Button cardRgs, BusRsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        cardRgs = (Button)findViewById(R.id.cardRgs);
        cardRgs.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){

                Intent intent2 = new Intent(MainActivity.this,Card.class);
                startActivity(intent2);
            }
        });

        BusRsv = (Button)findViewById(R.id.BusRsv);
        BusRsv.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){

                Intent intent9 = new Intent(MainActivity.this,stt_test.class);
                startActivity(intent9);
            }
        });
    }
}

