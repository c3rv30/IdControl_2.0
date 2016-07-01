package com.blacklist.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Editable;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
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
    	
        String query, query2;
        query = "CREATE TABLE users ( userId INTEGER, userName TEXT)";
        database.execSQL(query);
        query2 = "CREATE TABLE estadisticas ( rut TEXT, fecha TEXT)";
        database.execSQL(query2);
    }    
    

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        String query, query2;
        query = "DROP TABLE IF EXISTS users";
        database.execSQL(query);
        query2 = "DROP TABLE IF EXISTS estadisticas";
        database.execSQL(query2);
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

    public void insertRutEstadisticas(String rut, String fecha) {
    	//String date = setDate.setDateScaner();
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("rut", rut.toString().trim());
        values.put("fecha", fecha);
        database.insert("estadisticas", null, values);
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
   
 
    /**
     * Get Rut of Users from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getBlackUser(String receptor){    	
    	String rut = receptor;
    	rut.toString().replace(" ", "");
    	//String rut = "17107682k";    	
        ArrayList<HashMap<String, String>> usersList;        
        usersList = new ArrayList<HashMap<String, String>>();        
        String selectQuery = "SELECT * FROM users WHERE userName = '"+rut+"'";        
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
    
    public ArrayList<HashMap<String, String>> getAllAsistentes(String fecha) {    	
        ArrayList<HashMap<String, String>> usersList;        
        usersList = new ArrayList<HashMap<String, String>>();        
        String selectQuery = "SELECT * FROM estadisticas WHERE fecha = '"+fecha+"'";        
        SQLiteDatabase database = this.getWritableDatabase();        
        Cursor cursor = database.rawQuery(selectQuery, null);        
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("rut", cursor.getString(0));
                map.put("fecha", cursor.getString(1));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }          
}











