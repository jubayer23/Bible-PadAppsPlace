package com.biblepadllc.biblepadappsplacestore;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    public void nextPage(View view){

        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

    public void exit(View view){

        finish();

    }
}
