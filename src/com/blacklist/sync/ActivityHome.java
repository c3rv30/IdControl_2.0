package com.blacklist.sync;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import com.zkc.barcodescan.R;
import com.zkc.barcodescan.activity.MainActivity;

import android.app.Activity;

public class ActivityHome extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
	}
	
	//Button Scan activity
    public void btnScan(View v){
    	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
    
  //Button Scan activity
    public void btnStadistic(View v){
        
    }
    
  //Button Scan activity
    public void btnSync(View v){
        
    }
	
}
