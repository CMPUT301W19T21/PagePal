package ca.team21.pagepal;

import android.location.Location;

import java.util.ArrayList;

public class Request_List {

    private Profile owner;
    private ArrayList<Request> requestList;

    public Request_List(Profile owner) {
        this.owner = owner;
        requestList = new ArrayList<>();
    }

    public void addRequest(Profile owner, Profile requester, Book book, Location location) {
        ;
    }

    public void removeRequest(Book book) {
        ;
    }

    public Profile getOwner() {
        return owner;
    }

    public ArrayList<Request> getRequestList() {
        return requestList;
    }
}
