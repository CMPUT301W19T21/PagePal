package ca.team21.pagepal;

import org.junit.Test;

import java.util.ArrayList;

import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.BookList;

import static org.junit.Assert.assertEquals;


public class BookListTest {
    private BookList bookList = new BookList();
    ArrayList<Book> testList = new ArrayList<Book>();
    Book book1 = new Book();
    Book book2 = new Book();

    @Test
    public void addBook() {
        testList.add(book1);
        testList.add(book2);

        bookList.add(book1);
        bookList.add(book2);
        assertEquals(testList, bookList.get());
    }

    @Test
    public void delBook() {
        testList.remove(book1);
        bookList.remove(book1);

        assertEquals(testList, bookList.get());
    }
}
