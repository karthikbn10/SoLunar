package com.example.android.sunandmoon;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karthik on 10/16/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DB_PATH = "";
    public static String DB_NAME = "us_cities_with_timezones.sl3";
    public static final int DB_VERSION = 1;

    public static final String TB_USER = "cities";

    private SQLiteDatabase myDB;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

    @Override
    public synchronized void close() {
        if (myDB != null) {
            myDB.close();
        }
        super.close();
    }

    public List<String> getAllUsers() {
        List<String> listUsers = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;

        try {
            int numRows = (int) DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM " + TB_USER, null);
            int count = 0;
            c = db.rawQuery("SELECT name,state FROM " + TB_USER, null);
            if (c == null) return null;

            String name;
            String state;
            StringBuilder stringBuilder = new StringBuilder("");

            c.moveToFirst();
            do {

                name = c.getString(0);
                state = c.getString(1);
                stringBuilder.append(name + "," + state);
                listUsers.add(stringBuilder.toString());
                stringBuilder = new StringBuilder("");
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            Log.e("tle99", e.getMessage());
        }


        db.close();

        return listUsers;
    }


    /***
     * Open database
     */
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    /***
     * Copy database from source code assets to device
     * @throws IOException
     */
    public void copyDataBase() throws IOException{
        try {
            InputStream myInput = context.getAssets().open(DB_NAME);
            String outputFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;

            while((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.e("tle99 - copyDatabase", e.getMessage());
        }

    }



    /***
     * Check if the database doesn't exist on device, create new one
     * @throws IOException
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if (dbExist) {

        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e("tle99 - create", e.getMessage());
            }
        }
    }



    // ---------------------------------------------
    // PRIVATE METHODS
    // ---------------------------------------------

    /***
     * Check if the database is exist on device or not
     *
     */
    private boolean checkDataBase() {
        SQLiteDatabase tempDB = null;
        try {

            if(android.os.Build.VERSION.SDK_INT >= 17){
                DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
            }
            else
            {
                DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
            }
            String myPath = DB_PATH + DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.e("tle99 - check", e.getMessage());
        }
        if (tempDB != null)
            tempDB.close();
        return tempDB != null ? true : false;
    }

    public String[] getInfo(String[] info)

    {
        String name = info[0];
        String state = info[1];
        String[] result=  new String[3];

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;

        try {
                String nm = "name";
                String st = "state";
            //c = db.rawQuery("SELECT latitude, longitude, time_zone FROM " + TB_USER + " WHERE name =" + name + " AND state = " + state , null);
            c = db.rawQuery("SELECT * FROM " + TB_USER + " WHERE "+nm+ "='" + name + "' AND " + st + "='" + state+"'" , null);
            //c= db.query(TB_USER,null,nm+"="+name + " AND " + st + "=" + state ,null,null,null,null,null);
            if (c == null) return null;



            c.moveToFirst();


                result[0] = c.getString(2);
                result[1] = c.getString(3);
                result[2] = c.getString(4);

            c.close();
        } catch (Exception e) {
            Log.e("tle99", e.getMessage());
        }


        db.close();


        return  result;

    }
}

