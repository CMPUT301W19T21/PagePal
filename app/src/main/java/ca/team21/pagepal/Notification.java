package ca.team21.pagepal;

import java.util.Date;

public class Notification {

    private String message;
    private Date timestamp; // set to current time at instantiation

    public Notification(String message) {}


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {}


    public Date getTimestamp() {
        return timestamp;
    }
}
