package com.bussystemforblind.bsb;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;

public class BusAPI {

    private String serviceKey = "=OqgPPoNQvW0xRXmNtbLAJMBlzpdujJRRlMUy1ikXZS%2FiiNwnshZ50IWHbBPfZbTMCxHsHwQUovBFHLrBY%2BOsog%3D%3D";

    /*정류장 고유 번호로 정류장ID를 알아내서 반환하는 매소드*/
    public String getStationId(String stationNumber) throws IOException{
        StringBuilder urlBuilder = new StringBuilder("http://openapi.gbis.go.kr/ws/rest/busstationservice"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("keyword","UTF-8") + "=" + URLEncoder.encode(stationNumber, "UTF-8")); /*정류소 ID*/

        int startIndex=0;
        int endIndex=0;
        StringBuilder sb;
        String stationId="";

        do{
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            startIndex=0;
            endIndex=0;

            if(sb.length()!=0){
                startIndex = sb.indexOf("<stationId>")+11;
                endIndex = sb.indexOf("</stationId>");
                stationId = sb.substring(startIndex,endIndex);
                Log.d("Station ID : ",stationId);
            }
        }while(startIndex>sb.length());

        return stationId;
    }

    /*그 정류장(stationId)에 정차하는 버스들의 번호를 LinkedList에 저장하여 반환 */
    public LinkedList<String> getBusList(String stationId) throws IOException{
        StringBuilder urlBuilder = new StringBuilder("http://openapi.gbis.go.kr/ws/rest/busstationservice/route"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("stationId","UTF-8") + "=" + URLEncoder.encode(stationId, "UTF-8")); /*정류소 ID*/
        URL url = new URL(urlBuilder.toString());

        int startIndex= 12;
        int endIndex = 0;
        StringBuilder sb;
        LinkedList BusList = new LinkedList();

        do{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());

            startIndex= 12;
            endIndex = 0;
            while(startIndex>11){
                startIndex = sb.indexOf("<routeName>",startIndex+1)+11;
                endIndex = sb.indexOf("</routeName>",endIndex+1);
                if(startIndex>11){
                    BusList.add(sb.substring(startIndex,endIndex));
                    Log.d("BusList",sb.substring(startIndex,endIndex));
                }
            }
        }while(startIndex>sb.length());

        return BusList;
    }

    /*버스번호로 그 버스의 경로ID(routeId)를 알아내 반환하는 메소드*/
    public String getRouteId(String busNumber) throws IOException{
        StringBuilder urlBuilder = new StringBuilder("http://openapi.gbis.go.kr/ws/rest/busrouteservice"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("keyword","UTF-8") + "=" + URLEncoder.encode(busNumber, "UTF-8")); /*노선번호*/
        URL url = new URL(urlBuilder.toString());

        int startIndex= 0;
        int endIndex = 0;
        StringBuilder sb;
        String routeId;

        do{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());

            startIndex= 0;
            endIndex = 0;
            startIndex = sb.indexOf("<routeId>")+9;
            endIndex = sb.indexOf("</routeId>");
            routeId=sb.substring(startIndex,endIndex);
            Log.d("routeID : ", routeId);
        }while(startIndex>sb.length());

        return routeId;
    }

    /*해당 정류장에서부터 반환점까지 목적지로 설정할 수 있는 정류장을 String으로 저장하여 반환*/
    public String busStop(String routeId, String stationId) throws IOException {

        StringBuilder urlBuilder = new StringBuilder("http://openapi.gbis.go.kr/ws/rest/busrouteservice/station"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode(routeId, "UTF-8")); /*노선 ID*/
        URL url = new URL(urlBuilder.toString());

        int startIndex= 0;
        int endIndex = 0;
        StringBuilder sb;
        String stationName = "";

        do{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());

            startIndex= 12;
            endIndex = 0;

            while(startIndex>11){
                startIndex = sb.indexOf("<stationId>",startIndex)+11;
                endIndex = sb.indexOf("</stationId>",endIndex+1);
                Log.d("starIndex", Integer.toString(startIndex));
                Log.d("endIndex", Integer.toString(endIndex));
                Log.d("stationId",sb.substring(startIndex,endIndex)+", "+stationId);
                if(sb.substring(startIndex,endIndex).equals(stationId)){
                    break;
                }
            }

            int turnIndex = sb.indexOf("<turnYn>Y", startIndex);

            while(startIndex>0&&endIndex>0&& startIndex<turnIndex && endIndex<turnIndex){
                startIndex = sb.indexOf("<stationName>",startIndex)+13;
                endIndex  = sb.indexOf("</stationName>",endIndex+1);
                if(startIndex>0&&endIndex>0&& startIndex<turnIndex && endIndex<turnIndex){
                    Log.d("starIndex", Integer.toString(startIndex));
                    Log.d("endIndex", Integer.toString(endIndex));
                    stationName = stationName +"," +sb.substring(startIndex,endIndex);
                    Log.d("stationName", sb.substring(startIndex,endIndex));
                }
            }

        }while(startIndex>sb.length());

        return stationName;
    }

    /*해당 정류장에서부터 반환점까지 목적지로 설정할 수 있는 정류장의 "이름 + 순번" 들을 Linkedlist로 저장하여 반환*/
    public LinkedList<String> busStopList(String routeId, String stationId) throws IOException {

        StringBuilder urlBuilder = new StringBuilder("http://openapi.gbis.go.kr/ws/rest/busrouteservice/station"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode(routeId, "UTF-8")); /*노선 ID*/
        URL url = new URL(urlBuilder.toString());

        int startIndex= 0;
        int endIndex = 0;
        StringBuilder sb;
        LinkedList<String> stationList = new LinkedList<String>();

        do{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());

            startIndex= 12;
            endIndex = 0;

            while(startIndex>11){
                startIndex = sb.indexOf("<stationId>",startIndex)+11;
                endIndex = sb.indexOf("</stationId>",endIndex+1);
                Log.d("starIndex", Integer.toString(startIndex));
                Log.d("endIndex", Integer.toString(endIndex));
                Log.d("stationId",sb.substring(startIndex,endIndex)+", "+stationId);
                if(sb.substring(startIndex,endIndex).equals(stationId)){
                    break;
                }
            }

            int turnIndex = sb.indexOf("<turnYn>Y", startIndex);

            while(startIndex>0&&endIndex>0&& startIndex<turnIndex && endIndex<turnIndex){
                String stationName ="";
                startIndex = sb.indexOf("<stationName>",startIndex)+13;
                endIndex  = sb.indexOf("</stationName>",endIndex+1);
                if(startIndex>0&&endIndex>0&& startIndex<turnIndex && endIndex<turnIndex){
                    Log.d("starIndex", Integer.toString(startIndex));
                    Log.d("endIndex", Integer.toString(endIndex));
                    stationName = sb.substring(startIndex,endIndex);
                    Log.d("stationName", sb.substring(startIndex,endIndex));
                }

                startIndex = sb.indexOf("<stationSeq>",startIndex)+12;
                endIndex  = sb.indexOf("</stationSeq>",endIndex+1);
                if(startIndex>0&&endIndex>0&& startIndex<turnIndex && endIndex<turnIndex){
                    Log.d("starIndex", Integer.toString(startIndex));
                    Log.d("endIndex", Integer.toString(endIndex));
                    stationName = stationName+sb.substring(startIndex,endIndex);
                    Log.d("stationName", sb.substring(startIndex,endIndex));
                }

                stationList.add(stationName);

            }

        }while(startIndex>sb.length());

        return stationList;
    }

    /*busNumber를 가진 버스가 sationId라를 정류장이 몇번째 정류장 인가 정류장 순번을 알아내는 메소드*/
    public String getStaOrder(String stationId, String busNumber) throws IOException{
        StringBuilder urlBuilder = new StringBuilder("http://openapi.gbis.go.kr/ws/rest/busarrivalservice"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("stationId","UTF-8") + "=" + URLEncoder.encode(stationId, "UTF-8")); /*정류장번호*/
        URL url = new URL(urlBuilder.toString());

        int startIndex= 12;
        int endIndex = 0;
        StringBuilder sb;
        String staOrder="";

        do{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());

            startIndex= 12;
            endIndex = 0;
            LinkedList<String> rNList = new LinkedList<String>();
            while(startIndex>11){
                startIndex = sb.indexOf("<routeName>",startIndex+1)+11;
                endIndex = sb.indexOf("</routeName>",endIndex+1);
                if(startIndex>11){
                    rNList.add(sb.substring(startIndex,endIndex));
                }
            }
            startIndex= 11;
            endIndex = 0;
            LinkedList<String> sOList = new LinkedList<String>();
            while(startIndex>10){
                startIndex = sb.indexOf("<staOrder>",startIndex+1)+10;
                endIndex = sb.indexOf("</staOrder>",endIndex+1);
                if(startIndex>10){
                    sOList.add(sb.substring(startIndex,endIndex));
                }
            }

            for(int i=0; i<rNList.size(); i++){
                if(rNList.get(i).equals(busNumber.toString())==true){
                    staOrder=sOList.get(i);
                }
            }
        }while(startIndex>sb.length());

        return staOrder;
    }

    /*버스가 언제 도착하는지 알아내는 메소드, 몇 번째 전 정류장에 위치하는지 반환*/
    public String getBusArrival(String stationId, String routeId, String staOrder) throws IOException{
        StringBuilder urlBuilder = new StringBuilder("http://openapi.gbis.go.kr/ws/rest/busarrivalservice"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("stationId","UTF-8") + "=" + URLEncoder.encode(stationId, "UTF-8")); /*정류소 ID*/
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode(routeId, "UTF-8")); /*노선 아이디*/
        urlBuilder.append("&" + URLEncoder.encode("staOrder","UTF-8") + "=" + URLEncoder.encode(staOrder, "UTF-8")); /*노선의 정류소 순번*/
        URL url = new URL(urlBuilder.toString());

        int startIndex= 0;
        int endIndex = 0;
        StringBuilder sb;
        String busArrival="";

        do{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());

            Log.d("getBusArrival", sb.toString());

            startIndex= 0;
            endIndex = 0;
            startIndex = sb.indexOf("<locationNo1>")+13;
            endIndex = sb.indexOf("</locationNo1>",startIndex+1);
            if(startIndex<endIndex){
                busArrival=sb.substring(startIndex,endIndex);
            }
        }while(startIndex>sb.length());

        return busArrival;
    }

    /*사용자가 예약한 정보를 바탕으로 곧도착하는 버스의 버스번호판의 번호(BusId)를 반환하는 메소드*/
    public String getBusId(String stationId, String routeId, String staOrder) throws IOException{
        StringBuilder urlBuilder = new StringBuilder("http://openapi.gbis.go.kr/ws/rest/busarrivalservice"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("stationId","UTF-8") + "=" + URLEncoder.encode(stationId, "UTF-8")); /*정류소 ID*/
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode(routeId, "UTF-8")); /*노선 아이디*/
        urlBuilder.append("&" + URLEncoder.encode("staOrder","UTF-8") + "=" + URLEncoder.encode(staOrder, "UTF-8")); /*노선의 정류소 순번*/
        URL url = new URL(urlBuilder.toString());

        int startIndex= 0;
        int endIndex = 0;
        StringBuilder sb;
        String busId="";

        do{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            System.out.println(sb.toString());

            Log.d("getBusId", sb.toString());

            startIndex= 0;
            endIndex = 0;
            startIndex = sb.indexOf("<plateNo1>")+10;
            endIndex = sb.indexOf("</plateNo1>",startIndex+1);

            if(startIndex<endIndex){
                busId=sb.substring(startIndex,endIndex);
            }
        }while(startIndex>sb.length());

        return busId;
    }

    /*노선ID routeId 와 버스번호 busId 를 가지고 현재 버스가 몇번째 정류장에 위치하는지 반환*/
    public String getLocation(String routeId, String busId) throws IOException{
        String location=null;

        StringBuilder urlBuilder = new StringBuilder("http://openapi.gbis.go.kr/ws/rest/buslocationservice"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("routeId","UTF-8") + "=" + URLEncoder.encode(routeId, "UTF-8")); /*노선 ID*/
        URL url = new URL(urlBuilder.toString());

        int startIndex= 0;
        int endIndex = 0;
        StringBuilder sb;

        do{
            do{
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-type", "application/json");
                System.out.println("Response code: " + conn.getResponseCode());
                BufferedReader rd;
                if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();
                conn.disconnect();
            }while(sb.indexOf("NORMAL SERVICE.")==-1);

            startIndex = sb.indexOf("<plateNo>",startIndex)+9;
            endIndex = sb.indexOf("</plateNo>",startIndex);

            System.out.println(sb.substring(startIndex, endIndex));

            if(startIndex>0&&startIndex<endIndex&&sb.substring(startIndex,endIndex).equals(busId)){
                startIndex = sb.indexOf("<stationSeq>",startIndex)+12;
                endIndex = sb.indexOf("</stationSeq>",startIndex+1);
                location = sb.substring(startIndex,endIndex);
            }else if(!sb.substring(startIndex,endIndex).equals(busId)){
                location=null;
            }
        }while(location==null);

        Log.d("Location", location);

        return location;
    }

}
