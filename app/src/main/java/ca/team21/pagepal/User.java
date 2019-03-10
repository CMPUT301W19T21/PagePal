package ca.team21.pagepal;

import android.location.Location;
import android.location.LocationManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.internal.ParcelableSparseArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import ca.team21.pagepal.Book.Book;
import ca.team21.pagepal.Book.BookList;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public ArrayList<Book> getOwnedBookList() {return ownedBookList;}

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(email);
        dest.writeTypedList(ownedBookList);
        //dest.writeString(name);
        //location.writeToParcel(dest, flags);
    }
}
