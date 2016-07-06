package com.blacklist.sync;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.zkc.barcodescan.R;
import com.blacklist.sync.DBController;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityEstadisticas extends Activity {
	
	DBController controller = new DBController(this);
	private DatePicker datePicker;
	private Calendar calendar;
	private TextView dateView, estadisticas;
	private int year, month, day;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_estadisticas);
		
		dateView = (TextView)findViewById(R.id.textView3);
		estadisticas = (TextView)findViewById(R.id.textViewAsis);
		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);		
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		showDate(year, month+1, day);	
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        // When Sync action button is clicked
        /*if (id == R.id.refresh) {
            // Transfer data from remote MySQL DB to SQLite on Android and perform Sync
            controller.deleteFromTable();
            syncSQLiteMySQLDB();
            return true;
        }*/
        //return super.onOptionsItemSelected(item);
        finish();
        return true;
    }
	
	@SuppressWarnings("deprecation")
	public void setDate(View view){
		showDialog(999);
		Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
	}
	
	protected Dialog onCreateDialog(int id){
		// TODO Auto-generated method stub
		if(id == 999){
			return new DatePickerDialog(this, myDateListener, year, month, day);
		}
		return null;		
	}
	
	private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			// arg1 = year
			// arg2 = month
			// arg3 = day
			showDate(arg1, arg2+1, arg3);			
		}
	};
	
	private void showDate(int year, int month, int day){
		dateView.setText(new StringBuilder().append(day).append("/")
				.append(month).append("/").append(year));		
	}	
	
	
	public String setDateScaner(){		
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
		return fecha;
	}	
	
	public void resEstadisticas(View view){
		String pasFecha;
		pasFecha = dateView.getText().toString().trim();
		estadisticas.setText("");
		// Get User records from SQLite DB
        ArrayList<HashMap<String, String>> userList = controller.getAllAsistentes(pasFecha);		      
        // If users exists in SQLite DB
        if (userList.size() != 0){
        	String asis = Integer.toString(userList.size());
        	estadisticas.append("Total Asistentes es: " + asis);
        }else{
        	estadisticas.setText("No existen Datos");
        }
		
	}	
	
}














