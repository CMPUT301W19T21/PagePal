package ca.team21.pagepal;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import ca.team21.pagepal.models.Book;
import ca.team21.pagepal.models.Request;
import ca.team21.pagepal.models.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Tests the request class
 */
public class RequestTest {

    private User owner = new User();
    private User requester = new User();
    private Book book = new Book();
    private Request request = new Request(owner.getUsername(), requester.getUsername(), book);

    @Test
    public void testSetBook() {
        request.setBook(book);

        assertEquals(book, request.getBook());
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
    @Test
    public void setLocationTest() {


        Double lat = 53.47769792;
        Double lon = -113.51072995;

        request.setLatitude(lat);
        request.setLongitude(lon);
        assertEquals(lat, request.getLatitude());
        assertEquals(lon, request.getLongitude());
    }

    /**
     * Tests the borrower ready booleans
     */
    @Test
    public void setReady() {
        assertFalse(request.isOwnerReady());
        assertFalse(request.isBorrowerReady());

        request.setOwnerReady(true);
        request.setBorrowerReady(true);

        assertTrue(request.isOwnerReady());
        assertTrue(request.isBorrowerReady());
    }

}
