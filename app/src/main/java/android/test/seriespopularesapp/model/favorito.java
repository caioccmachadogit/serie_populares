package android.test.seriespopularesapp.model;

import android.test.seriespopularesapp.util.db.convert.Column;
import android.test.seriespopularesapp.util.db.convert.DatabaseBeans;

import java.io.Serializable;

/**
 * Created by re032629 on 28/09/2016.
 */
@DatabaseBeans
public class Favorito implements Serializable{
    private static final long serialVersionUID = 1L;

    @Column
    private String _id;

    @Column
    private String idSerie;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(String idSerie) {
        this.idSerie = idSerie;
    }
}
