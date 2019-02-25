package ca.team21.pagepal;

import java.io.File;
import java.util.Date;

public class CoverPhoto {

    private Date timestamp;
    private File image;

    public CoverPhoto(File image) {
        this.image = image;
        this.timestamp = new Date(); // initializes to the current time and date
    }
}
