package ca.team21.pagepal;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

public class User {

    private String username;
    private Location location;
    private ArrayList<Request> requestList;
    private ArrayList<Book> ownedList;
    private ArrayList<Book> borrowedList;

    public User() {}

    public User(String username, Date DOB, String email) {}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {}


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {}


    public ArrayList<Request> getRequestList() {
        return requestList;
    }

    public void addRequest(Request request) {
        requestList.add(request);
    }

    public void removeRequest(Request request) {}


    public ArrayList<Book> getOwnedList() {return ownedList;}

    public void addOwnedBook(Book book) {}

    public void removeOwnedBook(Book book) {}


    public ArrayList<Book> getBorrowedList() {return borrowedList;}

    public void addBorrowedBook(Book book) {}

    public void removeBorrowedBook(Book book) {}
}
