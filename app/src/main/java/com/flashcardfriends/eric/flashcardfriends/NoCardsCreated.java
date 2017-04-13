package com.flashcardfriends.eric.flashcardfriends;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NoCardsCreated extends AppCompatActivity {
    public final static String SET_NAME = "SET_NAME";
    public String setName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_cards_created);
        setName = this.getIntent().getStringExtra(SET_NAME).trim();
    }

    public void surething(View view) {
        Intent intent = new Intent(getApplicationContext(), Edit.class);
        intent.putExtra(SET_NAME, setName);
        startActivity(intent);
        this.finish();
    }
}
