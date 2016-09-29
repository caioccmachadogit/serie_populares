package android.test.seriespopularesapp.repository;

import android.app.Activity;
import android.test.seriespopularesapp.common.ICallBackAsync;
import android.test.seriespopularesapp.common.ICallBackRepository;
import android.test.seriespopularesapp.model.Series;
import android.test.seriespopularesapp.task.BuscarSeriesPopularesTask;
import android.test.seriespopularesapp.util.json.JsonUtil;

import java.util.List;


public class SerieRepositoryImpl implements SerieRepository {
    private Activity mActivity;
    private ICallBackRepository iCallBackRepository;

    public SerieRepositoryImpl(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void buscarSeriesPopulares(final ICallBackRepository iCallBackRepository) {
        this.iCallBackRepository = iCallBackRepository;

        new BuscarSeriesPopularesTask(mActivity, new ICallBackAsync() {
            @Override
            public void callBack(Object response) {
                JsonUtil jsonUtil = new JsonUtil();
                List<Series> list = jsonUtil.fromJsonList((String) response, Series.class);
                iCallBackRepository.callBack(list);
            }
        }).execute();

    }
}
