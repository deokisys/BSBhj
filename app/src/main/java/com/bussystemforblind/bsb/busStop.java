package com.bussystemforblind.bsb;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class busStop extends AppCompatActivity implements TextToSpeech.OnInitListener{
    LinearLayout linearLayout;
    String busNumber, stationId, routeId, dtnStation, busId, dtnNumber;
    BusAPI api = new BusAPI();
    TextView Tv;
    String location="";

    private TimerTask mTask;
    private Timer mTimer;

    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop);

        myTTS = new TextToSpeech(this, this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());

        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Toast toast2 = Toast.makeText(getApplicationContext(), "버스요금 결제가 완료되었습니다.", Toast.LENGTH_LONG);
        toast2.show();
        myTTS.speak("버스요금결제가 완료되었습니다.", TextToSpeech.QUEUE_ADD, null);

        Intent intent = getIntent();
        busNumber = intent.getStringExtra("busNumber");
        stationId = intent.getStringExtra("stationId");
        routeId = intent.getStringExtra("routeId");
        dtnStation = intent.getStringExtra("Destination");
        busId = intent.getStringExtra("busId");
        dtnNumber = intent.getStringExtra("dtnNumber");

        try {
            location = api.getLocation(routeId,busId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Log.d("dtnNumber", "dtnNumber"+dtnNumber);
        Log.d("locationNumebr", "locationNumebr"+location);

        Tv.setText("목적지까지 "+(Integer.parseInt(dtnNumber)-Integer.parseInt(location))+" 정류장 \\n남았습니다.\\n\\n새로고침 하시려면\\n화면을 터치하세요.");
        String speak = Tv.getText().toString();
        myTTS.speak(speak, TextToSpeech.QUEUE_ADD, null);
        mTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    do{
                        location = api.getLocation(routeId,busId);
                    }while(location.equals(""));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        Tv.setText("목적지까지 "+(Integer.parseInt(dtnNumber)-Integer.parseInt(location))+" 정류장 \\n남았습니다.\\n\\n새로고침 하시려면\\n화면을 터치하세요.");
                    }
                });

                Log.d("5초마다", "ㅋㅋ");
                String speak = Tv.getText().toString();
                myTTS.speak(speak, TextToSpeech.QUEUE_ADD, null);
                /*버스가 도착지 전정류장에 도착시*/
                if(location.equals("1")){
                    mTimer.cancel();

                    /*하차정보 서버에 전송*/
                    //SocketManager manager = SocketManager.getManager();
                    //manager.sendMsg("10");
                    //String msg = manager.getMsg();
                    //Log.d("MSG",msg);

                    Intent intent1 = new Intent(busStop.this, busStop2.class);
                    intent1.putExtra("busNumber", busNumber);
                    intent1.putExtra("Destination", dtnStation);
                    intent1.putExtra("stationId", stationId);
                    intent1.putExtra("routeId",routeId);
                    intent1.putExtra("busId", busId);
                    intent1.putExtra("dtnNumber", dtnNumber);
                    startActivity(intent1);
                }
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask, 5000, 5000); // 5초
        Log.d("TimerTask", "실행");


        /*
        목적지 선택시 목적지의 정류장 순번을 받아온다.
        routeId 와 busId로 현재 버스의 정류장 순번을 알아낸다.
        목적지 정류장 순번과 현재 버스가 위치한 정류장 순번을 비교하여 목적지까지 몇정류장 남았는지 계산->5초
        전정류장위치시 서버에 전달
        해당 정류장에 정차시 서버에 하차 메세지 전달
        */

        linearLayout = (LinearLayout)findViewById(R.id.activity_bus_stop);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), busStop2.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onInit(int status) {

    }
}
