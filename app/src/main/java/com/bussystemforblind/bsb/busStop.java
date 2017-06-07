package com.bussystemforblind.bsb;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class busStop extends AppCompatActivity implements TextToSpeech.OnInitListener {
    LinearLayout linearLayout;
    String busNumber, stationId, routeId, dtnStation, busId;
    BusAPI busAPI = new BusAPI();
    TextView Tv;
    String location="";
    int stopNum=0; // 목적정류장 까지 남은 정류장 수
    int dtnNum=0;
    int locNum=0;

    int k=0;


    private TimerTask mTask;
    private Timer mTimer;

    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop);

        myTTS = new TextToSpeech(this, this);

        /*상단바 디자인*/
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
        busNumber = intent.getStringExtra("busNumber"); // 탑승 중인 버스의 번호
        stationId = intent.getStringExtra("stationId"); // 출발한 정류장 ID
        routeId = intent.getStringExtra("routeId"); // 탑승중인 버스의 전체 노선 ID
        dtnStation = intent.getStringExtra("dtnStation"); // 목적 정류장 이름
        busId = intent.getStringExtra("busId"); // 탑승중인 버스의 번호판 ex)"12가1234"

        /*해당버스에 대하여 목적 정류장의 순번을 알아냄*/
        String dtnStationNum = null;
        LinkedList<String> list = new LinkedList<String>();

        Log.d("BusStop 클래스", "목적 정류장 순번을 알아냄");
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
            list=busAPI.busStopList(routeId, stationId);// ㅣist : 목적지 정류장들 List ex) "정류장 이름 + 정류장 순번"
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        for(int i=0; i<list.size(); i++){
            Log.d("목적 정류장 이름", dtnStation);
            if(list.get(i).indexOf(dtnStation)!=-1){
                dtnStationNum = list.get(i).substring(list.get(i).indexOf(dtnStation)+dtnStation.length()); // 목적 정류장 순번만 잘라냄
                Log.d("목적 정류장 이름,순번", list.get(i).toString());
                Log.d("목적 정류장 순번", "dtnStationNum : "+dtnStationNum);
                break;
            }
        }

        Log.d("BusStop 클래스", "현재 탑승중인 버스가 위치한 정류장 순번을 알아냄");
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
            /*탑승중인 버스가 위치한 정류장의 순번*/
            Log.d("busId",busId);
            location = busAPI.getLocation(routeId,busId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Log.d("dtnStationNum", "dtnStationNum"+dtnStationNum);
        Log.d("locationNumebr", "locationNumeber"+location);

        dtnNum = Integer.parseInt(dtnStationNum);
        locNum = Integer.parseInt(location);;

        if(dtnNum>locNum){
            stopNum = dtnNum - locNum;
        }else if(dtnNum<locNum){
            stopNum = locNum - dtnNum;
        }

        Log.d("stopNum", "stopNum : "+stopNum);

        Tv = (TextView) findViewById(R.id.dtnTv);

        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                Tv.setText("목적지까지 "+stopNum+" 정류장 \n남았습니다.\n\n새로고침 하시려면\n화면을 터치하세요.");
            }
        });

        Log.d("BusStop 클래스", "현재 탑승중인 버스가 위치한 정류장 순번을 알아냄");
        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
            /*탑승중인 버스가 위치한 정류장의 순번*/
            //location = busAPI.getLocation(routeId,busId); //오류 발생
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Log.d("locationNumebr", "locationNumeber"+location);


        mTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("BusStop 클래스", "현재 탑승중인 버스가 위치한 정류장 순번을 알아냄");
                try {
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
            /*탑승중인 버스가 위치한 정류장의 순번*/
                    location = busAPI.getLocation(routeId,busId);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                Log.d("locationNumebr", "locationNumeber"+location);

                locNum = Integer.parseInt(location);

                if(dtnNum>locNum){
                    stopNum = dtnNum - locNum;
                }else if(dtnNum<locNum){
                    stopNum = locNum - dtnNum;
                }

                Log.d("stopNum", "stopNum : "+stopNum);

                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        Tv.setText("목적지까지 "+stopNum+" 정류장 \n남았습니다.\n\n새로고침 하시려면\n화면을 터치하세요.");
                    }
                });

                /*버스가 도착지 전정류장에 도착시*/
                if(stopNum==1){
                                                   /*하차정보 서버에 전송*/
                    //[6]-2. [어플] 서버에게 다음 정류장에서 하차한다는 메세지를 전달

                    SocketManager manager = SocketManager.getManager();
                    manager.sendMsg("10");
                    String msg = manager.getMsg();
                    Log.d("MSG",msg); // 0 : 전송 실패 1 : 전송 성공

                    if(msg.equals("1")){
                        mTimer.cancel();
                        Intent intent1 = new Intent(busStop.this, busStop2.class);
                        intent1.putExtra("busNumber", busNumber);
                        intent1.putExtra("dtnStation", dtnStation);
                        intent1.putExtra("stationId", stationId);
                        intent1.putExtra("routeId",routeId);
                        intent1.putExtra("busId", busId);
                        startActivity(intent1);
                    }
                }
            }
        };
        mTimer = new Timer();
        if(stopNum>3){
            mTimer.schedule(mTask, 0, 10000); // 5초
        }else if(stopNum<=3){
            mTimer.schedule(mTask, 0, 5000); // 5초
        }

        //API 접근 오류 났을 때
//        for(k=0;k <2; k++){
//            SystemClock.sleep(5000);
//            Log.d("k", "k = "+Integer.toString(k));
//                /*
//                locNum = dtnNum-i;
//                if(dtnNum>locNum){
//                    stopNum = dtnNum - locNum;
//                }else if(dtnNum<locNum){
//                    stopNum = locNum - dtnNum;
//                }
//                */
//            stopNum--;
//
//            Log.d("stopNum", "stopNum : "+stopNum);
//
//            runOnUiThread(new Runnable(){
//                @Override
//                public void run() {
//                    Tv.setText("목적지까지 "+stopNum+" 정류장 \n남았습니다.\n\n새로고침 하시려면\n화면을 터치하세요.");
//                }
//            });
//        }

        /*버스가 도착지 전정류장에 도착시*/
//        if(stopNum==1){
//                           /*하차정보 서버에 전송*/
//            //[6]-2. [어플] 서버에게 다음 정류장에서 하차한다는 메세지를 전달
///*
//                    SocketManager manager = SocketManager.getManager();
//                    manager.sendMsg("10");
//                    String msg = manager.getMsg();
//                    Log.d("MSG",msg); // 0 : 전송 실패 1 : 전송 성공
//*/
//
//            Log.d("페이지 넘어감","busStop2로");
//            Intent intent1 = new Intent(busStop.this, busStop2.class);
//            intent1.putExtra("busNumber", busNumber);
//            intent1.putExtra("dtnStation", dtnStation);
//            intent1.putExtra("stationId", stationId);
//            intent1.putExtra("routeId",routeId);
//            intent1.putExtra("busId", busId);
//            startActivity(intent1);
//        }





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
                    String speak = Tv.getText().toString();
                    myTTS.speak(speak, TextToSpeech.QUEUE_ADD, null);
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
