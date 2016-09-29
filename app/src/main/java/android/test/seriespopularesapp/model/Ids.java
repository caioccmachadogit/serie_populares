package android.test.seriespopularesapp.model;

import java.io.Serializable;

/**
 * Created by re032629 on 28/09/2016.
 */

public class Ids implements Serializable{
    private String trakt;

    private String slug;

    public String getTrakt() {
        return trakt;
    }

    public void setTrakt(String trakt) {
        this.trakt = trakt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
