package ca.team21.pagepal;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class BookListTest {

    private Profile owner = new Profile();
    private Book book1 = new Book();
    private Book book2 = new Book();
    private Book book3 = new Book();
    private Book_List bookList = new Book_List(owner);
    private ArrayList<Book> testArray = new ArrayList<>();

    @Test
    public void addBookTest() {

        bookList.addBook(book1);
        testArray.add(book1);
        assertEquals(testArray, bookList.getBookList());

        bookList.addBook(book2);
        testArray.add(book2);
        assertEquals(testArray, bookList.getBookList());

        bookList.addBook(book3);
        testArray.add(book3);
        assertEquals(testArray, bookList.getBookList());

    }

    @Test
    public void removeBookTest() {

        bookList.addBook(book1);
        bookList.removeBook(book1);
        assertEquals(testArray, bookList.getBookList());

        bookList.addBook(book1);
        bookList.removeBook(book2); // book doesn't exist in list, list should not be changed
        testArray.add(book1);
        assertEquals(testArray, bookList.getBookList());

    }
}
