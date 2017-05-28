package com.bussystemforblind.bsb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class estTime extends AppCompatActivity implements TextToSpeech.OnInitListener{
    TextView estTv;
    Button cancleBtn, reBtn;
    LinearLayout linearLayout;
    BusAPI api = new BusAPI();

    private TimerTask mTask;
    private Timer mTimer;
    String busNumber;
    String stationId;
    String routeId;
    String staOrder;
    String busArrival;
    String dtnStation;
    String busId;
    String dtnNumber;

    private TextToSpeech myTTS;
    String speak = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_est_time);

        myTTS = new TextToSpeech(this, this);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());

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
        dtnNumber = intent.getStringExtra("dtnNumber");

        Log.d("routeId", routeId);
        Log.d("dtnStation", dtnStation);
        Log.d("dtnNumber", dtnNumber);

        try {
            staOrder=api.getStaOrder(routeId,busNumber);
            busArrival=api.getBusArrival(stationId,routeId,staOrder);
            busId=api.getBusId(stationId,routeId,staOrder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        estTv = (TextView)findViewById(R.id.estTime);
        estTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        estTv.setGravity(Gravity.CENTER_VERTICAL);
        estTv.setTextSize(35);
        estTv.setTextColor(Color.parseColor("#333333"));
        estTv.setText(busNumber+" 번 버스\n\n"+busArrival+"번째 전 정류장에\n\n" + "위치해 있습니다.");
        speak = estTv.getText().toString();

        /*도착예정버스의 교유번호, 정류장ID 서버에 전송*/
        //SocketManager manager = SocketManager.getManager();
        //manager.sendMsg("2-"+busId+"-"+stationId);
        //manager.sendMsg("2-0-"+stationId);
        //String msg = manager.getMsg();
        //Log.d("MSG",msg);

        /*비콘 정보 받기*/


        cancleBtn = (Button)findViewById(R.id.cancelRsv);
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimer.cancel();
                Toast toast = Toast.makeText(getApplicationContext(), "예약이 취소되었습니다.", Toast.LENGTH_LONG);
                myTTS.speak("예약이 취소되었습니다. 처음화면으로 돌아갑니다.", TextToSpeech.QUEUE_ADD, null);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        reBtn = (Button)findViewById(R.id.reBtn);
        reBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    staOrder=api.getStaOrder(routeId,busNumber);
                    busArrival=api.getBusArrival(stationId,routeId,staOrder);
                    //busId=api.getBusId(stationId,routeId,staOrder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                estTv.setText(busNumber+" 번 버스\n\n"+busArrival+"번째 전 정류장에\n\n" + "위치해 있습니다.");
                myTTS.speak(busNumber+" 번 버스\n\n"+busArrival+"번째 전 정류장에\n\n" + "위치해 있습니다.", TextToSpeech.QUEUE_ADD, null);
            }
        });

        linearLayout = (LinearLayout)findViewById(R.id.activity_est_time);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    staOrder=api.getStaOrder(routeId,busNumber);
                    busArrival=api.getBusArrival(stationId,routeId,staOrder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                estTv.setText(busNumber+" 번 버스\n\n"+busArrival+"번째 전 정류장에\n\n" + "위치해 있습니다.");
            }
        });

        /*5초마다 API접근해서 버스의 도착예정시간 파악*/
        mTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    busArrival=api.getBusArrival(stationId,routeId,staOrder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        estTv.setText(busNumber+" 번 버스\n\n"+busArrival+"번째 전 정류장에\n\n" + "위치해 있습니다.");
                    }
                });

                Log.d("5초마다", "ㅋㅋ");

                /*버스가 전정류장에 도착시*/
                if(busArrival.equals("1")){
                    mTimer.cancel();


                    /*도착예정버스의 교유번호, 정류장ID 서버에 전송*/
                    //SocketManager manager = SocketManager.getManager();
                    //manager.sendMsg("2-"+busId+"-"+stationId);
                    //manager.sendMsg("2-0-"+stationId);
                    //String msg = manager.getMsg();
                    //Log.d("MSG",msg);
                    /*비콘 정보 받기*/

                    Intent intent1 = new Intent(getApplicationContext(), arriveBus.class);
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

    }
    protected void onDestroy() {
        Log.i("test", "onDstory()");
        mTimer.cancel();
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        myTTS.speak(speak,TextToSpeech.QUEUE_ADD,null);
    }
}
