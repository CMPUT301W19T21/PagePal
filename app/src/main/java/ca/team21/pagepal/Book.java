package ca.team21.pagepal;

import java.io.File;

public class Book {

    public final String AVAILABLE = "Available";
    public final String REQUESTED = "Requested";
    public final String ACCEPTED  = "Accepted";
    public final String BORROWED = "Borrowed";

    private String title;
    private String author;
    private String ISBN;
    private String description;
    private String status;
    private Cover_Photo photo;

    public Book() {}

    public Book(String title, String author, String ISBN, File photo) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.status = AVAILABLE; // defaults to available
        this.photo = new Cover_Photo(photo);
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

    public Cover_Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Cover_Photo photo) {

    }
}
