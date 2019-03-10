package ca.team21.pagepal;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CoverPhoto implements Serializable {

    private String time;
    private String photoString;

    public CoverPhoto(String photo) {
        this.time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        this.photoString = photo; // initializes to the current time and date
    }

    public String getTime(){
        return this.time;
    }

    public String getPhotoString(){
        return this.photoString;
    }
}
