package com.zkc.barcodescan.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.media.MediaPlayer;

import com.blacklist.sync.ActivityHome;
import com.blacklist.sync.DBController;
import com.blacklist.sync.MainActivitySync;
import com.blacklist.sync.MostrarAsignacion;
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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.serialport.api.SerialPort;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
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
	public TextView nomList;
	//private Button emptyBtn;
	
	/*private Calendar calendar;
	private TextView fecha;
	private int year, month, day;*/
	
	ImageView checkvalid;	
	List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
	
	// DB Class to perform DB related operations
    DBController controller = new DBController(this);
    
    private Calendar calendar;
	private int year, month, day;
    
    // Date Class
    //ActivityEstadisticas setDate = new ActivityEstadisticas();  

	/*
	 * private byte[] choosedData = new byte[] { 0x07, (byte) 0xC6, 0x04, 0x08,
	 * 0x00, (byte) 0x9C, 0x0A, (byte) 0xFE, (byte) 0x81 };// �޸�һά������115200
	 * private byte[] choosedData2 = new byte[] { 0x07, (byte) 0xC6, 0x04, 0x08,
	 * 0x00, (byte) 0x9C, 0x0A, (byte) 0xFE, (byte) 0x81 };// �޸Ķ�ά������115200
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_main_two);		
		checkvalid = (ImageView)findViewById(R.id.checkImage);
		et_code = (EditText) findViewById(R.id.et_code);	
		nomList = (TextView)findViewById(R.id.nomList);
		
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
		//emptyBtn = (Button) findViewById(R.id.emptyBtn);
//		emptyBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				et_code.setText("");
//			}
//		});
		// ����ɨ��
		btnOpen = (Button) findViewById(R.id.btnOpen);
		btnOpen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SerialPort.CleanBuffer();
				CaptureService.scanGpio.openScan();
				clean(arg0);
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
		//menu.add(0, 1, 1, R.string.action_1d);
		//menu.add(0, 2, 2, R.string.action_2d);
		menu.add(0, 1, 1, R.string.asign_team);
		//menu.add(0, 2, 2, R.string.edit_team_id);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		if (item.getItemId() == 1) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, com.blacklist.sync.LoginAdmin.class);
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
	
	public void clean(View v){
		et_code.setText("");
		checkvalid.setImageDrawable(null);
	}
	
	public void msgPass(String pasar) {
        Toast toast = Toast.makeText(this, "PUEDE INGRESAR", Toast.LENGTH_SHORT);
        toast.show();        
    }
	
	public void msgNoPass() {
        Toast toast = Toast.makeText(this, "NO PUEDE INGRESAR", Toast.LENGTH_SHORT);
        toast.show();        
    }
	
	public void msgingreso() {
        Toast toast = Toast.makeText(this, "RUT YA REGISTRADO PARA HOY", Toast.LENGTH_SHORT);
        toast.show();        
    }
	
	public void msgInvalidRut() {
        Toast toast = Toast.makeText(this, "Rut Invalido Escanear Nuevamente", Toast.LENGTH_SHORT);
        toast.show();        
    }
	
	public void beepPass(){
		MediaPlayer mp = MediaPlayer.create(this, R.raw.valid_beep);
    	mp.start();
	}
	
	public void beepNoPass(){
		//MediaPlayer mp = MediaPlayer.create(this, R.raw.beep_no_pass);
		MediaPlayer mp = MediaPlayer.create(this, R.raw.denied_beep_v2);
    	mp.start();
	}
	
	public void vaciarCasilla(View v){
		et_code.setText("");			
	}
	
	public void manualButton(View v){		
		String pasar;
		pasar = et_code.getText().toString().trim();
		if(pasar.isEmpty()){
			Toast toast = Toast.makeText(this, "DEBE INGRESAR UN RUT", Toast.LENGTH_SHORT);
	        toast.show();
		}else{
			et_code.setText("");
			boolean b = validarRut(pasar);			
			if(b == true){
				validarAsis(pasar);
				System.out.println(b);
			}else{				
				SerialPort.CleanBuffer();
				CaptureService.scanGpio.openScan();
				clean(v);
			}
		}
	}
	
	public void getRut(String pasar){		
		// Get User records from SQLite DB
        ArrayList<HashMap<String, String>> userList = controller.getBlackUser(pasar);		      
        // If users exists in SQLite DB
        if (userList.size() != 0){
        	for (int a = 0; a<userList.size();a++){        		
        		HashMap<String, String> tmpData = (HashMap<String, String>) userList.get(a);
        		Set<String> key = tmpData.keySet();
        		Iterator it = key.iterator();
        		while (it.hasNext()){
        			String hmKey = (String)it.next();
        			String hmData = (String)tmpData.get(hmKey);        			
        			System.out.println("Key: "+hmKey +" & Data: "+hmData);
        			nomList.setText("Numero de Lista: "+hmData);
        		}
        	}
        	beepNoPass();
        	msgNoPass();
        	checkvalid.setImageResource(R.drawable.invalid_check);     
        	et_code.setText(pasar);
        	//Log.i(TAG, "MyBroadcastReceiver code: " + pasar);	
        }else{
        	nomList.setText("");
        	calendar = Calendar.getInstance();
    		year = calendar.get(Calendar.YEAR);		
    		month = calendar.get(Calendar.MONTH);
    		day = calendar.get(Calendar.DAY_OF_MONTH);
    		
    		String year2;
    		year2 = Integer.toString(year);
    		String month2;
    		month2 = Integer.toString(month+1);
    		String day2;
    		day2 = Integer.toString(day);
    		
    		String fecha;
    		fecha = day2+"/"+month2+"/"+year2;
    		fecha.trim().toString();	    		
        	et_code.setText(pasar);
        	beepPass();
        	msgPass(pasar);        	
        	checkvalid.setImageResource(R.drawable.valid_check);
        	
        	String equipo = controller.getEquipoAsignado();
            String ID = controller.getIdDispAsignado();
            
            equipo.toString().trim();
            ID.toString().trim();
        	
        	//controller.insertRutEstadisticas(pasar, fecha);
        	controller.insertRutEstadisticas(pasar, fecha, equipo, ID);
        }
	}
	
	public void validarAsis(String pasar){		
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);		
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		
		String year2;
		year2 = Integer.toString(year);
		String month2;
		month2 = Integer.toString(month+1);
		String day2;
		day2 = Integer.toString(day);
		
		String fecha;
		fecha = day2+"/"+month2+"/"+year2;
		fecha.trim().toString();
		
		// Get User records from SQLite DB
        ArrayList<HashMap<String, String>> userList = controller.getAsisEstadis(pasar, fecha);		      
        // If users exists in SQLite DB
        if (userList.size() != 0){
        	//beepNoPass();
        	msgingreso();
        	checkvalid.setImageResource(R.drawable.warning_check);     
        	et_code.setText(pasar);
        	//Log.i(TAG, "MyBroadcastReceiver code: " + pasar);	
        }else{
        	getRut(pasar);
        }		
	}
	
	public static boolean validarRut(String rut) {
		boolean validacion = false;
		try {
			rut = rut.toUpperCase();
			rut = rut.replace(".", "");
			rut = rut.replace("-", "");
			int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));
			char dv = rut.charAt(rut.length() - 1);
			int m = 0, s = 1;
			for (; rutAux != 0; rutAux /= 10) {
				s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
			}
			if (dv == (char) (s != 0 ? s + 47 : 75)) {
				validacion = true;			
			}
		} catch (java.lang.NumberFormatException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
		return validacion;		
	}
	
	@SuppressLint("DefaultLocale")
	public class ScanBroadcastReceiver extends BroadcastReceiver {
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
				igual.replace("-", "").replace("&", "").toLowerCase();
				et_code.setText(igual.replace("-", "").replace("&", "").toLowerCase());
				sinGuion = et_code.getText().toString();
				pasar = sinGuion;
			}else{
				sinGuion = text.substring(0,9);
				pasar = sinGuion.replace(" ", "").toUpperCase();
		    }			
						
			boolean b = validarRut(pasar);
			if(b == true){
				validarAsis(pasar);
				System.out.println(b);
			}else{
				System.out.println(b);
			}
		}
	}
}//Cierre clase