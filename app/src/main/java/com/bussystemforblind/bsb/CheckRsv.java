package com.bussystemforblind.bsb;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class CheckRsv extends AppCompatActivity implements OnInitListener {

    TextView checkTv;
    Button yes,no;
    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_rsv);

        myTTS = new TextToSpeech(this, this);

        /*상단바 디자인*/
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Intent intent = getIntent();
        final String busNumber = intent.getStringExtra("busNumber");
        final String dtnStation = intent.getStringExtra("Destination");
        final String stationId = intent.getStringExtra("stationId");
        final String routeId = intent.getStringExtra("routeId");
        final String dtnNumber = intent.getStringExtra("dtnNumber");

        checkTv = (TextView)findViewById(R.id.checkRsv);
        checkTv.setText(busNumber+" 번 버스\n\n"+dtnStation+"\n정류장\n\n" + "예약 하시겠습니까?");
        checkTv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        checkTv.setGravity(Gravity.CENTER_VERTICAL);
        checkTv.setTextSize(35);
        checkTv.setTextColor(Color.parseColor("#333333"));

        yes = (Button)findViewById(R.id.YES);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), estTime.class);
                intent1.putExtra("busNumber", busNumber);
                intent1.putExtra("Destination", dtnStation);
                intent1.putExtra("stationId", stationId);
                intent1.putExtra("routeId",routeId);
                intent1.putExtra("dtnNumber", dtnNumber);
                startActivity(intent1);
            }
        });
        no = (Button)findViewById(R.id.NO);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "처음화면으로 돌아갑니다.", Toast.LENGTH_LONG);
                myTTS.speak("처음화면으로 돌아갑니다.", TextToSpeech.QUEUE_ADD, null);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }
        });

    }

    @Override
    public void onInit(int status) {
    }
}
