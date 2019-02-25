package ca.team21.pagepal;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

public class User {

    private String username;
    private String name;
    private Location location;
    private String email;
    private RequestList requestList;
    private BookList ownedList;
    private BookList borrowedList;
    private NotificationList notificationList;
    private BookHistoryList bookHistoryList;

    public User() {}

    public User(String username, String name, String email) {}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {}


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

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public NotificationList getNotificationList() {
        return notificationList;
    }

    public BookHistoryList getBookHistoryList() {
        return bookHistoryList;
    }
}
