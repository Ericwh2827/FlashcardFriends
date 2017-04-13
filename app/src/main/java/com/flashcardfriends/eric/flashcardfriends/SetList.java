package com.flashcardfriends.eric.flashcardfriends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class SetList extends AppCompatActivity {
    public final static String SET_NAME = "SET_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_list);
        Context context = this.getApplicationContext();

        File pathToFiles = new File(context.getFilesDir().getPath());

        String[] files = pathToFiles.list();

        final TextView editingSet = (TextView) findViewById(R.id.textView);

        LinearLayout linearLayout = new LinearLayout(this);
        ListView listView = new ListView(this);
        setContentView(linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        //Add "Editing Set" text to the linear layout
        ((ViewGroup)editingSet.getParent()).removeView(editingSet);
        linearLayout.addView(editingSet);

        ArrayList<String> setList = new ArrayList<>();

        for(int i=0; i<files.length; i++){
            if(files[i].length()>17 && files[i].substring(0,17).equals("FlashcardFriends_")){
                setList.add(files[i].substring(17));
            }
        }

        if(setList.isEmpty()){
            Intent intent = new Intent(getApplicationContext(), NoSetsCreated.class);
            startActivity(intent);
            finish();
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, setList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String setName = String.valueOf(parent.getItemAtPosition(position));
                        Intent intent = new Intent(getApplicationContext(), Edit.class);
                        intent.putExtra(SET_NAME, setName);
                        startActivity(intent);
                        finish();
                    }
                });

        linearLayout.addView(listView);
    }
}
