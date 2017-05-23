package com.bussystemforblind.bsb;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.LinkedList;

public class InputBusNumber extends AppCompatActivity implements View.OnClickListener{

    BusAPI api = new BusAPI();
    Button btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_0,btn_reset,btn_ok,btn_hypen;
    TextView busNumber;
    String stationId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_bus_number);

        Intent intent = getIntent();
        stationId = intent.getStringExtra("stationId");

        /*상단바 디자인*/
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        btn_1 = (Button)findViewById(R.id.btn_1);
        btn_2 = (Button)findViewById(R.id.btn_2);
        btn_3 = (Button)findViewById(R.id.btn_3);
        btn_4 = (Button)findViewById(R.id.btn_4);
        btn_5 = (Button)findViewById(R.id.btn_5);
        btn_6 = (Button)findViewById(R.id.btn_6);
        btn_7 = (Button)findViewById(R.id.btn_7);
        btn_8 = (Button)findViewById(R.id.btn_8);
        btn_9 = (Button)findViewById(R.id.btn_9);
        btn_0 = (Button)findViewById(R.id.btn_0);
        btn_reset = (Button)findViewById(R.id.btn_reset);
        btn_ok = (Button)findViewById(R.id.btn_ok);
        btn_hypen = (Button)findViewById(R.id.btn_hypen);

        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_0.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_hypen.setOnClickListener(this);

        busNumber = (TextView)findViewById(R.id.BusNumber);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_1:
                busNumber.setText(busNumber.getText().toString() + " 1");
                break;
            case R.id.btn_2:
                busNumber.setText(busNumber.getText().toString() + " 2");
                break;
            case R.id.btn_3:
                busNumber.setText(busNumber.getText().toString() + " 3");
                break;
            case R.id.btn_4:
                busNumber.setText(busNumber.getText().toString() + " 4");
                break;
            case R.id.btn_5:
                busNumber.setText(busNumber.getText().toString() + " 5");
                break;
            case R.id.btn_6:
                busNumber.setText(busNumber.getText().toString() + " 6");
                break;
            case R.id.btn_7:
                busNumber.setText(busNumber.getText().toString() + " 7");
                break;
            case R.id.btn_8:
                busNumber.setText(busNumber.getText().toString() + " 8");
                break;
            case R.id.btn_9:
                busNumber.setText(busNumber.getText().toString() + " 9");
                break;
            case R.id.btn_0:
                busNumber.setText(busNumber.getText().toString() + " 0");
                break;
            case R.id.btn_reset:
                busNumber.setText("");
                break;
            case R.id.btn_hypen:
                busNumber.setText(busNumber.getText().toString() + " -");
                break;
            case R.id.btn_ok:
                LinkedList<String> busList = new LinkedList<>();
                try {
                    busList = api.getBusList(stationId);
                }catch (IOException e) {
                    e.printStackTrace();
                }
                String strBusNumber=busNumber.getText().toString();
                strBusNumber=strBusNumber.replaceAll(" ","");
                Log.d("StringBusNumber", strBusNumber);
                int i=0;
                for(i=0; i<busList.size(); i++){
                    if(busList.get(i).equals(strBusNumber)==true) break;
                }
                if(i<busList.size()){
                    //Intent intent = new Intent(getApplicationContext(), selectDestination.class);
                    Intent intent = new Intent(getApplicationContext(), selectDestination.class);
                    intent.putExtra("busNumber",strBusNumber);
                    intent.putExtra("stationId", stationId);
                    startActivity(intent);
                } else if(i==busList.size()){
                    busNumber.setText("");
                    Toast toast = Toast.makeText(getApplicationContext(), "정류장에서 출발하는 버스가 아닙니다.\n\n                 다시 입력해주세요.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                break;
        }
    }
}
