package com.flashcardfriends.eric.flashcardfriends;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AddNewCard extends AppCompatActivity {
    public final static String SET_NAME = "SET_NAME";
    public String setName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_card);
        setName = this.getIntent().getStringExtra(SET_NAME).trim();
    }

    public void submit(View view) throws IOException, SAXException, ParserConfigurationException {

        EditText questionText = (EditText) findViewById(R.id.edit_question);
        EditText answerText = (EditText) findViewById(R.id.edit_answer);

        if(questionText.getText().toString().trim().equals("")){
            questionText.setError( "Please enter question text!" );
        } if(answerText.getText().toString().trim().equals("")){
            questionText.setError( "Please enter answer text!" );
        } else {
            String filename = "FlashcardFriends_" + this.getIntent().getStringExtra(SET_NAME).trim();

            String filetext = "";
            FileInputStream inputStream;
            FileOutputStream outputStream;


            inputStream = openFileInput(filename);
            filetext = IOUtils.toString(inputStream, "UTF-8");
            inputStream.close();


            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);

            //Write Flashcards tag
            outputStream.write(( "<Flashcards>").getBytes(Charset.forName("UTF-8")));

            //Add previous questions to file
            if(!filetext.isEmpty()) {
                DocumentBuilder builder = null;
                builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

                InputSource src = new InputSource();
                src.setCharacterStream(new StringReader(filetext));

                Document doc = null;

                    doc = builder.parse(src);

                if(doc!=null) {
                    for(int i = 0; i< doc.getElementsByTagName("Q").getLength(); i++){
                        String oldQuestion = "<Q>" + doc.getElementsByTagName("Q").item(i).getTextContent() + "</Q>";
                        String oldAnswer = "<A>" + doc.getElementsByTagName("A").item(i).getTextContent() + "</A>";
                        outputStream.write((oldQuestion + oldAnswer).getBytes(Charset.forName("UTF-8")));
                    }
                }
            }

            //Add new question to file
            String question = "<Q>" + questionText.getText().toString().trim() + "</Q>";
            String answer = "<A>" + answerText.getText().toString().trim() + "</A>";

            //Close Flashcards tag
            outputStream.write((question + answer + "</Flashcards>").getBytes(Charset.forName("UTF-8")));

            outputStream.close();


            setResult(RESULT_OK, null);
            this.finish();
        }
    }
}
