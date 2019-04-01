package ca.team21.pagepal;

import org.junit.Test;

import java.util.ArrayList;

import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.HistoryItem;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the book history list
 */

//public class HistoryItemTest {
//
//    private HistoryItem historyItem;
//    private Book book1;
//    private Book book2;
//    private ArrayList<Book> testList;
//
//    /**
//     * Tests adding a book
//     */
//    @Test
//    public void addBookTest() {
//        historyItem = new HistoryItem();
//        book1 = new Book();
//        book2 = new Book();
//        testList = new ArrayList<>();
//
//        assertEquals(testList, historyItem.getHistoryList());
//
//        historyItem.addBook(book1);
//        testList.add(book1);
//        assertEquals(testList, historyItem.getHistoryList());
//
//        historyItem.addBook(book2);
//        testList.add(book2);
//        assertEquals(testList, historyItem.getHistoryList());
//
//    }
//
//    /**
//     * Tests removing a book
//     */
//    @Test void removeBookTest() {
//        historyItem = new HistoryItem();
//        book1 = new Book();
//        book2 = new Book();
//        testList = new ArrayList<>();
//
//        historyItem.addBook(book1);
//        historyItem.addBook(book2);
//        historyItem.removeBook(book1);
//
//        testList.add(book1);
//
//        assertEquals(testList, historyItem.getHistoryList());
//
//    }
//}
