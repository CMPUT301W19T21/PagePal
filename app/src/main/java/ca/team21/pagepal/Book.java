package ca.team21.pagepal;

import java.io.File;

import ca.team21.pagepal.status.Available;
import ca.team21.pagepal.status.Book_Status;

public class Book {
    private String title;
    private String author;
    private String ISBN;
    private String description;
    private Book_Status status;
    private Cover_Photo photo;

    public Book(String title, String author, String ISBN, File photo) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.status = new Available(); // defaults to available
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

    public Book_Status getStatus() {
        return status;
    }

    public void setStatus(Book_Status status) {

    }

    public Cover_Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Cover_Photo photo) {

    }
}
