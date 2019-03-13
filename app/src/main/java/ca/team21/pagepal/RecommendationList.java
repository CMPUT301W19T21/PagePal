package ca.team21.pagepal;

import android.location.Location;

import java.util.ArrayList;

import ca.team21.pagepal.Book.Book;
import ca.team21.pagepal.Book.BookHistoryList;

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

    }

    /**
     * Get the recommendation list
     *
     * @return the recommendation list
     */
    public ArrayList<Book> get() { return recommendedBooks; }
}
