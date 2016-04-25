package com.example.hotspot;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import com.example.hotspot.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements SensorEventListener{
	LocationManager lm;
	SensorManager sm;
	private float direction;
	private WifiManager wifiManager;
	private Button open;
	private boolean flag = false;
	WifiConfiguration apConfig = new WifiConfiguration();
	TextView tv1;
	
	//执行定时修改ssid
	Handler handler = new Handler(){
		public void handleMessage(Message msg){
			if(msg.what==0x111){
				//modify ssid at regular time
				setWifiApEnabled(flag);
			}
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //get wifiManager service
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        open=(Button)findViewById(R.id.open_hotspot);
        tv1=(TextView)findViewById(R.id.tv1);
        //switch the hotspot and change ssid regularly.
        open.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag=!flag;
				setWifiApEnabled(flag);
				
				//设置定时器，修改ssid
		        new Timer().schedule(new TimerTask(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						handler.sendEmptyMessage(0x111);
					}        	
		        }, 0,1000);
			}
		});
        
        
    }
    
    //wifi switch
    public boolean setWifiApEnabled(boolean enabled){
    	if(enabled){
    		//disable WiFi in any case so that we could use hotspot.
    		wifiManager.setWifiEnabled(false);
    	}
    	try{    		
    		apConfig.SSID = "方向角为："+direction;
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
    
    @Override
    protected void onResume(){
    	super.onResume();

    	sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_FASTEST);
    	sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_FASTEST);
    	sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),SensorManager.SENSOR_DELAY_FASTEST);
    	sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),SensorManager.SENSOR_DELAY_UI);
    	sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),SensorManager.SENSOR_DELAY_UI);
    	sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	sm.unregisterListener(this);
    }

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		int type=event.sensor.getType();
		float[] values=event.values;
		switch(type){
		case Sensor.TYPE_ORIENTATION:
			direction=values[0];
		}
	}
}

	







