package project.rahul.com.mediap;

/**
 * Created by Rahul on 2/7/2017.
 */

public class Song {

    String title;
    String path;
    int aart;

    public Song(){}

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public Song(String title, String path, int aart) {
        this.title = title;
        this.path = path;
        this.aart = aart;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAart() {
        return aart;
    }

    public void setAart(int aart) {
        this.aart = aart;
    }
}
