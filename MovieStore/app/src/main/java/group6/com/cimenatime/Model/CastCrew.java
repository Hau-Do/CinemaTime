package group6.com.cimenatime.Model;

/**
 * Created by Phuong Doan on 04/02/2017.
 */

public class CastCrew {

    private String name;
    private int photoId;

    private String photoPath;

    public CastCrew(String name, int photoId) {
        this.name = name;
        this.photoId = photoId;
    }

    public CastCrew(String name, String photoPath) {
        this.name = name;
        this.photoPath = photoPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

}
