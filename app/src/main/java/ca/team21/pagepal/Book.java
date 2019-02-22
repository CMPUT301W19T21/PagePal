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
        ;
    }

    public void setTitle(String title) {
        ;
    }

    public String getAuthor() {
        ;
    }

    public void setAuthor(String author) {
        ;
    }

    public String getISBN() {
        ;
    }

    public void setISBN(String ISBN) {
        ;
    }

    public String getDescription() {
        ;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Book_Status getStatus() {
        return status;
    }

    public void setStatus(Book_Status status) {
        this.status = status;
    }

    public Cover_Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Cover_Photo photo) {
        this.photo = photo;
    }
}
