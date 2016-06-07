package com.blacklist.sync;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import com.zkc.barcodescan.R;
import com.zkc.barcodescan.activity.MainActivity;

import android.app.Activity;

import com.blacklist.sync.MainActivitySync;

public class ActivityHome extends Activity {
	
	MainActivitySync sync = new MainActivitySync();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
	}
	
	//Button Scan activity
    public void btnScan(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    
  //Button Estadistics
    public void btnStadistic(View v){
        
    }
    
  //Button Sync DB
    public void btnSync(){
    	sync.syncDB(); 	        
    }	
}
