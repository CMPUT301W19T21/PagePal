package ca.team21.pagepal.models;

import java.util.HashMap;

public class GenreDictionary {
    private String genre;
    private HashMap<String, Integer> genreScore = new HashMap<String, Integer>();

    public HashMap<String, Integer> getGenreDictionary(){

        //Note that google api has volumeInfo.categories[] which is a list of subjects categories such as fiction, suspense, etc

        Object value = genreScore.get(genre);

        if (value != null){
            int count = genreScore.containsKey(genre) ? genreScore.get(genre) : 0;
            genreScore.put(genre, count + 1);
        }

        else{
            genreScore.put(genre, 0);

        }
        return genreScore;
    }
}
