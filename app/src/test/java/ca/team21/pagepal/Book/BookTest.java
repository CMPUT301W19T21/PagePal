package ca.team21.pagepal.Book;

import org.junit.Test;

import ca.team21.pagepal.Book.Book;

import static ca.team21.pagepal.Book.Book.REQUESTED;
import static org.junit.Assert.assertEquals;

public class BookTest {
    private Book book = new Book();

    @Test
    public void setTitleTest() {
        String title = "Brave New World";

        book.setTitle(title);
        assertEquals(title, book.getTitle());
    }

    @Test
    public void setAuthorTest() {
        String author = "Aldous Huxley";

        book.setAuthor(author);
        assertEquals(author, book.getAuthor());
    }

    @Test
    public void setDescTest() {
        String desc = "Futuristic novel";

        book.setDescription(desc);
        assertEquals(desc, book.getDescription());
    }

    @Test
    public void setStatusTest() {
        book.setStatus(REQUESTED);

        assertEquals(REQUESTED, book.getStatus());
    }
}
