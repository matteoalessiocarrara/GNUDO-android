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
        DbHelper.Task t = db.getTasks().add("Titolo", "Descrizione", 10L, 20L, false);

        Log.d("Titolo", t.getTitle());
        Log.d("Completato", t.isCompleted().toString());
        t.setTitle("Titolo modificato");
        Log.d("Titolo", t.getTitle());

        db.getTasks().remove(t.getId());
    }

}
