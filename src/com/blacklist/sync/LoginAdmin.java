package com.blacklist.sync;

import com.zkc.barcodescan.R;
import com.zkc.barcodescan.activity.MainActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class LoginAdmin extends Activity {
	public static EditText EditUserAdmin;
	public static EditText editTextPassAdmin;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_admin);
        
        EditUserAdmin = (EditText) findViewById(R.id.EditUserAdmin);
        editTextPassAdmin = (EditText) findViewById(R.id.editTextPassAdmin);
        EditUserAdmin.setText("rarratia");
        editTextPassAdmin.setText("Rupi2213");
        
        
        
	}
	
	public void btnLog(View view){
		String userEdit, passEdit, user, pass;
		userEdit = EditUserAdmin.getText().toString().trim();
		passEdit = editTextPassAdmin.getText().toString().trim();
		user = "rarratia";
		pass = "Rupi2213";
		if(userEdit.isEmpty() || passEdit.isEmpty()){
			Toast toast = Toast.makeText(this, "Debe Ingresar los datos", Toast.LENGTH_SHORT);
	        toast.show();			
		}else{
			if(user.equals(userEdit) && pass.equals(passEdit)){
				
				Intent intent = new Intent(this, AsignarEquipo.class);
		        startActivity(intent);
		        finish();
		        
			}else{
				Toast toast = Toast.makeText(this, "Usuario o Contrase�a Incrrectos", Toast.LENGTH_SHORT);
		        toast.show();
			}			
		}				
	}
}