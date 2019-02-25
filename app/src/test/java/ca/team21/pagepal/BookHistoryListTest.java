package ca.team21.pagepal;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BookHistoryListTest {

    private BookHistoryList bookHistoryList;
    private Book book1;
    private Book book2;
    private ArrayList<Book> testList;

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
