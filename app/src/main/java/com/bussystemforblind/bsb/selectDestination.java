package com.bussystemforblind.bsb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class selectDestination extends AppCompatActivity implements OnInitListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    BusAPI busAPI = new BusAPI();
    LinearLayout topLL;
    TextView topTV1;
    String tmp = "";
    String routeId = "";
    LinkedList<String> stationList = new LinkedList<>();
    Intent i;
    SpeechRecognizer mRecognizer;
    EditText stopname_input;
    private TextToSpeech myTTS;
    String busStopList = "";
    Context context = this;
    String busNumber;
    String stationId;
    String DtnStaionNum = "";
    private GestureDetector detector;
    String stt_f = "";
    String stt_t = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_destination);

        myTTS = new TextToSpeech(this, this);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);


        detector = new GestureDetector(this, this);

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
        busNumber = intent.getStringExtra("busNumber");
        stationId = intent.getStringExtra("stationId");

        /*목적지 정류장 List 가져오기*/
        //String busStopList = "";
        try {
            routeId = busAPI.getRouteId(busNumber);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyLog().build());
            busStopList = busAPI.busStop(routeId, stationId);// busStopList : 목적지 정류장들 List(","로 구분되어있는 String 문자열)
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*List가 비었을 때*/
        if (busStopList.equals("")) {
            busStopList = "";
            Toast toast = Toast.makeText(getApplicationContext(), "운행중인 버스가 없습니다.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
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
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            stopname_input.setText("" + rs[0]);

            String stt = stopname_input.getText().toString();

            stt_f = stt;
            stt_t = "뷁";
            if (stt.contains(" ")) {
                String split[] = stt.split(" ");
                stt_f = split[0];
                stt_t = split[1];
            }
            myTTS.speak(stt_f, TextToSpeech.QUEUE_ADD, null);
            if (!stt_t.equals("뷁")) {
                myTTS.speak(stt_t, TextToSpeech.QUEUE_ADD, null);
            }
            myTTS.speak("를 입력하셨습니다. 맞으시면 화면을 길게 터치해주시고, 다시입력하시고 싶으시면 화면을 더블터치해주세요.", TextToSpeech.QUEUE_ADD, null);

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
        myTTS.shutdown();
    }

    @Override
    public void onInit(int status) {
        String text = "정류장이름을 말씀해주세요.";
        myTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
        mRecognizer.startListening(i);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Toast.makeText(this, "5초이상 길게터치됨", Toast.LENGTH_SHORT).show();

        int id = 0;
        int black = 1;
        topLL = (LinearLayout) findViewById(R.id.dynamicArea);
        if (busStopList.matches(".*" + stt_f + ".*") || busStopList.matches(".*" + stt_t + ".*")) {
            Toast.makeText(selectDestination.this, "정류장이 존재합니다.", Toast.LENGTH_SHORT).show();
            String text = "입력하신 결과에 대한 정류장이 존재합니다. 터치하여 확인해 주세요.";
            myTTS.speak(text, TextToSpeech.QUEUE_ADD, null);


            int index_stt_f = busStopList.indexOf(stt_f);
            int index_stt_t = busStopList.indexOf(stt_t);

            String tmp = "";
            String tmp2 = "";


            if (busStopList.contains(stt_f)) {
                tmp2 = busStopList.substring(0, index_stt_f);
                for (int m = 0; m < tmp2.length(); m++) {
                    if (tmp2.substring(m, m + 1).equals(",") || tmp2.substring(m, m + 1).equals(tmp2.charAt(0))) {
                        tmp2 = tmp2.substring(m + 1);
                    }
                }
                tmp = busStopList.substring(index_stt_f);
                for (int k = 0; k < tmp.length(); k++) {
                    if (tmp.substring(k, k + 1).equals(",")) {
                        tmp = tmp.substring(0, k);
                    }
                }
            } else {
                tmp2 = busStopList.substring(0, index_stt_t);
                for (int m = 0; m < tmp2.length(); m++) {
                    if (tmp2.substring(m, m + 1).equals(",") || tmp2.substring(m, m + 1).equals(tmp2.charAt(0))) {
                        tmp2 = tmp2.substring(m + 1);
                    }
                }
                tmp = busStopList.substring(index_stt_t);
                for (int l = 0; l < tmp.length(); l++) {
                    if (tmp.substring(l, l + 1).equals(",")) {
                        tmp = tmp.substring(0, l);
                    }
                }
            }
            tmp = tmp2.concat(tmp);
            topTV1 = new TextView(selectDestination.this);
            topTV1.setText(tmp);

            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());

            topTV1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) px));
            topTV1.setTextSize(30);
            topTV1.setTypeface(Typeface.MONOSPACE);
            topTV1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            topTV1.setGravity(Gravity.CENTER_VERTICAL);
            if (black == 0) {
                topTV1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                topTV1.setTextColor(Color.parseColor("#333333"));
                black = 1;
            } else if (black == 1) {
                topTV1.setBackgroundColor(Color.parseColor("#333333"));
                topTV1.setTextColor(Color.parseColor("#ffffff"));
                black = 0;
            }
            topTV1.setId(id);
            stationList.add(tmp);
            id++;

            /*목적지까지 몇정류장인지 계산*/
            LinkedList<String> list = new LinkedList<String>();
            try {
                list = busAPI.busStopList(routeId, stationId);// ㅣist : 목적지 정류장들 List
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).matches(tmp)) {
                    DtnStaionNum = list.get(i).substring(0, 1);
                    Log.d("DtnStaionNum", DtnStaionNum);
                }
            }

            topTV1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CheckRsv.class);
                    intent.putExtra("busNumber", busNumber);
                    intent.putExtra("Destination", stationList.get(v.getId()));
                    intent.putExtra("stationId", stationId);
                    intent.putExtra("routeId", routeId);
                    intent.putExtra("dtnNumber", DtnStaionNum);
                    startActivity(intent);
                }
            });
            topLL.addView(topTV1);
        } else {
            Toast.makeText(selectDestination.this, "정류장이 존재하지않습니다.", Toast.LENGTH_SHORT).show();
            myTTS.speak("입력하신 결과에 대한 정류장이 존재하지 않습니다.", TextToSpeech.QUEUE_ADD, null);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }
    public void restart(){
        mRecognizer.startListening(i);
    }
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Toast.makeText(this, "더블터치됨", Toast.LENGTH_SHORT).show();
        myTTS.speak("다시 말씀해주세요.", TextToSpeech.QUEUE_ADD, null);
        restart();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
