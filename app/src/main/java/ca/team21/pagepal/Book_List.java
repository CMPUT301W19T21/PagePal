package ca.team21.pagepal;

import java.util.ArrayList;

public class Book_List {

    private Profile owner;
    private ArrayList<Book> bookList;

    public Book_List(Profile owner) {
        this.owner = owner;
        this.bookList = new ArrayList<>();
    }

    public void addBook(Book book) {

    }

    public void removeBook(Book book) {

    }

    public Profile getOwner() {
        return owner;
    }

    public ArrayList<Book> getBookList() {
        return bookList;
    }
}
