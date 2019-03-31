package ca.team21.pagepal.models;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Represents a notification which lets the user know when their books are requested or when their
 * requests are accepted.
 */
public class Notification {

    private String message;
    private String sender; // username of sender
    private String recipient; // username of recipient
    private String bookIsbn;
    private String owner;
    private Boolean unread;
    private Date timestamp;

    /**
     * Constructor for Notification
     * @param message Message of the notification
     */
    public Notification(String message, String sender, String recipient, String bookIsbn, String owner) {
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.bookIsbn = bookIsbn;
        this.owner = owner;
        this.unread = true; // defaults to unread
        this.timestamp = new Date(); // defaults to current time
    }

    /**
     * Empty constructor for notification
     */
    public Notification() {}

    public Task<Void> writeToDb() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("notifications");

        return dbRef.child(this.getRecipient()).child(this.getTimestamp().toString()).setValue(this);
    }

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
    public void setMessage(String message) {this.message = message;}

    /**
     * Get the time and date of the notification
     * @return The time and date of the notification
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Set the time and date
     * @param timestamp The time and date of the notification
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getUnread() {
        return unread;
    }

    public void setUnread(Boolean unread) {
        this.unread = unread;
    }

    public Task<Void> delete() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("notifications");

        return db.child(this.getRecipient()).child(this.getTimestamp().toString()).removeValue();
    }
}
