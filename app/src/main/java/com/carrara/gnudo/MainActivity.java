package com.carrara.gnudo;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.carrara.gnudo.DbHelper;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper db = DbHelper.getInstance(this);
        db.getReadableDatabase();
    }

}
