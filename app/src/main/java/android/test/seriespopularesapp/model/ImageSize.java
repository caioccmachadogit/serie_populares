package android.test.seriespopularesapp.model;

import java.io.Serializable;

/**
 * Created by re032629 on 25/09/2016.
 */

public class ImageSize implements Serializable{

    private String full;

    private String medium;

    private String thumb;

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
