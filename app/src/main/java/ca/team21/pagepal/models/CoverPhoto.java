package ca.team21.pagepal.models;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a cover photo for a book.
 */
public class CoverPhoto implements Serializable {

    private String time;
    private String photoString;

    /**
     * Constructor for CoverPhoto
     * @param photo The cover photo of the book
     */
    public CoverPhoto(String photo) {
        this.time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.photoString = photo; // initializes to the current time and date
    }


    /**
     * Get time string.
     *
     * @return the string
     */
    public String getTime(){
        return this.time;
    }

    /**
     * Get photo string.
     *
     * @return the photo string
     */
    public String getPhotoString(){
        return this.photoString;
    }

}
