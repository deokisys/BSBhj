package com.bussystemforblind.bsb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class selectDestination extends AppCompatActivity implements OnInitListener{

    BusAPI busAPI = new BusAPI();
    LinearLayout topLL;
    TextView topTV1;
    String tmp="";
    String routeId = "";
    LinkedList<String> stationList = new LinkedList<>();
    Intent i;
    SpeechRecognizer mRecognizer;
    EditText stopname_input;
    private TextToSpeech myTTS;
    String busStopList = "";
    Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_destination);

        myTTS = new TextToSpeech(this,this);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        mRecognizer.startListening(i);

        stopname_input = (EditText) findViewById(R.id.stopname_input);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());

        /*상단바 디자인*/
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*이전 페이지에서 넘어온 데이터*/
        Intent intent = getIntent();
        final String busNumber = intent.getStringExtra("busNumber");
        final String stationId = intent.getStringExtra("stationId");

        /*목적지 정류장 List 가져오기*/
        //String busStopList = "";
        try {
            routeId = busAPI.getRouteId(busNumber);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
            busStopList=busAPI.busStop(routeId, stationId);// busStopList : 목적지 정류장들 List(","로 구분되어있는 String 문자열)
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*List가 비었을 때*/
        if(busStopList.equals("")){
            busStopList = "";
            Toast toast = Toast.makeText(getApplicationContext(), "정류장이 존재하지 않습니다.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

        /*정류장 List 나열, 클릭시 예약 페이지로 이동
            topLL = (LinearLayout)findViewById(R.id.dynamicArea);

            tmp = "";
            int black = 0;
            int id=0;
            for(int i=0; i<busStopList.length(); i++){
                if(busStopList.substring(i,i+1).equals(",")==false){
                    tmp = tmp+busStopList.substring(i,i+1);
                }
                else if(busStopList.substring(i,i+1).equals(",")){
                    if(tmp.equals("")==false){
                        topTV1 = new TextView(selectDestination.this);

                        Context context = this;
                        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());

                        topTV1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) px));
                        topTV1.setTextSize(30);
                        topTV1.setText(tmp);
                        topTV1.setTypeface(Typeface.MONOSPACE);
                        topTV1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        topTV1.setGravity(Gravity.CENTER_VERTICAL);
                        topTV1.setId(id);
                        stationList.add(tmp);
                        id++;
                        if(black==0){
                            topTV1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            topTV1.setTextColor(Color.parseColor("#333333"));
                            black=1;
                        }
                        else if(black==1){
                            topTV1.setBackgroundColor(Color.parseColor("#333333"));
                            topTV1.setTextColor(Color.parseColor("#ffffff"));
                            black=0;
                        }
                        topTV1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), CheckRsv.class);
                                intent.putExtra("busNumber", busNumber);
                                intent.putExtra("Destination", stationList.get(v.getId()));
                                intent.putExtra("stationId", stationId);
                                intent.putExtra("routeId",routeId);
                                startActivity(intent);
                            }
                        });
                        topLL.addView(topTV1);
                        tmp="";
                    }
                }
        }*/
    }
    public void onClick(View v) {
        v.setBackgroundColor(Color.parseColor("#fbe600"));
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {

        }

        @Override
        public void onResults(Bundle results) {
            int id = 0;
            int black = 1;
            topLL = (LinearLayout)findViewById(R.id.dynamicArea);
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            stopname_input.setText("" + rs[0]);
            mRecognizer.startListening(i);
            String stt = stopname_input.getText().toString();
            if(busStopList.contains(stt)){
                Toast.makeText(selectDestination.this, "정류장이 존재합니다.", Toast.LENGTH_SHORT).show();
                String text = "정류장이 존재합니다.";
                myTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
                int index = busStopList.indexOf(stt);
                String tmp = "";
                tmp = busStopList.substring(index);
                for(int i=0; i<tmp.length(); i++){
                    if(tmp.substring(i,i+1).equals(",")){
                        int index2 = tmp.indexOf(",");
                        tmp = tmp.substring(index-1, index2);
                    }
                }

                topTV1 = new TextView(selectDestination.this);
                topTV1.setText(tmp);

                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());

                topTV1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) px));
                topTV1.setTextSize(30);
                topTV1.setTypeface(Typeface.MONOSPACE);
                topTV1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                topTV1.setGravity(Gravity.CENTER_VERTICAL);
                if(black==0){
                    topTV1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    topTV1.setTextColor(Color.parseColor("#333333"));
                    black=1;
                }
                else if(black==1){
                    topTV1.setBackgroundColor(Color.parseColor("#333333"));
                    topTV1.setTextColor(Color.parseColor("#ffffff"));
                    black=0;
                }
                topTV1.setId(id);
                stationList.add(tmp);
                id++;

                 /*이전 페이지에서 넘어온 데이터*/
                Intent intent = getIntent();
                final String busNumber = intent.getStringExtra("busNumber");
                final String stationId = intent.getStringExtra("stationId");

                topTV1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), CheckRsv.class);
                        intent.putExtra("busNumber", busNumber);
                        intent.putExtra("Destination", stationList.get(v.getId()));
                        intent.putExtra("stationId", stationId);
                        intent.putExtra("routeId",routeId);
                        startActivity(intent);
                    }
                });
                topLL.addView(topTV1);
            }else{
                Toast.makeText(selectDestination.this, "정류장이 존재하지않습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    };

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        //String text = "정류장이름을 말씀해주세요.";
        //myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
