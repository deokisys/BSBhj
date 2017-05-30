package com.bussystemforblind.bsb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;

import com.perples.recosdk.RECOBeaconRegion;
import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;



public class estTime extends AppCompatActivity implements TextToSpeech.OnInitListener {
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

        /*상단바 디자인*/
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Intent intent = getIntent();
        busNumber = intent.getStringExtra("busNumber"); // 버스 번호
        stationId = intent.getStringExtra("stationId"); // 정류장 ID
        routeId = intent.getStringExtra("routeId"); // 버스 전체노선 ID
        dtnStation = intent.getStringExtra("Destination"); // 목적 정류장 이름

        try {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
            // 현재 정류장이 예약한 버스의 몇번째 정류장인지 알아냄
            staOrder=api.getStaOrder(routeId,busNumber);
            // 해당 노선에서 해당 정류장에 도착할 버스가 몇번째 전 정류장에 위치해 있는지 알아냄
            busArrival=api.getBusArrival(stationId,routeId,staOrder);
            // 도착할 버스의 표지판 번호(busId)를 알아냄
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

        //새로고침 버튼 클릭 시 위의 과정 반복
        reBtn = (Button)findViewById(R.id.reBtn);
        reBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
                    staOrder=api.getStaOrder(routeId,busNumber);
                    busArrival=api.getBusArrival(stationId,routeId,staOrder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                estTv.setText(busNumber+" 번 버스\n\n"+busArrival+"번째 전 정류장에\n\n" + "위치해 있습니다.");
                myTTS.speak(busNumber+" 번 버스\n\n"+busArrival+"번째 전 정류장에\n\n" + "위치해 있습니다.", TextToSpeech.QUEUE_ADD, null);
            }
        });

        // 화면 클릭시에도 새로고침 기능
        linearLayout = (LinearLayout)findViewById(R.id.activity_est_time);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
                    staOrder=api.getStaOrder(routeId,busNumber);
                    busArrival=api.getBusArrival(stationId,routeId,staOrder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                estTv.setText(busNumber+" 번 버스\n\n"+busArrival+"번째 전 정류장에\n\n" + "위치해 있습니다.");
            }
        });

        /*5초마다 자동으로 API접근해서 버스의 도착예정시간 파악*/
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

                /*버스가 전정류장에 도착시*/
                if(busArrival.equals("1")){
                    mTimer.cancel();

                    /*도착예정버스의 교유번호, 정류장ID (예약 정보) 서버에 전송*/
                    //SocketManager manager = SocketManager.getManager();
                    //manager.sendMsg("2-"+busId+"-"+stationId);
                    //manager.sendMsg("2-0-"+stationId);
                    //String msg = manager.getMsg();
                    //Log.d("MSG",msg);

                    /*도착예정인 버스 앞, 뒷문에 부착된 비콘 정보 받기*/

                    Intent intent1 = new Intent(getApplicationContext(), arriveBus.class);
                    intent1.putExtra("busNumber", busNumber); // 버스 번호
                    intent1.putExtra("dtnStation", dtnStation); // 목적 정류장 이름
                    intent1.putExtra("stationId", stationId); // 정류장 ID
                    intent1.putExtra("routeId",routeId); // 버스 전체 노선ID
                    intent1.putExtra("busId", busId); // 도착예정인 버스의 버스번호판 번호 ex) "29가1234"
                    startActivity(intent1);
                }
            }
        };
        mTimer = new Timer();
        if(Integer.parseInt(busArrival)>3){
            mTimer.schedule(mTask, 10000, 10000); // 10초
            Log.d("10초마다", "반복");
        }else if(Integer.parseInt(busArrival)<=3){
            mTimer.schedule(mTask, 5000, 5000); // 5초
            Log.d("5초마다", "반복");
        }

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


