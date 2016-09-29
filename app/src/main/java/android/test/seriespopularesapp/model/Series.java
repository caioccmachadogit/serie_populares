package android.test.seriespopularesapp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by re032629 on 25/09/2016.
 */

public class Series implements Serializable {
    private String title;

    private String year;

    private String overview;

    private String rating;

    private String homepage;

    private List<String> genres;

    private Images images;

    private String aired_episodes;

    private Ids ids;

    public Ids getIds() {
        return ids;
    }

    public void setIds(Ids ids) {
        this.ids = ids;
    }

    public String getAired_episodes() {
        return aired_episodes;
    }

    public void setAired_episodes(String aired_episodes) {
        this.aired_episodes = aired_episodes;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
