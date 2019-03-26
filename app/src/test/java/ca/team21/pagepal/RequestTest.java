package ca.team21.pagepal;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.Request;
import ca.team21.pagepal.models.User;

import static org.junit.Assert.assertEquals;


/**
 * Tests the request class
 */
public class RequestTest {

    private User owner = new User();
    private User requester = new User();
    private Book book = new Book();
    private Request request;

    @Test
    public void testSetBook() {
        String isbn = "13564987";
        request.setBook(isbn);

        assertEquals(isbn, request.getBook());
    }

    @Test
    public void testSetOwner() {
        String username = "Wayne Gretzky";
        request.setOwner(username);

        assertEquals(username, request.getOwner());
    }

    @Test
    public void testSetRequester() {
        String username = "Connor McDavid";
        request.setRequester(username);

        assertEquals(username, request.getRequester());
    }

    /**
     * Tests setting the location
     */
    /*
    @Test
    public void setLocationTest() {
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);

        request.setLocation(loc);
        assertEquals(loc, request.getLocation());
    }
    */
}
