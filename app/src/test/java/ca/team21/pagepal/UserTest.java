package ca.team21.pagepal;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class UserTest {
    private User user = new User();

    @Test
    public void setUsernameTest() {
        String uName = "TheGreatOne99";

        user.setUsername(uName);
        assertEquals(uName, user.getUsername());
    }

    @Test
    public void setLocationTest() {
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);

        user.setLocation(loc);
        assertEquals(loc, user.getLocation());
    }

    @Test
    public void addRequests() {
        User owner = new User();
        User borrower = new User();
        Book book1 = new Book();
        book1.setTitle("Great Gatsby");
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);
        
        Request one = new Request(owner, borrower, book1, loc);
        
        Book book2 = new Book();
        book2.setTitle("Huckleberry Finn");

        Request two = new Request(owner, borrower, book2, loc);
        ArrayList<Request> testList = new ArrayList<Request>();
        testList.add(one);
        testList.add(two);

        user.addRequest(one);
        user.addRequest(two);

        assertEquals(testList, user.getRequestList());
    }

    @Test
    public void addBooks() {
        Book book1 = new Book();
        book1.setTitle("Great Gatsby");

        Book book2 = new Book();
        book2.setTitle("Huckleberry Finn");

        ArrayList<Book> testList = new ArrayList<>();
        testList.add(book1);
        testList.add(book2);

        user.addOwnedBook(book1);
        user.addOwnedBook(book2);

        assertEquals(testList, user.getOwnedList());
    }
}
