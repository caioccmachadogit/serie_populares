package android.test.seriespopularesapp.interactor;


import android.test.seriespopularesapp.model.Series;

import java.util.List;

public interface ListagemSeriesInteractor {

    interface OnListagemSeriesFinishedListener{
        void onSuccessBuscarSeriesPopulares(List<Series> list);

        void onErrorBuscarSeriesPopulares(String msg);
    }

    void buscarSeriesPopulares();
}
