package com.flashcardfriends.eric.flashcardfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

public class PlayOptions extends AppCompatActivity {
    public final static String SET_NAME = "SET_NAME";
    public String setName = "";

    public final static String FLASHCARD_MODE = "FLASHCARD_MODE";
    public String flashcardMode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_options);
        setName = this.getIntent().getStringExtra(SET_NAME).trim();
        flashcardMode="Practice";
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_practice:
                if (checked)
                    // Practice mode is selected
                    flashcardMode="Practice";
                    break;
            case R.id.radio_test:
                if (checked)
                    // Test mode is selected
                    flashcardMode="Test";
                    break;
        }
    }

    public void play(View view) {
        if(flashcardMode.equals("Practice")){
            Intent intent = new Intent(this, PracticeFlashcards.class);
            intent.putExtra(SET_NAME, setName);
            startActivity(intent);
        } else if(flashcardMode.equals("Test")){
            Intent intent = new Intent(this, TestFlashcards.class);
            intent.putExtra(SET_NAME, setName);
            startActivity(intent);
        }
    }
}
