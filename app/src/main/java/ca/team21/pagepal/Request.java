package ca.team21.pagepal;

import android.location.Location;

public class Request {

    private User owner;
    private User requester;
    private Book book;
    private Location location;

    public Request() {};

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

    public Location getLocation() {
        return location;
    }
}
