package ca.team21.pagepal;

import android.provider.MediaStore;

import java.util.HashMap;


public class GenreDictionary {
    private String genre;
    private HashMap<String, Integer> genreScore = new HashMap<String, Integer>();



    public HashMap<String, Integer> getGenreDictionary(){

        //note THAT google api has volumeInfo.categories[] which  is a list of subject categories such as
        //fiction, suspense, etc
        //Object value = genreScore.get(genre);

        Object value = genreScore.get(genre);
        //If it does have the genre already in it i want to just increment once
        if (value != null){
            int count = genreScore.containsKey(genre) ? genreScore.get(genre) : 0;
            genreScore.put(genre, count + 1);
        }
        //if it does not have the key i want to create a key to that genre and start with 1
        else{
            genreScore.put(genre, 1);
        }



        //genreScore.put("Fiction", 1);
        //genreScore.put("Mystery", 2);
        ///genreScore.put("History", 3);
        return genreScore;
    }
}

