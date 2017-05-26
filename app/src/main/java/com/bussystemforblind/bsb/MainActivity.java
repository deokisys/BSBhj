package com.bussystemforblind.bsb;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button busRsv;
    Button cardRgs;
    BusAPI api = new BusAPI();
    String stationId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*상단바 디자인*/
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*비콘 연결해서 정류장번호 얻음*/

        /*예약하기 버튼이 눌리면*/
        busRsv = (Button)findViewById(R.id.BusRsv);
        
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());

        busRsv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    stationId = api.getStationId("04178");
                    Log.d("stationID", stationId+"zz");
                } catch (IOException e) {
                    Log.d("error", e.toString());
                }
                Log.d("error", "1");
                Intent intent = new Intent(MainActivity.this, InputBusNumber.class);
                intent.putExtra("stationId", stationId);
                startActivity(intent);
            }
        });

        cardRgs = (Button)findViewById(R.id.cardRgs);
        cardRgs.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){

                Intent intent2 = new Intent(MainActivity.this,Card.class);
                startActivity(intent2);
            }
        });
    }
}
