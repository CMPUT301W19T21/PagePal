package ca.team21.pagepal;

import org.junit.Test;

import ca.team21.pagepal.models.Book;

import static ca.team21.pagepal.models.Book.REQUESTED;
import static org.junit.Assert.assertEquals;

/**
 * Tests the book class
 */
public class BookTest {
    private Book book = new Book();

    /**
     * Tests setting the title
     */
    @Test
    public void setTitleTest() {
        String title = "Brave New World";

        book.setTitle(title);
        assertEquals(title, book.getTitle());
    }

    /**
     * Tests setting the author
     */
    @Test
    public void setAuthorTest() {
        String author = "Aldous Huxley";

        book.setAuthor(author);
        assertEquals(author, book.getAuthor());
    }

    /**
     * Tests setting the description
     */
    @Test
    public void setDescTest() {
        String desc = "Futuristic novel";

        book.setDescription(desc);
        assertEquals(desc, book.getDescription());
    }

    /**
     * Tests setting the status
     */
    @Test
    public void setStatusTest() {
        book.setStatus(REQUESTED);

        assertEquals(REQUESTED, book.getStatus());
    }

    @Test
    public void setOwnerTest() {
        book.setOwner("tan");

        assertEquals("tan", book.getOwner());
    }
}
