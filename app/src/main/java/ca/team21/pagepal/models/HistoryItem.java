package ca.team21.pagepal.models;

import android.os.Parcel;
import android.os.Parcelable;


public class HistoryItem implements Parcelable {
    private String BookHisAuthor;
    private String BookHisTitle;
    private String BookHisGenre;
    private String BookHisPhoto;

    public HistoryItem(String BookHisAuthor, String BookHisTitle, String BookHisGenre, String BookHisPhoto){
        this.BookHisAuthor = BookHisAuthor;
        this.BookHisTitle = BookHisTitle;
        this.BookHisGenre = BookHisGenre;
        this.BookHisPhoto = BookHisPhoto;


    }
    public HistoryItem(){

    }


    public HistoryItem(Parcel parcel){
        this.BookHisAuthor = parcel.readString();
        this.BookHisTitle = parcel.readString();
        this.BookHisGenre = parcel.readString();
        this.BookHisPhoto = parcel.readString();

    }
    public static final Parcelable.Creator<HistoryItem> CREATOR = new Parcelable.Creator<HistoryItem>() {

        @Override
        public HistoryItem createFromParcel(Parcel parcel) {
            return new HistoryItem(parcel);
        }

        @Override
        public HistoryItem[] newArray(int size) {
            return new HistoryItem[0];
        }
    };
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(BookHisAuthor);
        dest.writeString(BookHisTitle);
        dest.writeString(BookHisGenre);
        dest.writeString(BookHisPhoto);
    }
    public int describeContents() {
        return hashCode();
    }

    public void setBookHisAuthor(String BookHisAuthor) {this.BookHisAuthor = BookHisAuthor;}

    public void setBookHisTitle(String BookHisTitle) { this.BookHisTitle = BookHisTitle; }

    public void setBookHisGenre(String BookHisGenre) {this.BookHisGenre = BookHisGenre;}

    public void setBookHisPhoto(String BookHisPhoto) {
        this.BookHisPhoto = BookHisPhoto;
    }

    public String getBookHisAuthor() {
        return BookHisAuthor;
    }

    public String getBookHisGenre() {
        return BookHisGenre;
    }

    public String getBookHisPhoto() {
        return BookHisPhoto;
    }

    public String getBookHisTitle() {
        return BookHisTitle;
    }
}


    /**private ArrayList<Book> historyList;


    public HistoryItem() {}

    public void addBook(Book book) {



    }

    public void removeBook(Book book) {


    }

    public ArrayList<Book> getHistoryList() {
        return historyList;
    }
     */

