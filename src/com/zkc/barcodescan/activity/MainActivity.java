package com.zkc.barcodescan.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.media.MediaPlayer;


import com.blacklist.sync.DBController;
import com.blacklist.sync.ActivityEstadisticas;
import com.blacklist.sync.MainActivitySync;
import com.zkc.Service.CaptureService;
import com.zkc.barcodescan.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.serialport.api.SerialPort;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private ScanBroadcastReceiver scanBroadcastReceiver;
	Button btnOpen, btnEdit;
	public static EditText et_code;
	private Button emptyBtn;
	
	private Calendar calendar;
	private TextView fecha;
	private int year, month, day;
	
	//ImageView img = (ImageView)findViewById(R.id.imageView);
	

	List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
	
	// DB Class to perform DB related operations
    DBController controller = new DBController(this);
    
    // Date Class
    ActivityEstadisticas setDate = new ActivityEstadisticas();
  

	/*
	 * private byte[] choosedData = new byte[] { 0x07, (byte) 0xC6, 0x04, 0x08,
	 * 0x00, (byte) 0x9C, 0x0A, (byte) 0xFE, (byte) 0x81 };// �޸�һά������115200
	 * private byte[] choosedData2 = new byte[] { 0x07, (byte) 0xC6, 0x04, 0x08,
	 * 0x00, (byte) 0x9C, 0x0A, (byte) 0xFE, (byte) 0x81 };// �޸Ķ�ά������115200
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_barcode_main);

		et_code = (EditText) findViewById(R.id.et_code);
		
		//et_code.setText("");
		// �˳�
		btnEdit = (Button) findViewById(R.id.btnEdit);
		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exitActivity();
			}
		});
		// �����Ϣ
		emptyBtn = (Button) findViewById(R.id.emptyBtn);
		emptyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_code.setText("");
			}
		});
		// ����ɨ��
		btnOpen = (Button) findViewById(R.id.btnOpen);
		btnOpen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SerialPort.CleanBuffer();
				CaptureService.scanGpio.openScan();
			}

		});

		Intent newIntent = new Intent(MainActivity.this, CaptureService.class);
		newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(newIntent);

		getOverflowMenu();

		scanBroadcastReceiver = new ScanBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.zkc.scancode");
		this.registerReceiver(scanBroadcastReceiver, intentFilter);
		
		// Bar Bacl layout
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		// Set Date
		//fecha = (TextView)findViewById(R.id.textView4);
    	calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);		
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);							
	}

	private void getOverflowMenu() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, 1, 1, R.string.action_1d);
		menu.add(0, 2, 2, R.string.action_2d);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if (item.getItemId() == 1) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ActivityBarcodeSetting.class);
			startActivity(intent);
			return super.onOptionsItemSelected(item);
		} else if (item.getItemId() == 2) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, ActivityQrcodeSetting.class);
			startActivity(intent);
			return super.onOptionsItemSelected(item);
		}
		finish();
		return true;
		
	}

	@Override
	protected void onResume() {
		System.out.println("onResume" + "open");
		Log.v("onResume", "open");
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		exitActivity();
	}

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(scanBroadcastReceiver);
		super.onDestroy();
	}

	private void exitActivity() {
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.popup_title)
				.setMessage(R.string.popup_message)
				.setPositiveButton(R.string.popup_yes,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								CaptureService.scanGpio.closeScan(); // �رյ�Դ
								CaptureService.scanGpio.closePower();

								finish();
							}
						}).setNegativeButton(R.string.popup_no, null).show();
	}
	
	public void prueba1() {
        Toast toast = Toast.makeText(this, "Es Igual", Toast.LENGTH_SHORT);
        toast.show();        
    }
	
	public void sonido(){
		MediaPlayer mp = MediaPlayer.create(this, R.raw.valid_beep);
    	mp.start();
	}
	
	public String date(){
		fecha.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));
    	String date = fecha.toString().trim();
    	return date;		
	}
	
	public void insertAsis(String rut){
		String envRut = rut;
		String envFecha = date();
		HashMap<String, String> queryValues = new HashMap<String, String>();
		queryValues.put("rut", envRut.toString().trim());
		queryValues.put("fecha", envFecha.toString().trim());
		controller.insertRutEstadisticas(queryValues);	
	}
	
	public

	@SuppressLint("DefaultLocale")
	class ScanBroadcastReceiver extends BroadcastReceiver {
		@SuppressLint("DefaultLocale")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String text = intent.getExtras().getString("code");			
			String rut = text;			
			String sSubcadena = rut.substring(0,5);			
			String newCed = "https";
			String igual;
			String sinGuion;
			String pasar;
			
			if(sSubcadena.equals(newCed)){
				igual = text.substring(52,62);
				sinGuion = igual.replace("-", "");
				pasar = sinGuion.toLowerCase();
			}else{
				sinGuion = text.substring(0,9);
				pasar = sinGuion.replace(" ", "").toLowerCase();										
		    }
			// Get User records from SQLite DB
	        ArrayList<HashMap<String, String>> userList = controller.getBlackUser(pasar);		      
	        // If users exists in SQLite DB
	        if (userList.size() != 0){
	        	Log.i(TAG, "MyBroadcastReceiver code: " + pasar);	     
	        	et_code.setText("AL FIN CTM!!");
	        	sonido();
	        	insertAsis(pasar);
	        	//img.setImageResource(R.drawable.valid_image);	 
	        		        	
	        }else{
	        	et_code.setText("puta la wea");
	        }				
		}
	}
}