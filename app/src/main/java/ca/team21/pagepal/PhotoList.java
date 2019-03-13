package ca.team21.pagepal;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A list of cover photos
 */
public class PhotoList implements Serializable {

    protected ArrayList<CoverPhoto> photoList;

    /**
     * Instantiates a new Photo list.
     */
    public PhotoList(){
        ArrayList<CoverPhoto> photoList = new ArrayList<>();
    }

    /**
     * Get photos array list.
     *
     * @return the array list
     */
    public ArrayList<CoverPhoto> getPhotos(){
        return photoList;
    }

    /**
     * Add photo.
     *
     * @param newPhoto the new photo
     */
    public void addPhoto(CoverPhoto newPhoto){
        photoList.add(newPhoto);
    }

    /**
     * Delete photo.
     *
     * @param deletePhoto the photo to delete
     */
    public void deletePhoto(CoverPhoto deletePhoto){
        photoList.remove(deletePhoto);
    }

    /**
     * Set the photo list.
     *
     * @param photos the photo list
     */
    public void setPhotos(ArrayList<CoverPhoto> photos){
        this.photoList = photos;
    }

}
