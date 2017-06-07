package com.bussystemforblind.bsb;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{

    Button busRsv;
    Button cardRgs;
    BusAPI api = new BusAPI();
    String stationId="";
    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //private TextToSpeech myTTS;
        myTTS = new TextToSpeech(this, this);

        /*상단바 디자인*/
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*비콘 연결해서 정류장번호 얻음*/

        busRsv = (Button)findViewById(R.id.BusRsv);
        busRsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTTS.speak(busRsv.getText().toString(),TextToSpeech.QUEUE_ADD,null);
            }
        });

        busRsv.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                myTTS.speak(busRsv.getText().toString()+"버튼을 선택하셨습니다.",TextToSpeech.QUEUE_ADD,null);
                try {
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
                    stationId = api.getStationId("04178");
                    Log.d("stationID", stationId+"zz");
                } catch (IOException e) {
                    Log.d("error", e.toString());
                }
                Log.d("error", "1");
                Intent intent = new Intent(MainActivity.this, InputBusNumber.class);
                intent.putExtra("stationId", stationId);
                startActivity(intent);

                return false;
            }
        });

        cardRgs = (Button)findViewById(R.id.cardRgs);
        cardRgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTTS.speak(cardRgs.getText().toString(),TextToSpeech.QUEUE_ADD,null);
            }
        });

        cardRgs.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                myTTS.speak(cardRgs.getText().toString()+"버튼을 선택하셨습니다.",TextToSpeech.QUEUE_ADD,null);
                Intent intent2 = new Intent(MainActivity.this,Card.class);
                startActivity(intent2);
                return false;
            }
        });
    }

    public void onInit(int status) {
        myTTS.speak("현재 위치하신 정류장은 경기대후문.수원박물관.광교대학로 입니다.",TextToSpeech.QUEUE_ADD,null);
        Toast toast2 = Toast.makeText(getApplicationContext(), "현재 위치하신 정류장은 경기대후문.수원박물관.광교대학로 입니다.", Toast.LENGTH_LONG);
        toast2.show();
    }

    @Override
    public
    void onDestroy() {
        super.onDestroy();
        myTTS.shutdown();
    }
}
