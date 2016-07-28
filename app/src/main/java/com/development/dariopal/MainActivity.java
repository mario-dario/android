package com.development.dariopal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.development.dariopal.neura_manager.NeuraManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NeuraManager.getInstance().initNeuraConnection();
    }
}
