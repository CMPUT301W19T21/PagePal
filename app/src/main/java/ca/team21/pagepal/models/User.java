package ca.team21.pagepal.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ca.team21.pagepal.BR;

/**
 * Represents a user with a unique username.
 */
public class User extends BaseObservable implements Parcelable {

    private String uid;
    private String username;
    private String email;
    private ArrayList<Book> ownedBookList = new ArrayList<>();
    /*
    private Location location = new Location(LocationManager.NETWORK_PROVIDER);
    private RequestList requestList = new RequestList();
    private BookList ownedList = new BookList();
    private BookList borrowedList = new BookList();
    private NotificationList notificationList = new NotificationList();
    private BookHistoryList bookHistoryList = new BookHistoryList();
    */
    private static final User user = new User();

    public User() {/* Empty constructor required for Firebase */}

    /**
     * Constructor used when creating new Users.
     * @param username  The username to set.
     * @param email The email to set.
     */
    public User(String uid, String username, String email) {
        this.uid = uid;
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
     * Gets the User object that matches the currently logged in user.
     *
     * @return  The User relating to the current user. The fields of the returned User may be null
     *          until the query returns.
     */
    public static User getInstance() {
        if (user.getUid() != null && user.getUid().equals(FirebaseAuth.getInstance().getUid())) {
            return user;
        } else {
            String uid = FirebaseAuth.getInstance().getUid();
            final Query query = FirebaseDatabase.getInstance().getReference()
                    .child("users").orderByChild("uid").equalTo(uid);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String key = "";
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        key = item.getKey();
                        break;
                    }
                    User newUser = dataSnapshot.child(key).getValue(User.class);
                    user.setUsername(newUser.getUsername());
                    user.setUid(newUser.getUid());
                    user.setEmail(newUser.getEmail());
                    //TODO add setters as things are implemented.
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return user;
        }
    }

    /**
     * Get the uid.
     *
     * @return The uid.
     */
    public String getUid() {
        return this.uid != null ? this.uid : "";
    }

    /**
     * Set the uid.
     * @param uid	The string to use.
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Get the username
     * @return the username
     */
    @Bindable
    public String getUsername() {
        return username != null ? this.username : "";
    }

    /**
     * Set the username
     * TODO: Check if username is unique (currently only checked on account creation)
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    /** Get the user's email
     *
     * @return The email
     */
    @Bindable
    public String getEmail() {
        return this.email != null ? this.email : "";
    }

    /**
     * Set the email
     * @param email The String to set.
     */
    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    /**
     * Get the user's owned books
     * @return THe user's books
     */
    @Bindable
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
            notifyPropertyChanged(BR.ownedBookList);
        }
    }


    /*

    @Bindable
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    /*
    @Bindable
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


    @Bindable
    public BookList getBorrowedList() {return borrowedList;}

    public void addBorrowedBook(Book book) {
        this.borrowedList.add(book);
    }

    public void removeBorrowedBook(Book book) {
        this.borrowedList.remove(book);
    }


    @Bindable
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
        //location.writeToParcel(dest, flags);
    }

    /**
     * Writes the User to the database.
     *
     * @return The Task object for adding listeners to.
     */
    public Task<Void> writeToDb() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users");

        return db.child(this.username).setValue(this);
    }
}
