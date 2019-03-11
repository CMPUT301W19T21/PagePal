package ca.team21.pagepal;

import java.io.Serializable;
import java.util.ArrayList;

public class PhotoList implements Serializable {

    protected ArrayList<CoverPhoto> photoList;

    public PhotoList(){
        ArrayList<CoverPhoto> photoList = new ArrayList<>();
    }

    public ArrayList<CoverPhoto> getPhotos(){
        return photoList;
    }

    public void addPhoto(CoverPhoto newPhoto){
        photoList.add(newPhoto);
    }

    public void deletePhoto(CoverPhoto deletePhoto){
        photoList.remove(deletePhoto);
    }

    public void setPhotos(ArrayList<CoverPhoto> photos){
        this.photoList = photos;
    }

}
