package com.flashcardfriends.eric.flashcardfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class NoSetsCreated extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_sets_created);
    }

    public void surething(View view) {
        Intent intent = new Intent(getApplicationContext(), Create.class);
        startActivity(intent);
        this.finish();
    }

}
