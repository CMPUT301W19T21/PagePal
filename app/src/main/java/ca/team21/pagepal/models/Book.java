package ca.team21.pagepal.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import java.io.File;


/**
 * Represents a Book
 * TODO: Implement cover photo (will need to store in database somehow, can't just be a local file)
 */
public class Book implements Parcelable {

    /**
     * The constant AVAILABLE.
     */
    public static final String AVAILABLE = "Available";
    /**
     * The constant REQUESTED.
     */
    public static final String REQUESTED = "Requested";
    /**
     * The constant ACCEPTED.
     */
    public static final String ACCEPTED = "Accepted";
    /**
     * The constant BORROWED.
     */
    public static final String BORROWED = "Borrowed";

    private String title;
    private String author;
    private String isbn;
    private String description;
    private String status;
    private String genre;
    private String owner;
    private String borrower;
    //private File photo;

    /**
     * Empty constructor for Book, sets default values
     */
    public Book() {
        this.status = AVAILABLE;
    }

    /**
     * Constructor for book. Requires title, author, and isbn
     *
     * @param title  Title of the book
     * @param author Author of the book
     * @param ISBN   isbn of the book
     */
    public Book(String title, String author, String ISBN) {
        this.title = title;
        this.author = author;
        this.isbn = ISBN;
        this.description = ""; // defaults to empty string
        this.status = AVAILABLE; // defaults to available
        //this.photo = new File("");
    }

    /**
     * Constructor for parcelable
     *
     * @param parcel Parcel to create the object with
     */
    public Book(Parcel parcel) {
        this.title = parcel.readString();
        this.author = parcel.readString();
        this.description = parcel.readString();
        this.isbn = parcel.readString();
        this.status = parcel.readString();
        this.genre = parcel.readString();
        this.owner = parcel.readString();
        this.borrower = parcel.readString();
        //this.photo = (File) parcel.readValue(null); // TODO write photo to parcelable
    }

    /**
     * Writes Book object to a parcel
     * @param dest parcel to write to
     * @param flags flags used by parcelable
     */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(isbn);
        dest.writeString(status);
        dest.writeString(genre);
        dest.writeString(owner);
        dest.writeString(borrower);
        //dest.writeValue(photo);
    }

    /**
     * CREATOR for Parcelable interface
     */
    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {

        @Override
        public Book createFromParcel(Parcel parcel) {
            return new Book(parcel);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[0];
        }
    };


    public int describeContents() {
        return hashCode();
    }

    /**
     * Sets the title of the book
     *
     * @param title Title of the book
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the author of the book
     *
     * @param author Author of the book
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets isbn.
     *
     * @param isbn the isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        // if the passed string is not one of the status types
        if (!status.equals(AVAILABLE) &&
            !status.equals(REQUESTED) &&
            !status.equals(ACCEPTED) &&
            !status.equals(BORROWED)){
            throw new IllegalArgumentException("Incorrect status type");
        } else {
            this.status = status;
        }
    }

    /**
     * Sets owner.
     *
     * @param username the username of the owner
     */
    public void setOwner(String username) {
        this.owner = username;
    }

    /*
    public void setPhoto(File photo) {
        this.photo = photo;
    } */


    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets isbn.
     *
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets genre.
     *
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getBorrower() {
        if (borrower == null) {
            return "";
        } else {
            return borrower;
        }
    }

    /**
     * Write the Book to the database.
     * @return  The Task for adding listeners to.
     */
    public Task<Void> writeToDb() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("books");

        return db.child(this.owner).child(this.isbn).setValue(this);
    }

    /**
     * Delete the Book from the database
     * @return  The Task for adding listeners to.
     */
    public Task<Void> delete() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("books");

        return db.child(this.owner).child(this.isbn).removeValue();
    }
}
