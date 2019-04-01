package ca.team21.pagepal;

import org.junit.Test;

import java.util.ArrayList;

import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.HistoryItem;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the book history list
 */

public class HistoryItemTest {

    private HistoryItem historyItem;
    private ArrayList<Book> testList;

    /**
     * Tests adding a book
     */
    @Test
    public void addBookTest() {
        historyItem = new HistoryItem();

        historyItem.setBookHisTitle("The Hobbit");
        historyItem.setBookHisAuthor("Tolkien");
        historyItem.setBookHisGenre("Fantasy");

        assertEquals("The Hobbit", historyItem.getBookHisTitle());
        assertEquals("Tolkien", historyItem.getBookHisAuthor());
        assertEquals("Fantasy", historyItem.getBookHisGenre());

    }
}
