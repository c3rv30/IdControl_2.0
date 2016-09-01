package com.blacklist.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.zkc.barcodescan.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MostrarAsignacion extends Activity {
	
	TextView equipo;
	TextView dispId;
	TextView textViewprueba;
	
	// DB Class to perform DB related operations
    DBController controller = new DBController(this);
    HashMap<String, String> queryValues;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mostrar_equipo);
        
        equipo = (TextView) findViewById(R.id.textViewEquipo);
        dispId = (TextView) findViewById(R.id.textViewIdDisp);
        
        // Get User records from SQLite DB
        //SArrayList<HashMap<String, String>> equipList = controller.getAllEquipoAsignado();
        
        //HashMap<String, String> equipList2 = controller.getAllEquipoAsignadoNew();
        
        String equipo1 = controller.getEquipoAsignado();
        String ID = controller.getIdDispAsignado();
        
        // If users exists in SQLite DB
        if (equipo1.length() != 0) {        	
        	
        	equipo.setText(equipo1.toString());
        	dispId.setText(ID.toString());        	
        	/*Iterator<?> it = equipList2.entrySet().iterator();        	
        	while(it.hasNext()){
        		@SuppressWarnings("rawtypes")
				Map.Entry e = (Map.Entry)it.next();
        		equipo.setText(e.getKey().toString());
        		dispId.setText(e.getValue().toString());
        	}*/
        	
        	/*for (HashMap<String, String> hashMap : equipList){        		
        		System.out.println(hashMap.keySet());
        		equipo.setText(hashMap.keySet().toString());        
        		
        		for(String key : hashMap.keySet()){
        			System.out.println(hashMap.get(key));
        			//Sequipo.setText(hashMap.get(key).toString());
        			dispId.setText(hashMap.get(key).toString());
        		}        		
        	}*/  	
        }    
	}	
	
	public void btnDesEquipo(View view){		
		controller.vaciarEquipoAsignado();		
	}
}







