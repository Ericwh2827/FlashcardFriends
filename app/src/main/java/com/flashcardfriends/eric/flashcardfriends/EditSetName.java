package com.flashcardfriends.eric.flashcardfriends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class EditSetName extends AppCompatActivity {
    public final static String SET_NAME = "SET_NAME";
    public String setName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_set_name);
        setName = this.getIntent().getStringExtra(SET_NAME).trim();
        EditText edit_set_name = (EditText) findViewById(R.id.edit_set_name);
        edit_set_name.setText(setName);
    }

    public void update(View view) throws IOException, SAXException, ParserConfigurationException {

        EditText edit_set_name = (EditText) findViewById(R.id.edit_set_name);

        if(edit_set_name.getText().toString().trim().equals("")){
            edit_set_name.setError( "Please enter set name!" );
        } else {
            String filename = "FlashcardFriends_" + this.getIntent().getStringExtra(SET_NAME).trim();

            String filenamenew = "FlashcardFriends_" + edit_set_name.getText().toString().trim();

            String filetext = "";
            FileInputStream inputStream;
            FileOutputStream outputStream;


            inputStream = openFileInput(filename);
            filetext = IOUtils.toString(inputStream, "UTF-8");
            inputStream.close();


            outputStream = openFileOutput(filenamenew, Context.MODE_PRIVATE);


            //Write Flashcards tag
            outputStream.write(("<Flashcards>").getBytes(Charset.forName("UTF-8")));

            //Add previous questions to file
            if (!filetext.isEmpty()) {
                DocumentBuilder builder = null;
                builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

                InputSource src = new InputSource();
                src.setCharacterStream(new StringReader(filetext));

                Document doc = null;

                doc = builder.parse(src);

                if (doc != null) {
                    for (int i = 0; i < doc.getElementsByTagName("Q").getLength(); i++) {
                        String oldQuestion = "<Q>" + doc.getElementsByTagName("Q").item(i).getTextContent() + "</Q>";
                        String oldAnswer = "<A>" + doc.getElementsByTagName("A").item(i).getTextContent() + "</A>";
                        outputStream.write((oldQuestion + oldAnswer).getBytes(Charset.forName("UTF-8")));
                    }
                }
            }

            //Close Flashcards tag
            outputStream.write(("</Flashcards>").getBytes(Charset.forName("UTF-8")));
            outputStream.close();

            //Delete old set
            File dir = getFilesDir();
            filename = "FlashcardFriends_" + this.getIntent().getStringExtra(SET_NAME).trim();
            File file = new File(dir, filename);

            if (!file.delete()) {
                throw new IOException("Unable to delete file: " + file.getAbsolutePath());
            }

            Intent intent=new Intent();
            intent.putExtra(SET_NAME, edit_set_name.getText().toString().trim());
            setResult(RESULT_OK, intent);
            this.finish();
        }
    }
}
