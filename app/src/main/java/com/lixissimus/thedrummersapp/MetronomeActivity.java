package com.lixissimus.thedrummersapp;

import android.os.Bundle;

public class MetronomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);
        super.onCreateDrawer();
    }

}
