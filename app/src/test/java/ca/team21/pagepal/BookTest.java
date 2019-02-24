package ca.team21.pagepal;

import org.junit.Test;

import java.io.File;

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
    public void setIsbnTest() {
        String isbn = " 0062696122";

        book.setISBN(isbn);
        assertEquals(isbn, book.getISBN());
    }

    @Test
    public void setDescTest() {
        String desc = "Futuristic novel";

        book.setDescription(desc);
        assertEquals(desc, book.getDescription());
    }

    @Test
    public void setPhotoTest() {
        Cover_Photo img = new Cover_Photo(new File("default.png"));

        book.setPhoto(img);
        assertEquals(img, book.getPhoto());
    }
}
