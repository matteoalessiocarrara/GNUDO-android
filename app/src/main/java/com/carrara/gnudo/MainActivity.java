package com.carrara.gnudo;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.carrara.gnudo.DbHelper;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper db = DbHelper.getInstance(this);
        db.getTasks().add("Titolo", "Descrizione", 10L, 20L, false);
        Log.d("README", db.getTasks().getIdList().toString());
    }

}
