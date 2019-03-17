package ca.team21.pagepal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Represents a user with a unique username.
 */
public class User implements Parcelable {

    private String username;
    private String email;
    private ArrayList<Book> ownedBookList = new ArrayList<>();
    /*
    private String name;
    private Location location = new Location(LocationManager.NETWORK_PROVIDER);
    private RequestList requestList = new RequestList();
    private BookList ownedList = new BookList();
    private BookList borrowedList = new BookList();
    private NotificationList notificationList = new NotificationList();
    private BookHistoryList bookHistoryList = new BookHistoryList();
    */

    public User() {/* Empty constructor required for Firebase */}

    /**
     * Constructor used when creating new Users.
     * @param username  The username to set.
     * @param email The email to set.
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.ownedBookList = new ArrayList<>();
    }

    /**
     * Constructor for parcelable
     * @param in parcel to instantiate with
     */
    protected User(Parcel in) {
        username = in.readString();
        email = in.readString();
        in.readTypedList(ownedBookList, Book.CREATOR);
        if (ownedBookList == null) {
            ownedBookList = new ArrayList<>();
        }
        //name = in.readString();
        //location = in.readParcelable(Location.class.getClassLoader());
    }

    /**
     * CREATOR for parcelable
     */
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * Get the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username
     * TODO: Check if username is unique (currently only checked on account creation)
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /** Get the user's email
     *
     * @return The email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Get the user's owned books
     * @return THe user's books
     */
    public ArrayList<Book> getOwnedBookList() {return ownedBookList;}

    /**
     * Add a book to the user's owned books
     * @param book The book to add
     */
    public void addOwnedBook(Book book) {
        if (ownedBookList == null) {
            ownedBookList = new ArrayList<>();
        }

        // if book is not already in list
        if (!ownedBookList.contains(book)) {
            ownedBookList.add(book);
        }
    }


    /*
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    /*
    public RequestList getRequestList() {
        return requestList;
    }

    public void addRequest(Request request) {
        this.requestList.addRequest(request);
    }

    public void removeRequest(Request request) {
        this.requestList.removeRequest(request);
    }




    public void addOwnedBook(Book book) {
        this.ownedList.add(book);
    }

    public void removeOwnedBook(Book book) {
        this.ownedList.remove(book);
    }


    public BookList getBorrowedList() {return borrowedList;}

    public void addBorrowedBook(Book book) {
        this.borrowedList.add(book);
    }

    public void removeBorrowedBook(Book book) {
        this.borrowedList.remove(book);
    }


    public NotificationList getNotificationList() {
        return notificationList;
    }

    public BookHistoryList getBookHistoryList() {
        return bookHistoryList;
    }
    */

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the User object to a parcel
     * @param dest The parcel to write to
     * @param flags Flags for parcelable
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(email);
        dest.writeTypedList(ownedBookList);
        //dest.writeString(name);
        //location.writeToParcel(dest, flags);
    }
}
