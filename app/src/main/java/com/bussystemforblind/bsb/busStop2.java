package com.bussystemforblind.bsb;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class busStop2 extends AppCompatActivity implements TextToSpeech.OnInitListener {

    String busNumber, stationId, routeId, dtnStation, busId, dtnNumber;
    Button btn;
    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop2);
        myTTS = new TextToSpeech(this, this);
        /*상단바 디자인*/
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Intent intent = getIntent();
        busNumber = intent.getStringExtra("busNumber");
        stationId = intent.getStringExtra("stationId");
        routeId = intent.getStringExtra("routeId");
        dtnStation = intent.getStringExtra("Destination");
        busId = intent.getStringExtra("busId");
        dtnNumber = intent.getStringExtra("dtnNumber");

        btn = (Button) findViewById(R.id.getOff);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*서버에 하차 완료 메시지 날림*/
                //SocketManager manager = SocketManager.getManager();
                //manager.sendMsg("12-T");
                //String msg = manager.getMsg();
                //Log.d("MSG",msg);

                Toast toast2 = Toast.makeText(getApplicationContext(), "하차가 완료되었습니다.", Toast.LENGTH_LONG);
                toast2.show();
                myTTS.speak("하차가 완료되었습니다.",TextToSpeech.QUEUE_ADD, null);

                Intent intent = new Intent(busStop2.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onInit(int status) {

    }
}
