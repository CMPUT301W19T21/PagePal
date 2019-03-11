package ca.team21.pagepal.Book;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.AccessibleObject;

//import java.io.File;


/**
 * Represents a Book
 * TODO: Implement cover photo (will need to store in database somehow, can't just be a local file)
 */
public class Book implements Parcelable {

    public static final String AVAILABLE = "Available";
    public static final String REQUESTED = "Requested";
    public static final String ACCEPTED = "Accepted";
    public static final String BORROWED = "Borrowed";

    private String title;
    private String author;
    private String isbn;
    private String description;
    private String status;
    private String genre;
    private String owner;
    //private File photo;

    public Book() {
    }

    /**
     * Constructor for book. Requires title, author, and isbn
     * @param title Title of the book
     * @param author Author of the book
     * @param ISBN isbn of the book
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
     * @param parcel Parcel to create the object with
     */
    public Book(Parcel parcel) {
        this.title = parcel.readString();
        this.author = parcel.readString();
        this.description = parcel.readString();
        this.isbn = parcel.readString();
        this.status = parcel.readString();
        this.owner = parcel.readString();
        //this.photo = (File) parcel.readValue(null);
    }

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

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(isbn);
        dest.writeString(status);
        dest.writeString(owner);
        //dest.writeValue(photo);
    }

    public int describeContents() {
        return hashCode();
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setStatus(String s) {
        // if the passed string is not one of the status types
        if (!s.equals(AVAILABLE) &&
            !s.equals(REQUESTED) &&
            !s.equals(ACCEPTED) &&
            !s.equals(BORROWED)){
            throw new IllegalArgumentException("Incorrect status type");
        } else {
            this.status = s;
        }
    }

    public void setOwner(String uid) {
        this.owner = uid;
    }

    /*
    public void setPhoto(File photo) {
        this.photo = photo;
    } */


    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getOwner() {
        return owner;
    }

    public String getGenre() {
        return genre;
    }
}
