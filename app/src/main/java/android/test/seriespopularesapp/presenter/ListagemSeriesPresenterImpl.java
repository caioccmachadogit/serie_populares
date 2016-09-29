package android.test.seriespopularesapp.presenter;

import android.app.Activity;
import android.test.seriespopularesapp.interactor.ListagemSeriesInteractor;
import android.test.seriespopularesapp.interactor.ListagemSeriesInteractorImpl;
import android.test.seriespopularesapp.model.Series;
import android.test.seriespopularesapp.presentation.ListagemSeriesView;
import android.test.seriespopularesapp.repository.FavoritoRepository;
import android.test.seriespopularesapp.util.db.GerenciadorDB;

import java.util.List;

/**
 * Created by Santander on 29/09/16.
 */

public class ListagemSeriesPresenterImpl implements ListagemSeriesPresenter, ListagemSeriesInteractor.OnListagemSeriesFinishedListener{

    private Activity mActivity;
    private ListagemSeriesInteractor listagemSeriesInteractor;
    private ListagemSeriesView listagemSeriesView;

    public ListagemSeriesPresenterImpl(ListagemSeriesView listagemSeriesView) {
        this.mActivity = (Activity) listagemSeriesView;
        this.listagemSeriesView = listagemSeriesView;
        this.listagemSeriesInteractor = new ListagemSeriesInteractorImpl(this.mActivity,this);

        criaDB();
        buscarSeriesPopulares();
    }

    @Override
    public void buscarSeriesPopulares() {
        listagemSeriesInteractor.buscarSeriesPopulares();
    }

    @Override
    public void onSuccessBuscarSeriesPopulares(List<Series> list) {
        listagemSeriesView.setRecycleView(list);
    }

    @Override
    public void onErrorBuscarSeriesPopulares(String msg) {
        listagemSeriesView.setMsgError(msg);
    }

    private void criaDB() {
        /*----------------------------------------------------------------------------------------------------------------------------------------*/
        // **** CARREGA UM ARRAY DE QUERYS PARA EXECU��O NO MOMENTO DE CRIA��O DO BANCO DE DADOS
        GerenciadorDB.QUERY_CREATE_BANCO_DE_DADOS.add(FavoritoRepository.createTable);
        // **** CRIA DIR EXTERNO E BANCO DE DADOS
        GerenciadorDB ger = new GerenciadorDB();
        ger.criarDB(mActivity);
        /*----------------------------------------------------------------------------------------------------------------------------------------*/
    }
}
