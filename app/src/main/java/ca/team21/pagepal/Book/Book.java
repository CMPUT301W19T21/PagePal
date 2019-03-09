package ca.team21.pagepal.Book;

import android.os.Parcel;
import android.os.Parcelable;

//import java.io.File;


/**
 * Represents a Book
 * TODO: Implement cover photo (will need to store in database somehow, can't just be a local file)
 */
public class Book implements Parcelable {
    public static final String AVAILABLE = "available";
    public static final String REQUESTED = "requested";
    public static final String ACCEPTED = "accepted";
    public static final String BORROWED = "borrowed";
    private String title;
    private String author;
    private String ISBN;
    private String description;
    private String status;
    //private File photo;

    public Book() {}

    /**
     * Constructor for book. Requires title, author, and ISBN
     * @param title Title of the book
     * @param author Author of the book
     * @param ISBN ISBN of the book
     */
    public Book(String title, String author, String ISBN) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
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
        this.ISBN = parcel.readString();
        this.status = parcel.readString();
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
        dest.writeString(ISBN);
        dest.writeString(status);
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

    public String getDescription() {
        return description;
    }

    public void setStatus(String s) {
        // if the passed string is not one of the status types
        if (!s.equals(ACCEPTED) &&
            !s.equals(REQUESTED) &&
            !s.equals(ACCEPTED) &&
            !s.equals(BORROWED)){
            throw new IllegalArgumentException("Incorrect status type");
        } else {
            this.status = s;
        }
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

    public String getISBN() {
        return ISBN;
    }

    public String getStatus() {
        return status;
    }
}
