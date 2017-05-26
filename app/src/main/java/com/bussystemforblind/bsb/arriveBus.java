package com.bussystemforblind.bsb;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

public class arriveBus extends AppCompatActivity {
    String busNumber;
    String stationId;
    String routeId;
    String dtnStation,busId, dtnNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrive_bus);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());

        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try{
            Thread.sleep(5000);
        }catch (Exception e){

        }

        Intent intent = getIntent();
        busNumber = intent.getStringExtra("busNumber");
        stationId = intent.getStringExtra("stationId");
        routeId = intent.getStringExtra("routeId");
        dtnStation = intent.getStringExtra("Destination");
        busId = intent.getStringExtra("busId");
        dtnNumber = intent.getStringExtra("dtnNumber");


        /*비콘으로 버스와 나의 거리를 측정하여 탑승여부 확인*/

        /*DB에 접근하여 결제정보 가져옴*/

        /*서버에 결제정보 전송*/
        //SocketManager manager = SocketManager.getManager();
        //manager.sendMsg("2-"+busId+"-"+stationId);
        //manager.sendMsg("8-T-0101045445715584-05/21-56712");
        //String msg = manager.getMsg();
        //Log.d("MSG",msg);

        /*결제정보 전송 확인후 다음 페이지로 이동*/
        //if(msg.equals("9-1")){
        if(true){
            Intent intent1 = new Intent(arriveBus.this, busStop.class);
            intent1.putExtra("busNumber", busNumber);
            intent1.putExtra("Destination", dtnStation);
            intent1.putExtra("stationId", stationId);
            intent1.putExtra("routeId",routeId);
            intent1.putExtra("busId", busId);
            intent1.putExtra("dtnNumber", dtnNumber);
            startActivity(intent1);

        }else{
            Intent intent1 = new Intent(arriveBus.this, arriveBus.class);
            intent1.putExtra("busNumber", busNumber);
            intent1.putExtra("Destination", dtnStation);
            intent1.putExtra("stationId", stationId);
            intent1.putExtra("routeId",routeId);
            intent1.putExtra("busId", busId);
            intent1.putExtra("dtnNumber", dtnNumber);
            startActivity(intent1);
        }
    }
}
