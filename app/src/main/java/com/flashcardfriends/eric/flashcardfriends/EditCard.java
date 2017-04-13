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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class EditCard extends AppCompatActivity {
    public final static String SET_NAME = "SET_NAME";
    public String setName = "";
    public final static String POSITION = "POSITION";
    public int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        setName = this.getIntent().getStringExtra(SET_NAME).trim();
        position = this.getIntent().getIntExtra(POSITION, 0);
        String filename = "FlashcardFriends_" + this.getIntent().getStringExtra(SET_NAME).trim();

        FileInputStream inputStream = null;
        try {
            inputStream = openFileInput(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String filetext = "";

        try {
            filetext = IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String question = "";
        String answer = "";

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
                    if(i==position){
                        question = doc.getElementsByTagName("Q").item(i).getTextContent();
                        answer = doc.getElementsByTagName("A").item(i).getTextContent();
                    }
                }
            }
        }

        final EditText edit_question = (EditText) findViewById(R.id.edit_question);
        edit_question.setText(question);

        final EditText edit_answer = (EditText) findViewById(R.id.edit_answer);
        edit_answer.setText(answer);

    }

    public void update(View view) throws IOException, SAXException, ParserConfigurationException {

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
                        if(i!=position){
                            String oldQuestion = "<Q>" + doc.getElementsByTagName("Q").item(i).getTextContent() + "</Q>";
                            String oldAnswer = "<A>" + doc.getElementsByTagName("A").item(i).getTextContent() + "</A>";
                            outputStream.write((oldQuestion + oldAnswer).getBytes(Charset.forName("UTF-8")));
                        } else {
                            //Replace old question with edited question to file
                            String question = "<Q>" + questionText.getText().toString().trim() + "</Q>";
                            String answer = "<A>" + answerText.getText().toString().trim() + "</A>";
                            outputStream.write((question + answer).getBytes(Charset.forName("UTF-8")));
                        }

                    }
                }
            }

            //Close Flashcards tag
            outputStream.write(("</Flashcards>").getBytes(Charset.forName("UTF-8")));
            outputStream.close();

            setResult(RESULT_OK, null);
            this.finish();
        }
    }

    public void delete(View view) throws IOException, SAXException, ParserConfigurationException {

        EditText questionText = (EditText) findViewById(R.id.edit_question);
        EditText answerText = (EditText) findViewById(R.id.edit_answer);

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
                    //Replace everything but current position QA
                    if(i!=position){
                        String oldQuestion = "<Q>" + doc.getElementsByTagName("Q").item(i).getTextContent() + "</Q>";
                        String oldAnswer = "<A>" + doc.getElementsByTagName("A").item(i).getTextContent() + "</A>";
                        outputStream.write((oldQuestion + oldAnswer).getBytes(Charset.forName("UTF-8")));
                    }
                }
            }
        }

        //Close Flashcards tag
        outputStream.write(("</Flashcards>").getBytes(Charset.forName("UTF-8")));
        outputStream.close();

        setResult(RESULT_OK, null);
        this.finish();
    }
}
