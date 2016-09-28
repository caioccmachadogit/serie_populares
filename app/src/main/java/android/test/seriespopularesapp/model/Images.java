package android.test.seriespopularesapp.model;

import java.io.Serializable;

/**
 * Created by re032629 on 25/09/2016.
 */

public class Images implements Serializable{

    private ImageSize banner;

    private ImageSize thumb;

    public ImageSize getBanner() {
        return banner;
    }

    public void setBanner(ImageSize banner) {
        this.banner = banner;
    }

    public ImageSize getThumb() {
        return thumb;
    }

    public void setThumb(ImageSize thumb) {
        this.thumb = thumb;
    }
}
