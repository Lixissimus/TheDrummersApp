package com.lixissimus.thedrummersapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TunerContentProvider {

    private static final String DRUMSET_NAME = "Sonor SQ2";

    private DrumSet drumSet;

    public TunerContentProvider(Context context) {
        DBHandler dbHandler = new DBHandler(context, null, null, 1);
        drumSet = dbHandler.getDrumSet(DRUMSET_NAME);
    }

    public List<DrumSet.Drum> getDrumsList() {
        return new ArrayList<>(getDrumsMap().values());
    }

    public Map<String, DrumSet.Drum> getDrumsMap() {
        return drumSet.getDrums();
    }
}
