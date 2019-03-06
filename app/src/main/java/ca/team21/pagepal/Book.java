package ca.team21.pagepal;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Book {
    public static final String AVAILABLE = "available";
    public static final String REQUESTED = "requested";
    public static final String ACCEPTED = "accepted";
    public static final String BORROWED = "borrowed";
    private String title;
    private String author;
    private String ISBN;
    private String description;
    private String status;
    private String genre;
    private CoverPhoto photo;

    public Book() {}

    public Book(String title, String author, String ISBN,String genre ,File photo) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.status = AVAILABLE; // defaults to available
        this.genre = genre;
        this.photo = new CoverPhoto(photo);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {

    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
    }

    public String getGenre() {return genre;}

    public void setGenre(String genre){

    }

    // My plan is that when you return a book we take the genre of that book add one value to the
    //counter for that key and add it to the dictionary
    // Map<String, Integer> numberMapping = new HashMap<>();
    //example of adding a key = action, and the value = 0





    public CoverPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(CoverPhoto photo) {
    }

}
