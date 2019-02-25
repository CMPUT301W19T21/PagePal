package ca.team21.pagepal;

import android.location.Location;

public class Request {

    private Profile owner;
    private Profile requester;
    private Book book;
    private Location location;

    public Request() {};

    public Request(Profile owner, Profile requester, Book book) {}

    public Profile getOwner() {
        return owner;
    }

    public Profile getRequester() {
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
