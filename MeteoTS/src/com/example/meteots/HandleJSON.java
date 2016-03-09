package com.example.meteots;


import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;


import org.json.JSONArray;
import org.json.JSONObject;


import android.annotation.SuppressLint;

public class HandleJSON {
	private String temperatureOut = "temperatureOut";
   private String temperatureIn = "temperatureIn";
   private String humidity = "humidity";
   private String pressure = "pressure";
   private String created_at = "created_at";
   private String urlString = null;

   public volatile boolean parsingComplete = true;
private static java.util.Scanner scanner;
   public HandleJSON(String url){
      this.urlString = url;
   }
   public String getTemperatureOut(){
      return temperatureOut;
   }
   public String getTemperatureIn(){
      return temperatureIn;
   }
   public String getHumidity(){
      return humidity;
   }
   public String getPressure(){
      return pressure;
   }
   public String getDate(){
	      return created_at;
	   }

   @SuppressLint("NewApi")
   public void readAndParseJSON(String in) {
      try {
    	  
    	  JSONObject reader = new JSONObject(in);
    	  JSONArray jsonarray  = (JSONArray) reader.get("feeds");

   	  
    	    JSONObject main = jsonarray.getJSONObject(0);
    	    
    	    temperatureIn = main.getString("field3");
    	    temperatureOut = main.getString("field5");
            pressure = main.getString("field4");
            humidity = main.getString("field2");
            created_at= main.getString("created_at");
            created_at = created_at.substring(0,10)+" "+created_at.substring(11,19);
            
            parsingComplete = false;



        } catch (Exception e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
        }

   }
   public void fetchJSON(){
      Thread thread = new Thread(new Runnable(){
         @Override
         public void run() {
         try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
         InputStream stream = conn.getInputStream();

      String data = convertStreamToString(stream);

      readAndParseJSON(data);
         stream.close();

         } catch (Exception e) {
            e.printStackTrace();
         }
         }
      });

       thread.start(); 		
   }
   static String convertStreamToString(java.io.InputStream is) {
      scanner = new java.util.Scanner(is);
	java.util.Scanner s = scanner.useDelimiter("\\A");
      return s.hasNext() ? s.next() : "";
   }
}