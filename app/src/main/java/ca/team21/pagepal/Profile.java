package ca.team21.pagepal;

import android.location.Location;

import java.util.Date;

public class Profile {

    private String username;
    private String name;
    private Location location;
    private Date DOB;
    private String email;
    private Request_List requestList;
    private Book_List bookList;

    public Profile(String username, String name, Date DOB, String email) {
        this.username = username;
        this.name= name;
        this.DOB = DOB;
        this.email = email;
        requestList = new Request_List(this);
        bookList = new Book_List(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation() {

    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

    }

    public Request_List getRequestList() {
        return requestList;
    }

    public Book_List getBookList() {
        return bookList;
    }
}
