package android.test.seriespopularesapp.interactor;


import android.app.Activity;
import android.test.seriespopularesapp.R;
import android.test.seriespopularesapp.common.ICallBackRepository;
import android.test.seriespopularesapp.model.Series;
import android.test.seriespopularesapp.repository.SerieRepositoryImpl;

import java.util.List;

public class ListagemSeriesInteractorImpl implements ListagemSeriesInteractor{

    private Activity mActivity;
    private ListagemSeriesInteractor.OnListagemSeriesFinishedListener onListagemSeriesFinishedListener;
    private SerieRepositoryImpl serieRepository;

    public ListagemSeriesInteractorImpl(Activity mActivity, ListagemSeriesInteractor.OnListagemSeriesFinishedListener onListagemSeriesFinishedListener) {
        this.mActivity = mActivity;
        this.onListagemSeriesFinishedListener = onListagemSeriesFinishedListener;
        this.serieRepository = new SerieRepositoryImpl(mActivity);
    }

    @Override
    public void buscarSeriesPopulares() {
        serieRepository.buscarSeriesPopulares(new ICallBackRepository() {
            @Override
            public void callBack(Object response) {
                List<Series> list = (List<Series>) response;
                if(list != null){
                    onListagemSeriesFinishedListener.onSuccessBuscarSeriesPopulares(list);
                }
                else
                    onListagemSeriesFinishedListener.onErrorBuscarSeriesPopulares(mActivity.getApplicationContext().getString(R.string.msg_erro_request));
            }
        });
    }
}
