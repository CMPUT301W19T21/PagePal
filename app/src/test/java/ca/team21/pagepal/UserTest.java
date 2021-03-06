package ca.team21.pagepal;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import java.util.ArrayList;

import ca.team21.pagepal.models.User;

import static org.junit.Assert.assertEquals;

/**
 * Tests the user class
 * TODO re-add getters and setters to User
 */
public class UserTest {
    private User user = new User();

    @Test
    public void testSetUsername() {
        String uName = "TheGreatOne99";

        user.setUsername(uName);
        assertEquals(uName, user.getUsername());
    }

    @Test
    public void testSetEmail() {
        String email = "thegr81@oil.yeg";

        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testSetUid() {
        String uid = "ABC123";
        user.setUid(uid);

        assertEquals(uid, user.getUid());
    }
    /*
    @Test
    public void testSetLocation() {
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);

        user.setLocation(loc);
        assertEquals(loc, user.getLocation());
    }
    */

    /*
    @Test
    public void testAddRequests() {
        User owner = new User();
        User borrower = new User();
        Book book1 = new Book();
        book1.setTitle("Great Gatsby");
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);
        
        Request one = new Request(owner, borrower, book1, loc);
        
        Book book2 = new Book();
        book2.setTitle("Huckleberry Finn");

        Request two = new Request(owner, borrower, book2, loc);
        RequestList testList = new RequestList();
        testList.addRequest(one);
        testList.addRequest(two);

        user.addRequest(one);
        user.addRequest(two);

        assertEquals(testList, user.getRequestList());
    }

    @Test
    public void testAddBooks() {
        Book book1 = new Book();
        book1.setTitle("Great Gatsby");

        Book book2 = new Book();
        book2.setTitle("Huckleberry Finn");

        BookList testList = new BookList();
        testList.add(book1);
        testList.add(book2);

        user.addOwnedBook(book1);
        user.addOwnedBook(book2);

        assertEquals(testList, user.getOwnedList());
    }
    */
}
