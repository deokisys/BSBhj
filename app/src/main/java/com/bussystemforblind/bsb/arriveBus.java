package com.bussystemforblind.bsb;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class arriveBus extends AppCompatActivity {
    LinearLayout linearLayout;

    String busNumber;
    String stationId;
    String routeId;
    String dtnStation;

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

        Intent intent = getIntent();
        busNumber = intent.getStringExtra("busNumber");
        stationId = intent.getStringExtra("stationId");
        routeId = intent.getStringExtra("routeId");
        dtnStation = intent.getStringExtra("Destination");



        /*비콘으로 버스와 나의 거리를 측정하여 탑승여부 확인*/
        /*서버에 결제정보 전송*/
        /*결제정보 전송 확인후 다음 페이지로 이동*/
        linearLayout = (LinearLayout)findViewById(R.id.activity_arrive_bus);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "버스요금 결제가 완료되었습니다.", Toast.LENGTH_LONG);
                toast.show();
                Intent intent1 = new Intent(getApplicationContext(), busStop.class);
                intent1.putExtra("busNumber", busNumber);
                intent1.putExtra("Destination", dtnStation);
                intent1.putExtra("stationId", stationId);
                intent1.putExtra("routeId",routeId);
                startActivity(intent1);
            }
        });
    }
}
