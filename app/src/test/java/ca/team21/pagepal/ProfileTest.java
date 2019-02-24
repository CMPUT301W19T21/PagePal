package ca.team21.pagepal;

import android.location.Location;
import android.location.LocationManager;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ProfileTest {
    private Profile profile = new Profile();

    @Test
    public void setUserNameTest() {
        String uName = "TheGreatOne99";

        profile.setUsername(uName);
        assertEquals(uName, profile.getUsername());
    }

    @Test
    public void setNameTest() {
        String name = "Wayne Gretzky";

        profile.setName(name);
        assertEquals(name, profile.getName());
    }

    @Test
    public void setEmailTest() {
        String email = "oilers4evr@hotmail.com";

        profile.setEmail(email);
        assertEquals(email, profile.getEmail());
    }

    @Test
    public void setDobTest() {
        Date birthday = new Date();

        profile.setDOB(birthday);
        assertEquals(birthday, profile.getDOB());
    }

    @Test
    public void setLocationTest() {
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);

        profile.setLocation(loc);
        assertEquals(loc, profile.getLocation());
    }

    @Test
    public void addRequests() {
        Profile owner = new Profile();
        Profile borrower = new Profile();
        Book book1 = new Book();
        book1.setTitle("Great Gatsby");
        Location loc = new Location(LocationManager.NETWORK_PROVIDER);
        
        Request one = new Request(owner, borrower, book1, loc);
        
        Book book2 = new Book();
        book2.setTitle("Huckleberry Finn");

        Request two = new Request(owner, borrower, book2, loc);
        Request_List testList = new Request_List(borrower);
        testList.addRequest(one);
        testList.addRequest(two);

        profile.addRequest(one);
        profile.addRequest(two);

        assertEquals(testList, profile.getRequestList());
    }

    @Test
    public void addBooks() {
        Book book1 = new Book();
        book1.setTitle("Great Gatsby");

        Book book2 = new Book();
        book2.setTitle("Huckleberry Finn");

        Book_List testList = new Book_List(profile);
        testList.addBook(book1);
        testList.addBook(book2);

        profile.addBook(book1);
        profile.addBook(book2);

        assertEquals(testList, profile.getBookList());
    }
}
