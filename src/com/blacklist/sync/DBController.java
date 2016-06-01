package com.blacklist.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

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
}