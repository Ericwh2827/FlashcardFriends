package com.flashcardfriends.eric.flashcardfriends;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TestFlashcards extends AppCompatActivity {
    public final static String SET_NAME = "SET_NAME";
    public String setNameString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_flashcards);
        final TextView setName = (TextView) findViewById(R.id.setName);
        final TextView question_answer_text = (TextView) findViewById(R.id.question_answer_text);
        question_answer_text.setGravity(Gravity.CENTER_HORIZONTAL);

        setNameString = this.getIntent().getStringExtra(SET_NAME);
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

        final ArrayList<Pair<String, String>> questionAnswerList = new ArrayList<Pair<String, String>>();

        if (!filetext.isEmpty()) {
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
            if (doc != null) {
                for (int i = 0; i < doc.getElementsByTagName("Q").getLength(); i++) {
                    String question = doc.getElementsByTagName("Q").item(i).getTextContent();
                    String answer = doc.getElementsByTagName("A").item(i).getTextContent();
                    Pair<String, String> quesitonAnswer = new Pair<String, String>(question, answer);
                    questionAnswerList.add(quesitonAnswer);
                }
                if (!questionAnswerList.isEmpty()) {
                    question_answer_text.setText(questionAnswerList.get(0).first);
                }
            }
        }

        if (questionAnswerList.isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), NoCardsCreated.class);
            intent.putExtra(SET_NAME, setNameString);
            startActivity(intent);
            finish();
        } else {
            ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.activity_test_flashcards);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                final TextView question_or_answer = (TextView) findViewById(R.id.question_or_answer);
                int currentCard = 0;
                boolean question = false;

                @Override
                public void onClick(View v) {
                    if (currentCard >= questionAnswerList.size()) {
                        currentCard = 0;
                    }
                    if (question) {
                        question_or_answer.setText("Question: ");
                        question_or_answer.setTextColor(Color.RED);
                        question_answer_text.setText(questionAnswerList.get(currentCard).first);
                        question_answer_text.setTextColor(Color.RED);
                        question = false;
                    } else {
                        question_or_answer.setText("Answer: ");
                        question_or_answer.setTextColor(Color.BLUE);
                        question_answer_text.setText(questionAnswerList.get(currentCard).second);
                        question_answer_text.setTextColor(Color.BLUE);
                        currentCard++;
                        question = true;
                    }
                }

            });
        }
    }
}
