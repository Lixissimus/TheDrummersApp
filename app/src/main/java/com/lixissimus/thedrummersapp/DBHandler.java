package com.lixissimus.thedrummersapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "DrummersAppDBHandler";

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "drummersapp.db";
    public static final String TABLE_DRUMSETS = "drumsets";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DRUMS = "drums";
    public static final String COLUMN_FREQS = "freqs";


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_DRUMSETS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DRUMS + " TEXT, " +
                COLUMN_FREQS + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUMSETS);
        onCreate(db);
    }

    public void addDrumSet(DrumSet drumSet) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, drumSet.getConfigName());
        values.put(COLUMN_DRUMS, drumSet.getDrumsString());
        values.put(COLUMN_FREQS, drumSet.getFreqsString());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_DRUMSETS, null, values);
        db.close();
    }

    public void deleteDrumSet(String drumSetName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DRUMSETS + " WHERE " + COLUMN_NAME + "=\"" + drumSetName + "\";");
    }

    // return configurations as String
    public String drumSetsToString() {
        String drumSetString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DRUMSETS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndex(COLUMN_NAME));
            String toms = c.getString(c.getColumnIndex(COLUMN_DRUMS));
            String freqs = c.getString(c.getColumnIndex(COLUMN_FREQS));
            drumSetString += name + ": " + toms + " - " + freqs + "\n";
            c.moveToNext();
        }

        db.close();
        return drumSetString;
    }

    public void dropDrumSets() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUMSETS);
        onCreate(db);
    }

    public void seed() {
        // remove all drum sets first
        dropDrumSets();

        DrumSet gretsch = new DrumSet("Catalina Maple");
        gretsch.addDrum(new DrumSet.Drum("Snare", 350, 400));
        gretsch.addDrum(new DrumSet.Drum("Tom 1", 220, 230));
        gretsch.addDrum(new DrumSet.Drum("Tom 2", 140, 145));

        DrumSet sonor = new DrumSet("Sonor SQ2");
        sonor.addDrum(new DrumSet.Drum("Snare", 320, 324));
        sonor.addDrum(new DrumSet.Drum("Tom 1", 240, 240));
        sonor.addDrum(new DrumSet.Drum("Tom 2", 190, 195));
        sonor.addDrum(new DrumSet.Drum("Tom 3", 140, 140));

        addDrumSet(gretsch);
        addDrumSet(sonor);
    }
}
