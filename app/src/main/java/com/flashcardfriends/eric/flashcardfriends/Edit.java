package com.flashcardfriends.eric.flashcardfriends;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Edit extends AppCompatActivity {
    public final static String SET_NAME = "SET_NAME";
    public final static String POSITION = "POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final TextView setName = (TextView) findViewById(R.id.setName);
        final TextView textView = (TextView) findViewById(R.id.textView);
        final Button addNewCard = (Button) findViewById(R.id.activity_add_new_card);
        final Button deleteSet = (Button) findViewById(R.id.activity_delete_set);
        setName.setText(this.getIntent().getStringExtra(SET_NAME));


        String filename = "FlashcardFriends_" + this.getIntent().getStringExtra(SET_NAME).trim();
        String filetext = "";
        FileInputStream inputStream;

        try {
            inputStream = openFileInput(filename);
            filetext = IOUtils.toString(inputStream, "UTF-8");
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinearLayout linearLayout = new LinearLayout(this);
        ListView listView = new ListView(this);
        setContentView(linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        //Add "Editing Set" text to the linear layout
        ((ViewGroup)textView.getParent()).removeView(textView);
        linearLayout.addView(textView);

        //Add Set Name text to the linear layout
        ((ViewGroup)setName.getParent()).removeView(setName);
        linearLayout.addView(setName);

        //Inner LinearLayout for buttons
        LinearLayout linearLayoutButtons = new LinearLayout(this);
        linearLayoutButtons.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutButtons.setGravity(Gravity.CENTER_HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(25, 0, 25, 0);

        //Add "Delete Set" button to the linearLayoutButtons
        ((ViewGroup)deleteSet.getParent()).removeView(deleteSet);
        deleteSet.setLayoutParams(params);
        linearLayoutButtons.addView(deleteSet);


        //Add "Add New Card" button to the linearLayoutButtons
        ((ViewGroup)addNewCard.getParent()).removeView(addNewCard);
        addNewCard.setLayoutParams(params);
        linearLayoutButtons.addView(addNewCard);


        //Add buttons
        linearLayout.addView(linearLayoutButtons);


        ArrayList<String> setList = new ArrayList<>();

        if(!filetext.isEmpty()) {
            DocumentBuilder builder = null;
            try {
                builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(filetext));

            Document doc = null;
            try {
                doc = builder.parse(src);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(doc!=null) {
                for(int i = 0; i< doc.getElementsByTagName("Q").getLength(); i++){
                    String question = doc.getElementsByTagName("Q").item(i).getTextContent();
                    String answer = doc.getElementsByTagName("A").item(i).getTextContent();
                    setList.add("Q: " + question + "\n" + "A: " + answer);
                }
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, setList);
        listView.setAdapter(adapter);
        final String setname = this.getIntent().getStringExtra(SET_NAME);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditCard.class);
                intent.putExtra(SET_NAME, setname);
                intent.putExtra(POSITION, position);
                startActivityForResult(intent, 1);
            }
        });

        linearLayout.addView(listView);
    }

    public void addNewCard(View view) {
        Intent intent = new Intent(this, AddNewCard.class);
        intent.putExtra(SET_NAME, this.getIntent().getStringExtra(SET_NAME));
        startActivityForResult(intent, 1);
    }

    public void deleteSet(View view) throws IOException {
        new AlertDialog.Builder(this)
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete the entire flashcard set?")
                .setPositiveButton("Yeah, do it!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        File dir = getFilesDir();
                        String filename = "FlashcardFriends_" + Edit.this.getIntent().getStringExtra(SET_NAME).trim();
                        File file = new File(dir, filename);

                        if (!file.delete()) {
                            try {
                                throw new IOException("Unable to delete file: " + file.getAbsolutePath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Edit.this.finish();
                    }
                })
                .setNegativeButton("Woops. No!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void editSetName(View view) {
        Intent intent = new Intent(this, EditSetName.class);
        intent.putExtra(SET_NAME, this.getIntent().getStringExtra(SET_NAME));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, Edit.class);
            if(data!=null){
                refresh.putExtra(SET_NAME, data.getStringExtra(SET_NAME));
            } else {
                refresh.putExtra(SET_NAME, this.getIntent().getStringExtra(SET_NAME));
            }

            startActivity(refresh);
            this.finish();
        }
    }
}
