package android.test.seriespopularesapp.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.test.seriespopularesapp.common.ICallBackAsync;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuscarSeriesPopularesTask extends AsyncTask<Void, Void, String> {

    private Activity activity;
    private ProgressDialog pd;
    private final String URL = "https://api.trakt.tv/shows/popular?extended=full,images";
    private ICallBackAsync iCallBackAsync;

    public BuscarSeriesPopularesTask(Activity activity, ICallBackAsync iCallBackAsync) {
        this.activity = activity;
        this.iCallBackAsync = iCallBackAsync;
    }

    @Override
    protected void onPreExecute() {
        //---Inicia a espera do progresso---
        pd = ProgressDialog.show(activity, "Buscando informações", "Carregando...");
    }

    @Override
    protected String doInBackground(Void... param) {
        try {
            return getBuscarSeriesPopulares(URL);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        iCallBackAsync.callBack(result);
        if (pd != null) {
            pd.cancel();
            pd = null;
        }
        super.onPostExecute(result);
    }


    private String getBuscarSeriesPopulares(String myurl) throws IOException {
        InputStream is = null;
        int length = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("trakt-api-version", "2");
            conn.setRequestProperty("trakt-api-key", "db8ec0f19d9095078b3ba855b194ba936f07bef8d44f75e7242daba8747eca16");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("RESPONSE=====", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = convertInputStreamToString(is);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
