package com.bussystemforblind.bsb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Card extends AppCompatActivity {
    Button cardShow, cardRgs2, cardRmv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        /*상단바 디자인*/
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        cardShow = (Button)findViewById(R.id.cardShow);
        cardRgs2 = (Button)findViewById(R.id.cardRgs2);
        cardRmv = (Button)findViewById(R.id.cardRmv);

        cardShow.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent4 = new Intent(Card.this, CardShow.class);
                startActivity(intent4);
            }
        });

        cardRgs2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view){
                Intent intent3 = new Intent(Card.this,CardRgs.class);
                startActivity(intent3);
            }
        });

        cardRmv.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent5 = new Intent(Card.this, CardRmv.class);
                startActivity(intent5);
            }
        });

    }
}
