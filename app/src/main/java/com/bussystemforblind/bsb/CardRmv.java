package com.bussystemforblind.bsb;

import android.database.SQLException;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CardRmv extends AppCompatActivity implements OnInitListener{

    DB_Controller controller;
    TextView text;
    private TextToSpeech myTTS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_rmv);
        myTTS = new TextToSpeech(this, this);

        controller = new DB_Controller(this,"",null,1);

        text = (TextView)findViewById(R.id.text);
        controller.show_card(text);

        /*상단바 디자인*/
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }


    public void btnclick(View view){
        switch (view.getId()){
            case R.id.card_remove:
                try{
                    controller.delete_card();
                }catch (SQLException e){
                }
                if(TextUtils.isEmpty(text.getText())){
                    Toast.makeText(CardRmv.this, "제거할 카드가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    String text1 = "제거할 카드가 존재하지 않습니다.";
                    myTTS.speak(text1, TextToSpeech.QUEUE_ADD, null);
                }else{
                    Toast.makeText(CardRmv.this, "카드가 제거되었습니다.", Toast.LENGTH_SHORT).show();
                    String text1 = "카드가 제거되었습니다.";
                    myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
                    text = (TextView)findViewById(R.id.text);
                    controller.show_card(text);
                }



        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if(TextUtils.isEmpty(text.getText())){
            Toast.makeText(CardRmv.this, "제거할 카드가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            String text1 = "제거할 카드가 존재하지 않습니다.";
            myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
        }else{
            String text_guid = "등록되어 있는 카드의 카드번호는 ";
            String text_cardnum = text.getText().toString();
            int i = text_cardnum.indexOf("/");
            String text_cardnum_ok = text_cardnum.substring(0,i-1);
            String text3 = "입니다. 제거하시겠습니까?";
            myTTS.speak(text_guid, TextToSpeech.QUEUE_FLUSH, null);
            myTTS.speak(text_cardnum_ok, TextToSpeech.QUEUE_ADD, null);
            myTTS.speak(text3, TextToSpeech.QUEUE_ADD, null);
        }
    }
}
