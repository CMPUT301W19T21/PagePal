package ca.team21.pagepal;

import org.junit.Test;

import java.util.ArrayList;

import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.BookHistoryList;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the book history list
 */
public class BookHistoryListTest {

    private BookHistoryList bookHistoryList;
    private Book book1;
    private Book book2;
    private ArrayList<Book> testList;

    /**
     * Tests adding a book
     */
    @Test
    public void addBookTest() {
        bookHistoryList = new BookHistoryList();
        book1 = new Book();
        book2 = new Book();
        testList = new ArrayList<>();

        assertEquals(testList, bookHistoryList.getHistoryList());

        bookHistoryList.addBook(book1);
        testList.add(book1);
        assertEquals(testList, bookHistoryList.getHistoryList());

        bookHistoryList.addBook(book2);
        testList.add(book2);
        assertEquals(testList, bookHistoryList.getHistoryList());

    }

    /**
     * Tests removing a book
     */
    @Test void removeBookTest() {
        bookHistoryList = new BookHistoryList();
        book1 = new Book();
        book2 = new Book();
        testList = new ArrayList<>();

        bookHistoryList.addBook(book1);
        bookHistoryList.addBook(book2);
        bookHistoryList.removeBook(book1);

        testList.add(book1);

        assertEquals(testList, bookHistoryList.getHistoryList());

    }
}
