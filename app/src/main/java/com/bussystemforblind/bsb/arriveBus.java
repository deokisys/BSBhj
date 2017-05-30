package com.bussystemforblind.bsb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOErrorCode;

import com.perples.recosdk.RECOBeaconRegion;
import java.util.ArrayList;
import java.util.Collection;
import com.perples.recosdk.RECOBeaconRegionState;
import com.perples.recosdk.RECOMonitoringListener;
import com.perples.recosdk.RECORangingListener;
import com.perples.recosdk.RECOServiceConnectListener;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;


public class arriveBus extends AppCompatActivity
        //implements RECOServiceConnectListener,RECORangingListener,RECOMonitoringListener
{
    String busNumber;
    String stationId;
    String routeId;
    String dtnStation,busId, dtnNumber;


    /*비콘*//*
    private RECOBeaconManager recoManager;
    private ArrayList<RECOBeaconRegion> rangingRegions;
    //
    private TextView tv;
    private TextView tv2;
    private TextView tv3;
    private TextView nu;
    //
    private ArrayList<RECOBeacon> mRangedBeacons;
    private int count=0;
    private int count2=0;
    private int count_f=0;
    private int count_t=0;
    private int count_e=0;
    private static final int REQUEST_LOCATION = 10;
    private View mLayout;*/

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
        busNumber = intent.getStringExtra("busNumber"); // 버스 번호
        stationId = intent.getStringExtra("stationId"); // 정류장 ID
        routeId = intent.getStringExtra("routeId"); // 버스 전체 노선 ID
        dtnStation = intent.getStringExtra("dtnStation"); // 목적 정류장 이름
        busId = intent.getStringExtra("busId"); // 버스 번호판 ex) "12가1234"

        /*비콘으로 버스와 나의 거리를 측정하여 탑승여부 확인*/
        /*mLayout = findViewById(R.id.activity_arrive_bus);

        tv = (TextView) findViewById(R.id.textview);
        tv2 = (TextView) findViewById(R.id.textview2);
        tv3 = (TextView) findViewById(R.id.textview3);
        nu=(TextView) findViewById(R.id.nu);
        nu.setText("정지");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//권한
            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.i("MainActivity", "The location permission (ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION) is not granted.");
                this.requestLocationPermission();
            } else {
                Log.i("MainActivity", "The location permission (ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION) is already granted.");
            }
        }

        boolean mScanRecoOnly = true;
        boolean mEnableBackgroundTimeout=false;
        recoManager = RECOBeaconManager.getInstance(this,mScanRecoOnly,mEnableBackgroundTimeout);//레코메니저 인스턴스 생성
        recoManager.bind(this);//연결시작
        recoManager.setRangingListener(this);//스캔 리스너*/
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
            intent1.putExtra("busNumber", busNumber); // 버스 번호
            intent1.putExtra("dtnStation", dtnStation); // 목적 정류장 이름
            intent1.putExtra("stationId", stationId); // 출발 정류장 ID
            intent1.putExtra("routeId",routeId); // 버스 전체 노선 ID
            intent1.putExtra("busId", busId); // 탑승할 버스의 버스 번호판 ex)"12가1234"
            startActivity(intent1);

        }else{ // 결제정보 전송 후 결제 승인이 안남
            Intent intent1 = new Intent(arriveBus.this, arriveBus.class);
            intent1.putExtra("busNumber", busNumber);
            intent1.putExtra("dtnStation", dtnStation);
            intent1.putExtra("stationId", stationId);
            intent1.putExtra("routeId",routeId);
            intent1.putExtra("busId", busId);
            startActivity(intent1);
        }
    }
/*
    @Override
    public void onServiceConnect() {

        Toast tt =Toast.makeText(this,"연결",Toast.LENGTH_SHORT);
        tt.show();

        //등록
        String mProximityUuid = "24DDF411-8CF1-440C-87CD-E368DAF9C93E";
        int mMajor1 = 1;

        int mMinor1 = 1;
        int mMinor2 = 2;
        int mMinor3 = 3;
        //RECOBeaconRegion mRecoRegion = new RECOBeaconRegion(mProximityUuid, mMajor1, "정류장");
        RECOBeaconRegion mRecoRegion1 = new RECOBeaconRegion(mProximityUuid, mMajor1, mMinor1, "버스앞");
        RECOBeaconRegion mRecoRegion2 = new RECOBeaconRegion(mProximityUuid, mMajor1, mMinor2,  "버스뒤");
        RECOBeaconRegion mRecoRegion3 = new RECOBeaconRegion(mProximityUuid, mMajor1,  mMinor3, "정류장");

        rangingRegions = new ArrayList<RECOBeaconRegion>();
        //rangingRegions.add(mRecoRegion);
        rangingRegions.add(mRecoRegion1);
        rangingRegions.add(mRecoRegion2);
        //rangingRegions.add(mRecoRegion3);
        recoManager.setScanPeriod(1);
        recoManager.setSleepPeriod(1);
        recoManager.setDiscontinuousScan(true);

        for(RECOBeaconRegion region : rangingRegions) {//스캔 시작
            try {
                //tv2.setText(tv2.getText()+"포문\n");
                recoManager.startRangingBeaconsInRegion(region);
                //recoManager.requestStateForRegion(region);
            } catch (RemoteException e) {

                //RemoteException 발생 시 작성 코드

            } catch (NullPointerException e) {

                //NullPointerException 발생 시 작성 코드

            }
        }

//연결시
    }
    @Override
    public void onServiceFail(RECOErrorCode recoErrorCode) {
        Toast tt2 =Toast.makeText(this,"연결안됨?",Toast.LENGTH_SHORT);
        tt2.show();
//연결안될시
    }


    public void didRangeBeaconsInRegion(Collection<RECOBeacon> recoBeacons,
                                        RECOBeaconRegion recoRegion) {
        int s = recoBeacons.size();
        String test = s+"";
        // tv3.setText(tv3.getText()+test);

        //ranging 중인 region에서 1초 간격으로 감지된
        //RECOBeacon들 리스트와 함께 이 callback 메소드를 호출합니다.
        //recoRegion에서 감지된 RECOBeacon 리스트 수신 시 작성 코드

        Log.d("1", "1");
        if (s == 0) {
            tv.setText(tv.getText()+"노인식\n");

        } else {
            //tv.setText(tv.getText()+"인식\n");
            mRangedBeacons = new ArrayList<RECOBeacon>(recoBeacons);
            RECOBeacon recoBeacon = mRangedBeacons.get(0);
            double rssi=recoBeacon.getRssi();
            double txpower=recoBeacon.getTxPower();
            String distance;


            //5번

            //1번 일치 종료
            //2번 앞뒤 다수결 - 차이가 1개면 멈추고 일치나오면 끝
            //3번 입구와 거리 2m 미만인경우
            if(rssi==0) { //거리구하는 공식
                distance = "0";
            }
            else {
                double ratio = rssi * 1.0 / txpower;
                double accuracy;
                if (ratio < 1.0) {
                    accuracy = Math.pow(ratio, 10);
                } else {
                    accuracy = (0.89976) * Math.pow(ratio, 7.7095) + 0.111;//거리구하는 공식
                }

                distance = accuracy+"\n";
                if(recoBeacon.getMinor()==1){//
                    tv.setText(distance);//앞
                }else{
                    tv2.setText(distance);//뒤
                }
                count++;
                A:
                if (count % 2 == 0) {
                    double a;//앞
                    double c;//뒤
                    double b = 4;//비콘 사이 거리


                    try {
                        a = Double.parseDouble((String) tv.getText());
                        c = Double.parseDouble((String) tv2.getText());
                    } catch (NumberFormatException e) {
                        break A;
                    }
                    double result = c * c - (a * a + b * b);//공식 삼각함수

                    if(a<2){//정문 거리가 2 미만
                        Toast ss = Toast.makeText(getApplicationContext(),"정문근처 끝",Toast.LENGTH_SHORT);
                        ss.show();
                        for(RECOBeaconRegion region : rangingRegions) {//스캔 종료
                            try {
                                recoManager.stopRangingBeaconsInRegion(region);
                            } catch (RemoteException e) {
                                //RemoteException 발생 시 작성 코드
                            } catch (NullPointerException e) {
                                //NullPointerException 발생 시 작성 코드
                            }
                        }
                    }

                    if (result < -5) {//앞으로
                        //nu.setText("앞으로");
                        count_f++;
                    } else if (result > 5) {//뒤로
                        //nu.setText("뒤로");
                        count_e++;
                    } else {//일치
                        //nu.setText("일치");
                        Toast ss = Toast.makeText(getApplicationContext(),"일치로 끝",Toast.LENGTH_SHORT);
                        ss.show();
                        for(RECOBeaconRegion region : rangingRegions) {
                            try {
                                recoManager.stopRangingBeaconsInRegion(region);
                            } catch (RemoteException e) {
                                //RemoteException 발생 시 작성 코드
                            } catch (NullPointerException e) {
                                //NullPointerException 발생 시 작성 코드
                            }
                        }
                    }
                    count2++;
                    if(count2==5){
                        if(count_f-count_e==1){
                            Toast ss = Toast.makeText(getApplicationContext(),"5초 정지",Toast.LENGTH_SHORT);
                            ss.show();
                        }
                        else if(count_f>count_e){
                            nu.setText("앞으로");
                        }
                        else if(count_f<count_e){
                            nu.setText("뒤로");
                        }
                        count2=0;
                    }

                    count=0;
                }
            }

        }
    }

    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion recoRegion, RECOErrorCode errorCode) {
        //ranging을 정상적으로 시작하지 못했을 경우 이 callback 메소드가 호출됩니다.
        //RECOErrorCode는 "Error Code"를 확인하시기 바랍니다.
        //ranging 실패 시 코드 작성
        tv.setText(tv.getText()+"\n실패");

    }


    //-------------------------------
    public void didDetermineStateForRegion(RECOBeaconRegionState recoRegionState,
                                           RECOBeaconRegion recoRegion) {
        //monitoring 시작 후에 monitoring 중인 region에 들어가거나 나올 경우
        //(region 의 상태에 변화가 생긴 경우) 이 callback 메소드가 호출됩니다.
        //didEnterRegion, didExitRegion callback 메소드와 함께 호출됩니다.
        //region 상태 변화시 코드 작성
        tv2.setText(tv2.getText()+"\n변화");
    }

    public void didEnterRegion(RECOBeaconRegion recoRegion, Collection<RECOBeacon> beacons) {
        //monitoring 시작 후에 monitoring 중인 region에 들어갈 경우 이 callback 메소드가 호출됩니다.
        // 0.2 버전부터 이 callback 메소드가 호출 될 경우,  recoRegion에서 감지된 비콘들을 전달합니다.
        //region 입장시 코드 작성
        tv2.setText(tv2.getText()+"\n입장");
    }

    public void didExitRegion(RECOBeaconRegion recoRegion) {
        //monitoring 시작 후에 monitoring 중인 region에서 나올 경우 이 callback 메소드가 호출됩니다.
        //region 퇴장시 코드 작성
        tv2.setText(tv2.getText()+"\n퇴장");
    }

    public void didStartMonitoringForRegion(RECOBeaconRegion recoRegion) {
        //monitoring 시작 후에 monitoring을 시작하고 이 callback 메소드가 호출됩니다.
        //monitoring 정상 실행 시 코드 작성
        tv2.setText(tv2.getText()+"\n정상");
    }

    public void monitoringDidFailForRegion(RECOBeaconRegion recoRegion, RECOErrorCode errorCode) {
        //monitoring이 정상적으로 시작하지 못했을 경우 이 callback 메소드가 호출됩니다.
        //RECOErrorCode는 "Error Code" 를 확인하시기 바랍니다.
        //monitoring 실패 시 코드 작성
        tv2.setText(tv2.getText()+"\n실패");
    }




    //
    private void requestLocationPermission() {//권한
        recoManager.setDiscontinuousScan(false);
        if(!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        Snackbar.make(mLayout, "Location permission is needed to monitor or range beacons", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(arriveBus.this , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
                    }
                })
                .show();
    }*/
}
