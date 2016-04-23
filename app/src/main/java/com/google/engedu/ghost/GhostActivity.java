package com.google.engedu.ghost;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.regex.Matcher;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = true;
    private String word_fragment;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        userTurn = false;
        word_fragment = new String();
        onStart(null);

        try {
            dictionary = new SimpleDictionary(getAssets().open("words.txt"));
        }
        catch (IOException file){
            Log.d("File", "Unable to open file");
        }

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_UP){

            char letter = event.getDisplayLabel();
            if(!Character.isDigit(letter)){
                Log.d("word", word_fragment);
                word_fragment += letter;
                word_fragment = word_fragment.toLowerCase();
                if(dictionary.isWord(word_fragment))
                {
                    TextView label = (TextView) findViewById(R.id.gameStatus);
                    label.setText("Player Wins");
                }
                else {

                    TextView ghost_text = (TextView) findViewById(R.id.ghostText);
                    ghost_text.setText(word_fragment);
                    userTurn = false;
                    computerTurn();
                }
                return super.onKeyUp(keyCode, event);
            }
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {

        Log.d("Computer turn", "something");
        TextView label = (TextView) findViewById(R.id.ghostText);
        TextView user = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again

        if(TextUtils.isEmpty(word_fragment)){
            label.setText("");
        }
        else if(dictionary.isWord(word_fragment)){
            TextView text_view = (TextView) findViewById(R.id.gameStatus);

            text_view.setText("Computer Wins");
        }

        else{
            //since the word is not a fragment, we need to create a new word
            String word = dictionary.getAnyWordStartingWith(word_fragment);

            if(!TextUtils.isEmpty(word))
            {
                word_fragment += word.charAt(word.length()-1);
                userTurn = true;
                user.setText(USER_TURN);
                label.setText(word_fragment);
            }
            else //this means that there is no valid word available
            {

                user.setText("Computer Wins");
                String correct_word = dictionary.getGoodWordStartingWith(word_fragment);
                if(TextUtils.isEmpty(correct_word   ))
                    label.setText(word_fragment);
                else
                    label.setText(correct_word);
                /*
                int ascii = (int) (Math.random() * 26) + 97;
                char character = (char) ascii;
                word_fragment += character;
                */
            }



        }


        return;
    }

    public void ChallengeButton(View view){

        TextView score = (TextView) findViewById(R.id.gameStatus);

        //it is a valid word
        if(word_fragment.length() <= 4 && dictionary.isWord(word_fragment)){
            //the player wins
            score.setText("Player Wins");
            return;
        }

        String possible_word = dictionary.getGoodWordStartingWith(word_fragment);

        if(!TextUtils.isEmpty(possible_word)){
            score.setText("Computer Wins");
            TextView label = (TextView) findViewById(R.id.ghostText);
            label.setText(possible_word);
        }
        else {
            score.setText("Player Wins");
            TextView label = (TextView) findViewById(R.id.ghostText);
            label.setText(possible_word);
        }
    }

    public void restart(View view){
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        word_fragment = "";
        TextView user = (TextView) findViewById(R.id.gameStatus);
        user.setText(USER_TURN);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
}
