package com.blacklist.sync;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.content.Intent;

import com.zkc.barcodescan.R;
import com.zkc.barcodescan.activity.MainActivity;

import android.app.Activity;

import com.blacklist.sync.MainActivitySync;

public class ActivityHome extends Activity {
	
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        
	}
	
	//Button Scan activity
    public void btnScan(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    
  //Button Estadistics
    public void btnStadistic(View view){
    	Intent intent = new Intent(this, ActivityEstadisticas.class);
    	startActivity(intent);        
    }
    
  //Button Sync DB
    public void btnSync(View view){
    	Intent intent = new Intent(this, MainActivitySync.class);
        startActivity(intent);    	        
    }	
}
