package ca.team21.pagepal;

import java.util.Date;

/**
 * Represents a notification which lets the user know when their books are requested or when their
 * requests are accepted.
 */
public class Notification {

    private String message;
    private Date timestamp; // set to current time at instantiation

    /**
     * Constructor for Notification
     * @param message Message of the notification
     */
    public Notification(String message) {}

    /**
     * Get the message
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message
     * @param message The message
     */
    public void setMessage(String message) {}

    /**
     * Get the time and date of the notification
     * @return The time and date
     */
    public Date getTimestamp() {
        return timestamp;
    }
}
