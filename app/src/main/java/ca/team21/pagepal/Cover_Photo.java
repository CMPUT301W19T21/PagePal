package ca.team21.pagepal;

import java.io.File;
import java.util.Date;

public class Cover_Photo {

    private Date timestamp;
    private File image;

    public Cover_Photo(File image) {
        this.image = image;
        this.timestamp = new Date(); // initializes to the current time and date
    }
}
