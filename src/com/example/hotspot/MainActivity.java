package com.example.hotspot;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {
	private WifiManager wifiManager;
	private Button open;
	private boolean flag = false;
	
	//
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get wifiManager service
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        open=(Button)findViewById(R.id.open_hotspot);
        //set the hotspot
        open.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag=!flag;
				setWifiApEnabled(flag);
			}
		});
        
        //设置定时修改ssid
        new Timer().schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0x111);
			}
        	
        }, 0,50);
                
        
        
    }
    
    //wifi switch
    public boolean setWifiApEnabled(boolean enabled){
    	if(enabled){
    		//disable WiFi in any case
    		wifiManager.setWifiEnabled(false);
    	}
    	try{
    		WifiConfiguration apConfig = new WifiConfiguration();
    		apConfig.SSID = "memeda";
    		apConfig.preSharedKey="xiaoliubo";
    		Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class,Boolean.TYPE);
    		return (Boolean) method.invoke(wifiManager, apConfig,enabled);
    	}catch(Exception e){
    		return false;
    	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}







