package ca.team21.pagepal.models;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A list of recommendations generated based on a user's history of loans and what they liked and disliked
 * (This is our WOW feature)
 */
public class RecommendationList {
    private BookHistoryList history;
    private Location location;
    private ArrayList<Book> recommendedBooks;

    /**
     * Instantiates a new Recommendation list.
     *
     * @param history  the history
     * @param location the location
     */
    public RecommendationList(BookHistoryList history, Location location) {
        this.history = history;
        this.location = location;
    }

    /**
     * Generate a list of recommendations.
     */
    public void generateList() {
        GenreDictionary z = new GenreDictionary();

        HashMap<String, Integer> genreScore = z.getGenreDictionary();

        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(genreScore.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String,Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        HashMap<String, Integer> temporary = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> placeholder : list) {
            temporary.put(placeholder.getKey(), placeholder.getValue());
        }

        for (Map.Entry<String, Integer> SortedGenre : temporary.entrySet()){

        }

    }

    /**
     * Get the recommendation list
     *
     * @return the recommendation list
     */
    public ArrayList<Book> get() {

        return recommendedBooks; }
}
