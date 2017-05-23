package com.bussystemforblind.bsb;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class busStop extends AppCompatActivity {
    LinearLayout linearLayout;
    String busNumber;
    String stationId;
    String routeId;
    String dtnStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop);

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

        linearLayout = (LinearLayout)findViewById(R.id.activity_bus_stop);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), busStop2.class);
                startActivity(intent1);
            }
        });
    }
}
