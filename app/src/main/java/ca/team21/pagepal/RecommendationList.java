package ca.team21.pagepal;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RecommendationList {
    private BookHistoryList history;
    private Location location;
    private ArrayList<Book> recommendedBooks;



    public RecommendationList(BookHistoryList history, Location location) {
        this.history = history;
        this.location = location;


    }

    public void generateList() {
        GenreDictionary z = new GenreDictionary();


        HashMap<String, Integer> genreScore = z.getGenreDictionary();


        //Create a list from elements of hashmap
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(genreScore.entrySet());

        //sort the List
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        //put data from sorted list back into hashmap

        HashMap<String, Integer> temporary = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> placeholder : list) {
            temporary.put(placeholder.getKey(), placeholder.getValue());
        }

        //This for loop with go through sorted genre and then we find books???
        for (Map.Entry<String, Integer> SortedGenre : temporary.entrySet()){



            // first item of for loop we check out that genre with all the books with similar genre in this area
            //the next check is the one with the biggest user reccomendations score


            //Note VolumeInfo.averageRating (double) the mean review rating for this volume (min = 1.0, max 5.0)
            //Note VolumeInfo.ratingsCount (integer) the number of review ratings for this volume




        }

    }

    public ArrayList<Book> get(){
        return recommendedBooks; }
}
