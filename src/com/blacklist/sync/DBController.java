package com.blacklist.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

import com.zkc.barcodescan.R;

/**
 * Created by c3rv30 on 18-05-16.
 * Clase DBController
 */

public class DBController extends SQLiteOpenHelper {

    public DBController(Context applicationcontext){
        super(applicationcontext, "user.db", null, 1);
    }
    
    
 // Create tables
    @Override
    public void onCreate(SQLiteDatabase database) {
    	
        String query;
        query = "CREATE TABLE users ( userId INTEGER, userName TEXT)";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS users";
        database.execSQL(query);
        onCreate(database);
    }

    public void deleteFromTable(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query;
        query = "DELETE FROM users;";
        database.execSQL(query);
    }
    
    /**
     * Inserts User into SQLite DB
     * @param queryValues
     */
    public void insertUser(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", queryValues.get("userId"));
        values.put("userName", queryValues.get("userName"));
        database.insert("users", null, values);
        database.close();
    }

    /**
     * Get list of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllUsers() {    	
        ArrayList<HashMap<String, String>> usersList;        
        usersList = new ArrayList<HashMap<String, String>>();        
        String selectQuery = "SELECT * FROM users";        
        SQLiteDatabase database = this.getWritableDatabase();        
        Cursor cursor = database.rawQuery(selectQuery, null);        
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", cursor.getString(0));
                map.put("userName", cursor.getString(1));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }    
    
    public String parametro(String rut){
    	String rut1 = rut;
    	return rut1;
    }
    
    
    
    /**
     * Get Rut of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getBlackUser(String envio) 
    {
    	String param = "";
    	param = envio;
    	
    	
        ArrayList<HashMap<String, String>> usersList;        
        usersList = new ArrayList<HashMap<String, String>>();        
        String selectQuery = "SELECT * FROM users WHERE userName = '"+param+"'";
        
        SQLiteDatabase database = this.getWritableDatabase();        
        Cursor cursor = database.rawQuery(selectQuery, null);        
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", cursor.getString(0));
                map.put("userName", cursor.getString(1));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;        
        }  
    
//    public DBController recuperarUsuarioNegro(String rut) {
//    	String rut_black = rut;
//        SQLiteDatabase db = getReadableDatabase();
//        String selectQuery = "SELECT * FROM users WHERE userName = '" + rut_black + "'";      
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        if(cursor != null) {
//            cursor.moveToFirst();
//        }        
//        Contactos contactos = new Contactos(c.getInt(0), c.getString(1), 
//                c.getInt(2), c.getString(3));
//        db.close();
//        c.close();
//        return contactos;
//    }
}











