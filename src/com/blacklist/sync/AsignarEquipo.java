package com.blacklist.sync;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zkc.barcodescan.R;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.media.MediaPlayer;


import com.blacklist.sync.DBController;


public class AsignarEquipo extends Activity {
	
	// DB Class to perform DB related operations
    DBController controller = new DBController(this);
    // Progress Dialog Object
    ProgressDialog prgDialog;
    HashMap<String, String> queryValues;
    public static EditText editTextIdDispositivo;
    
    
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_asignar_equipo);
        
        // Get User records from SQLite DB
        ArrayList<HashMap<String, String>> equiList = controller.getAllEquipoAsignado();		      
        // If users exists in SQLite DB
        if (equiList.size() != 0){
        	
        	Intent intent = new Intent(this, MostrarAsignacion.class);
            startActivity(intent);
            
        }else{             	
        	// Get User records from SQLite DB
            ArrayList<String> userList = controller.getAllEquipo2();
            // If users exists in SQLite DB
            if (userList.size() != 0) {
                // Set the User Array list in ListView         	
                //SimpleCursorAdapter adapter = new SimpleCursorAdapter(AsignarEquipo.this, userList, R.layout.activity_asignar_equipo, new String[] {"equipoNom" }, new int[] { R.id.text1});
                ArrayAdapter my_adapter = new ArrayAdapter(this, R.layout.spinner_row, userList);
                Spinner mySpinner = (Spinner)findViewById(R.id.EquipoSpinner);
                //ListView myList = (ListView) findViewById(android.R.id.list);              
                mySpinner.setAdapter(my_adapter);            
            }        	
        }
        
        
        
     // Initialize Progress Dialog properties
        prgDialog = new ProgressDialog(this);
        //prgDialog.setMessage("Transferring Data from Remote MySQL DB and Syncing SQLite. Please wait...");
        prgDialog.setMessage("Transfiriendo Datos del servidor remoto, espere...");
        prgDialog.setCancelable(false);
        // BroadCase Receiver Intent Object
        Intent alarmIntent = new Intent(getApplicationContext(), SampleBC.class);
        // Pending Intent Object
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Alarm Manager Object
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        // Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in
        // Remote MySQL DB
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 60 * 1000, pendingIntent);
        
        //getActionBar().setDisplayHomeAsUpEnabled(true);  
	}
	
	public void btnAsignarEquipo(View view){
		String dispId;
		editTextIdDispositivo = (EditText) findViewById(R.id.editTextIdDispositivo);
		dispId = editTextIdDispositivo.getText().toString();
		dispId.toString().trim();
		Spinner spinner = (Spinner)findViewById(R.id.EquipoSpinner);
		String nom = spinner.getSelectedItem().toString();
		controller.insertEquipoAsignado(nom, dispId);
		Intent intent = new Intent(this, MostrarAsignacion.class);
        startActivity(intent);
	}
	
	public void btnSyncEquipo(View v){		
		syncSQLiteToMySQLDBEquipo();			
	}
	
	// Method to Sync MySQL to SQLite DB table Equipo
    public void syncSQLiteToMySQLDBEquipo() {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        prgDialog.show();
        // Make Http call to getusers.php
        //client.post("http://192.168.1.139:8888/getusers.php", params, new AsyncHttpResponseHandler() {
        client.post("http://idcontrol.cc/sqlitemysqlsync/getequipo.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Hide ProgressBar
                prgDialog.hide();
                // Update SQLite DB with response sent by getusers.php
                updateSQLiteequipo(response);
                Toast.makeText(getApplicationContext(), "Lista de Equipo Actualizado", Toast.LENGTH_LONG).show();
            }
            // When error occured
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void updateSQLiteequipo(String response){
        ArrayList<HashMap<String, String>> usersynclist;
        usersynclist = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if(arr.length() != 0){
                // Loop through each array element, get JSON object which has userid and username
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("equipoId"));
                    System.out.println(obj.get("equipoNom"));
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValues.put("equipoId", obj.get("equipoId").toString());
                    // Add userName extracted from Object
                    queryValues.put("equipoNom", obj.get("equipoNom").toString());
                    // Insert User into SQLite DB
                    controller.insertEquipo(queryValues);
                    HashMap<String, String> map = new HashMap<String, String>();                    
                    // Add status for each User in Hashmap
                    map.put("Id", obj.get("equipoId").toString());
                    map.put("status", "1");
                    usersynclist.add(map);
                }
                // Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
                // Permite cambiar estado de sincronizacion de datos en servidor
                //updateMySQLSyncStsEquipo(gson.toJson(usersynclist));
                // Reload the Main Activity
                reloadActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
 // Method to inform remote MySQL DB about completion of Sync activity
    public void updateMySQLSyncStsEquipo(String json) {
        System.out.println(json);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("syncsts", json);
        // Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
        client.post("http://idcontrol.cc/sqlitemysqlsync/updatesyncstsequipo.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                //Toast.makeText(getApplicationContext(), "MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "El Servidor a sido informado de la sincronizacion", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
            	//Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            	Toast.makeText(getApplicationContext(), "Ocurrio un Error", Toast.LENGTH_LONG).show();
            }
        });
    }
    
    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), AsignarEquipo.class);
        startActivity(objIntent);
    }
    
}













