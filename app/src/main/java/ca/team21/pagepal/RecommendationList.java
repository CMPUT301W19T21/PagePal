package ca.team21.pagepal;

import android.location.Location;

import java.util.ArrayList;

import ca.team21.pagepal.Book.Book;
import ca.team21.pagepal.Book.BookHistoryList;

public class RecommendationList {
    private BookHistoryList history;
    private Location location;
    private ArrayList<Book> recommendedBooks;

    public RecommendationList(BookHistoryList history, Location location) {
        this.history = history;
        this.location = location;
    }

    public void generateList() {

    }

    public ArrayList<Book> get() { return recommendedBooks; }
}
