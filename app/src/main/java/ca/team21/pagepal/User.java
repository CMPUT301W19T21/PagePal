package ca.team21.pagepal;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

public class User {

    private String username;
    private String name;
    private String email;
    private Location location;
    private RequestList requestList;
    private BookList ownedList;
    private BookList borrowedList;
    private NotificationList notificationList;
    private BookHistoryList bookHistoryList;

    public User() {/* Empty constructor required for Firebase */}

    /**
     * Constructor used when creating new Users.
     * @param username  The username to set.
     * @param email     The email to set.
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {}


    public RequestList getRequestList() {
        return requestList;
    }

    public void addRequest(Request request) {}

    public void removeRequest(Request request) {}


    public BookList getOwnedList() {return ownedList;}

    public void addOwnedBook(Book book) {}

    public void removeOwnedBook(Book book) {}


    public BookList getBorrowedList() {return borrowedList;}

    public void addBorrowedBook(Book book) {}

    public void removeBorrowedBook(Book book) {}


    public NotificationList getNotificationList() {
        return notificationList;
    }

    public BookHistoryList getBookHistoryList() {
        return bookHistoryList;
    }
}
