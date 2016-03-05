package com.lixissimus.thedrummersapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "DrummersAppDBHandler";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "drummersapp.db";
    public static final String TABLE_CONFIGURATIONS = "configurations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TOMS = "toms";


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_CONFIGURATIONS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_TOMS + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIGURATIONS);
        onCreate(db);
    }

    public void addConfiguration(Configuration configuration) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, configuration.get_name());
        values.put(COLUMN_TOMS, configuration.get_tomsString());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_CONFIGURATIONS, null, values);
        db.close();
    }

    public void deleteConfiguration(String configName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CONFIGURATIONS + " WHERE " + COLUMN_NAME + "=\"" + configName + "\";");
    }

    // return configurations as String
    public String configurationsToString() {
        Log.d(TAG, "start conversion");
        String configString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CONFIGURATIONS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndex(COLUMN_NAME));
            String toms = c.getString(c.getColumnIndex(COLUMN_TOMS));
            configString += name + ": " + toms + "\n";
            c.moveToNext();
        }

        db.close();
        Log.d(TAG, "done conversion");
        return configString;
    }

    public void dropConfigurations() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIGURATIONS);
        onCreate(db);
    }

    public void seed() {
        // remove all configurations first
        dropConfigurations();

        int[] toms1 = {220, 340, 480};
        Configuration c1 = new Configuration("Set 1", toms1);
        addConfiguration(c1);

        int[] toms2 = {200, 310, 400, 520};
        Configuration c2 = new Configuration("Set 2", toms2);
        addConfiguration(c2);
    }
}
