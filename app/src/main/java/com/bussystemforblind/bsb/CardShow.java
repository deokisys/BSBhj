package com.bussystemforblind.bsb;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class CardShow extends AppCompatActivity implements OnInitListener{

    TextView textView;
    private TextToSpeech myTTS;
    DB_Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_show);
        myTTS = new TextToSpeech(this,this);

        controller = new DB_Controller(this, "", null, 1);

        textView = (TextView)findViewById(R.id.textView);
        controller.show_card(textView);

        /*상단바 디자인*/
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (TextUtils.isEmpty(textView.getText())){
            Toast.makeText(CardShow.this, "등록된 카드가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            String text = "등록된 카드가 존재하지 않습니다.";
            myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }else{
            String text_guid = "등록되어 있는 카드의 카드번호는 ";
            String text_cardnum = textView.getText().toString();
            int i = text_cardnum.indexOf("/");
            String text_cardnum_ok = text_cardnum.substring(0,i-1);
            String text3 = "입니다.";
            myTTS.speak(text_guid, TextToSpeech.QUEUE_FLUSH, null);
            myTTS.speak(text_cardnum_ok, TextToSpeech.QUEUE_ADD, null);
            myTTS.speak(text3, TextToSpeech.QUEUE_ADD, null);
        }
    }
}
