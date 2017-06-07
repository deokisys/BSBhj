package com.bussystemforblind.bsb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class busStop2 extends AppCompatActivity implements TextToSpeech.OnInitListener {

    String busNumber, stationId, routeId, dtnStation, busId, dtnNumber;
    Button btn, notBtn;
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
        dtnStation = intent.getStringExtra("dtnStation");
        busId = intent.getStringExtra("busId");


        // 하차 완료 버튼
        btn = (Button) findViewById(R.id.getOff);
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*서버에 하차 완료 메시지 날림*/

                //[7]-1. [어플] 사용자로부터 하차 확인을 받고 서버에게 하차 완료 메세지 전송

                SocketManager manager = SocketManager.getManager();
                manager.sendMsg("12-T");
                String msg = manager.getMsg();
                Log.d("MSG",msg); // 0 : 전송 실패 1 : 전송 성공



                Toast toast2 = Toast.makeText(getApplicationContext(), "하차가 완료되었습니다.", Toast.LENGTH_LONG);
                toast2.show();
                myTTS.speak("하차가 완료되었습니다.",TextToSpeech.QUEUE_ADD, null);

                Intent intent = new Intent(busStop2.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myTTS.speak("하차 완료",TextToSpeech.QUEUE_ADD, null);
            }
        });

        // 하차 실패 버튼
        notBtn = (Button) findViewById(R.id.nogGetOff);
        notBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                /*서버에 하차 실패 메시지 날림*/

                //[8]-1. [어플] 사용자로부터 하차하지 못했다는 입력을 받고 서버에게 하차 실패 메세지 전송
                SocketManager manager = SocketManager.getManager();
                manager.sendMsg("12-F");
                String msg = manager.getMsg();
                //Log.d("MSG",msg); // 0 : 전송 실패 1 : 전송 성공


                Toast toast2 = Toast.makeText(getApplicationContext(), "하차에 실패하였습니다.", Toast.LENGTH_LONG);
                toast2.show();
                myTTS.speak("하차에 실패하였습니다.",TextToSpeech.QUEUE_ADD, null);

                Intent intent = new Intent(busStop2.this, MainActivity.class);
                startActivity(intent);
                return false;
            }
        });
        notBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myTTS.speak("하차 실패",TextToSpeech.QUEUE_ADD, null);
            }
        });

    }

    @Override
    public void onInit(int status) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTTS.shutdown();
    }
}
