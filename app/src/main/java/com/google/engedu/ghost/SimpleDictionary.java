package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        //we need to use the prefix we get and see whether it can be found within any word in the dictionary

        if(prefix.isEmpty()){
            int random_index = (int) (Math.random() * words.size());
            return words.get(random_index);
        }

        Comparator<String> c = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                if(lhs.startsWith(rhs))
                    return 0;
                else if(rhs.startsWith(lhs)){
                    return 0;
                }
                else {
                    return lhs.compareTo(rhs);
                }

            }
        };

        int index_of_word = Collections.binarySearch(words, prefix, c);

        if(index_of_word < 0){
            return null;
        }
        else
        {
            String word_found = words.get(index_of_word);
            if(word_found.length() >= prefix.length()) {
                String new_word = prefix + word_found.charAt(prefix.length());
                return new_word;
            }
            return null;
        }

        /*

        while(startint_index <= startint_index && !words.get(current_index).startsWith(prefix))){

            if(words.get(current_index).substring(0, prefix.length() + 1).compareTo(prefix) > 0){

            }
        }

        if(words.get(current_index).startsWith(prefix))

        */
        //return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        if(prefix.isEmpty()){
            int random_index = (int) (Math.random() * words.size());
            return words.get(random_index);
        }

        Comparator<String> c = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                if(lhs.startsWith(rhs))
                    return 0;
                else if(rhs.startsWith(lhs)){
                    return 0;
                }
                else {
                    return lhs.compareTo(rhs);
                }

            }
        };

        int index_of_word = Collections.binarySearch(words, prefix, c);

        if(index_of_word < 0){
            return null;
        }
        else
        {
            String word_found = words.get(index_of_word);
            return word_found;
        }

    }
}
