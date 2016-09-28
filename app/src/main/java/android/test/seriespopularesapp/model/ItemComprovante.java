package android.test.seriespopularesapp.model;

/**
 * Created by Santander on 28/09/16.
 */

public class ItemComprovante {

    public ItemComprovante(String label, String value) {
        this.label = label;
        this.value = value;
    }

    private String label;

    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
