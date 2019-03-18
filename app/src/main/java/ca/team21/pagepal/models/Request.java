package ca.team21.pagepal.models;

import android.location.Location;

public class Request {

    private User owner;
    private User requester;
    private Book book;
    private Location location;

    public Request() {};

    public Request(User owner, User requester, Book book) {}

    public Request(User owner, User requester, Book book, Location location) {}

    public User getOwner() {
        return owner;
    }

    public User getRequester() {
        return requester;
    }

    public Book getBook() {
        return book;
    }

    public void setLocation(Location location) {}

    public Location getLocation() {
        return location;
    }
}
