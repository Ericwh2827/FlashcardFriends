package com.flashcardfriends.eric.flashcardfriends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.FileOutputStream;

public class Create extends AppCompatActivity {
    public final static String SET_NAME = "SET_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_create);
        layout.addView(textView);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_flashcard:
                if (checked)
                    // Flashcards mode is selected
                    break;
            case R.id.radio_multiple_choice:
                if (checked)
                    // Multiple choice mode is selected
                    break;
        }
    }

    public void submit(View view) {
        EditText setName = (EditText) findViewById(R.id.edit_set_name);

        if(setName.getText().toString().trim().equals("")){
            setName.setError( "Please enter a set name!" );
        } else {

            String filename = "FlashcardFriends_" + setName.getText().toString().trim();
            String string = "<Flashcards></Flashcards>";
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(string.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(this, Edit.class);
            intent.putExtra(SET_NAME, setName.getText().toString().trim());
            startActivity(intent);
            this.finish();
        }
    }
}
