package com.example.meteots;


import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
	
	private String url1 = "http://api.thingspeak.com/channels/27167/feed.json?offset=1&results=1&round=1";
	private TextView temperatureIn,temperatureOut,humidity,pressure,date;
	private HandleJSON obj;
	private WebView webView;
	private boolean conn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupToolbar();
		 getFeeds();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}*/
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_settings:
            	return true;
               
            case R.id.action_refresh:
            	getFeeds();
   	           
               break;
        }
        return super.onOptionsItemSelected(item);
    }
	
	
	 private void setupToolbar(){
	        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
	        setSupportActionBar(toolbar);
	    // Show menu icon
	        final ActionBar ab = getSupportActionBar();
	        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
	        ab.setDisplayHomeAsUpEnabled(true);
	    }
	 
	 public void getFeeds() {
		 conn=isNetworkAvailable();
		 if (conn==false){
				Toast.makeText(getApplicationContext(), "brak po³¹czenia z internetem", Toast.LENGTH_SHORT).show();
				return;
			}
		  temperatureOut  = (TextView)findViewById(R.id.oTempVal);
	        pressure = (TextView)findViewById(R.id.pressVal);
	        humidity = (TextView)findViewById(R.id.iHummVal);
	        temperatureIn = (TextView)findViewById(R.id.iTempVal);
	        date = (TextView)findViewById(R.id.date);
	        
	        String finalUrl = url1;
	        
	        obj = new HandleJSON(finalUrl);
	        obj.fetchJSON();

	        while(obj.parsingComplete);
	        temperatureOut.setText(obj.getTemperatureOut());
	        temperatureIn.setText(obj.getTemperatureIn());
	        humidity.setText(obj.getHumidity().substring(0, 2));
	        date.setText(obj.getDate());
	        pressure.setText(obj.getPressure()); 

	        // Font path
	        String fontPath = "digital-7m.ttf";

	        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

	        temperatureOut.setTypeface(tf);  
	        temperatureIn.setTypeface(tf);
	        humidity.setTypeface(tf);
	        pressure.setTypeface(tf);
	        
	        webView = (WebView) findViewById(R.id.webView1);
	        webView.getSettings().setJavaScriptEnabled(true);
	        webView.loadUrl("https://thingspeak.com/plugins/9575");
	        //webView.setBackgroundColor(Color.TRANSPARENT); 
	        webView.setBackgroundColor(0x00000000);
	  } 
	 
	 public boolean isNetworkAvailable() {
		    ConnectivityManager cm = (ConnectivityManager) 
		      getSystemService(this.CONNECTIVITY_SERVICE);
		    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		    // if no network is available networkInfo will be null
		    // otherwise check if we are connected
		    if (networkInfo != null && networkInfo.isConnected()) {
		        return true;
		    }
		    return false;
		} 
}
