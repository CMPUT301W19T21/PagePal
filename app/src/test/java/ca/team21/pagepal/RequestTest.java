package ca.team21.pagepal;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RequestTest {

    private Profile owner = new Profile();
    private Profile requester = new Profile();
    private Book book = new Book();
    private Request request = new Request(owner, requester, book);

    @Test
    public void setLocationTest() {
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);

        request.setLocation(loc);
        assertEquals(loc, request.getLocation());
    }
}