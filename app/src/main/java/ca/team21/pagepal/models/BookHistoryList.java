package ca.team21.pagepal.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import ca.team21.pagepal.models.Book;


public class BookHistoryList implements Parcelable {
    private String BookHisAuthor;
    private String BookHisTitle;
    private String BookHisGenre;

    public BookHistoryList(String BookHisAuthor, String BookHisTitle, String BookHisGenre){
        this.BookHisAuthor = BookHisAuthor;
        this.BookHisTitle = BookHisTitle;
        this.BookHisGenre = BookHisGenre;

    }

    public BookHistoryList(Parcel parcel){
        this.BookHisAuthor = parcel.readString();
        this.BookHisTitle = parcel.readString();
        this.BookHisGenre = parcel.readString();
    }
    public static final Parcelable.Creator<BookHistoryList> CREATOR = new Parcelable.Creator<BookHistoryList>() {

        @Override
        public BookHistoryList createFromParcel(Parcel parcel) {
            return new BookHistoryList(parcel);
        }

        @Override
        public BookHistoryList[] newArray(int size) {
            return new BookHistoryList[0];
        }
    };
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(BookHisAuthor);
        dest.writeString(BookHisTitle);
        dest.writeString(BookHisGenre);
        //dest.writeValue(photo);
    }
    public int describeContents() {
        return hashCode();
    }
    public void setAuthor(String BookHisAuthor) {this.BookHisAuthor = BookHisAuthor;}

    public void setTitle(String BookHisTitle) { this.BookHisTitle = BookHisTitle; }

    public void setGenre(String BookHisGenre) {this.BookHisGenre = BookHisGenre;}


}


    /**private ArrayList<Book> historyList;


    public BookHistoryList() {}

    public void addBook(Book book) {



    }

    public void removeBook(Book book) {


    }

    public ArrayList<Book> getHistoryList() {
        return historyList;
    }
     */

