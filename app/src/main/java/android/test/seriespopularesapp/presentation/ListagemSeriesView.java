package android.test.seriespopularesapp.presentation;


import android.test.seriespopularesapp.model.Series;

import java.util.List;

public interface ListagemSeriesView {

    void setRecycleView(List<Series> list);

    void setMsgError(String msgError);
}
