package com.bussystemforblind.bsb;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;



public class CardRgs extends AppCompatActivity implements OnInitListener {

    TextView textView2;
    EditText cardnum, effdate, cvc, password, uname, regnum;
    DB_Controller controller;
    private TextToSpeech myTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_rgs);
        myTTS = new TextToSpeech(this,this);

        /*상단바 디자인*/
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        cardnum = (EditText)findViewById(R.id.cardnum_input);
        effdate = (EditText)findViewById(R.id.effdate_input);
        cvc = (EditText)findViewById(R.id.cvc_input);
        password = (EditText)findViewById(R.id.password_input);
        uname = (EditText)findViewById(R.id.uname_input);
        regnum = (EditText)findViewById(R.id.regnum_input);

        controller = new DB_Controller(this,"",null,1);

        textView2 = (TextView)findViewById(R.id.textView2);
        controller.show_card(textView2);
    }


    public void btn_click(View view){
        switch (view.getId()){
            case R.id.card_save:
                if(textView2.getText()!=null && !textView2.getText().equals("")){
                    Toast.makeText(CardRgs.this, "등록되어 있는 카드가 존재하여 더이상의 등록이 불가능합니다.", Toast.LENGTH_SHORT).show();
                    String text1 = "등록되어 있는 카드가 존재하여 더이상의 등록이 불가능합니다.";
                    myTTS.speak(text1, TextToSpeech.QUEUE_FLUSH, null);
                    break;
                }
                try{
                    //입력부족시
                    controller.insert_card(cardnum.getText().toString(), effdate.getText().toString(), Integer.parseInt(cvc.getText().toString()), Integer.parseInt(password.getText().toString()), uname.getText().toString(), regnum.getText().toString());
                }catch (SQLException e){
                    Toast.makeText(CardRgs.this, "이미 등록된 카드입니다.", Toast.LENGTH_SHORT).show();
                    String text2 = "이미 등록된 카드입니다.";
                    myTTS.speak(text2, TextToSpeech.QUEUE_FLUSH, null);
                }
                Toast.makeText(CardRgs.this, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                String text3 = "등록이 완료되었습니다.";
                myTTS.speak(text3, TextToSpeech.QUEUE_FLUSH, null);
                Intent back = new Intent(CardRgs.this, Card.class);
                startActivity(back);
                break;
        }
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
