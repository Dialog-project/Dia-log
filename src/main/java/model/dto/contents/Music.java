package model.dto.contents;

import java.time.LocalDate;
import java.util.Date;

public class Music extends Contents{
    private String Singer;
    private String Album;
    
    public String getSinger() {
        return Singer;
    }
    public void setSinger(String singer) {
        Singer = singer;
    }
    public String getAlbum() {
        return Album;
    }
    public void setAlbum(String album) {
        Album = album;
    }

    public void setTitle(String title) {
        super.setTitle(title);
    }

    public void setPublishDate(Date publishDate) {
        super.setPublishDate(publishDate);
    }

    public void setGenre(String genre) {
        super.setGenre(genre);
    }

    public String getTitle() {
        return super.getTitle();
    }

    public Date getPublishDate() {
        return super.getPublishDate();
    }

    public String getGenre() {
        return super.getGenre();
    }

    public Music() { }
    
    public Music(String singer, String album) {
        super();
        Singer = singer;
        Album = album;
    }
    
    @Override
    public String toString() {
        return "Music [Singer=" + Singer + ", Album=" + Album + "]";
    }
    
}
