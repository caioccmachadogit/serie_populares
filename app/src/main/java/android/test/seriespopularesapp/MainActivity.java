package android.test.seriespopularesapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.seriespopularesapp.model.Series;
import android.test.seriespopularesapp.repository.FavoritoRepository;
import android.test.seriespopularesapp.util.db.GerenciadorDB;
import android.test.seriespopularesapp.util.json.JsonUtil;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SeriesAdapter adapter;
    private final int  MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        verificaPermissao();
    }

    private void verificaPermissao() {
        if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(mActivity,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    else
                        init();
                }
                else
                    init();
    }

    private void init(){
        criaDB();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        new BuscarSeriesPopulares(this).execute("https://api.trakt.tv/shows/popular?extended=full,images");
    }

    private void criaDB() {
        /*----------------------------------------------------------------------------------------------------------------------------------------*/
        // **** CARREGA UM ARRAY DE QUERYS PARA EXECU��O NO MOMENTO DE CRIA��O DO BANCO DE DADOS
        GerenciadorDB.QUERY_CREATE_BANCO_DE_DADOS.add(FavoritoRepository.createTable);
        // **** CRIA DIR EXTERNO E BANCO DE DADOS
        GerenciadorDB ger = new GerenciadorDB();
        ger.criarDB(MainActivity.this);
        /*----------------------------------------------------------------------------------------------------------------------------------------*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    init();
                }
                else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private class BuscarSeriesPopulares extends AsyncTask<String, Void, String>{

        private Activity activity;
        private ProgressDialog pd;

        public BuscarSeriesPopulares(Activity activity) {
            this.activity = activity;
        }

        @Override
    protected void onPreExecute() {
        //---Inicia a espera do progresso---
		pd = ProgressDialog.show(activity, "Buscando informações", "Carregando...");
    }

        @Override
        protected String doInBackground(String... strings) {
            try {
                return requestBuscarSeriesPopulares(strings[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            JsonUtil jsonUtil = new JsonUtil();
            List<Series> list = jsonUtil.fromJsonList(result, Series.class);
            if(list != null)
                carregarRecycleView(list);
            else
                Toast.makeText(MainActivity.this, activity.getResources().getString(R.string.msg_erro_request), Toast.LENGTH_LONG).show();

            this.pd.dismiss();
        }
    }

    private void carregarRecycleView(List<Series> list) {
        adapter = new SeriesAdapter(this, list);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private String requestBuscarSeriesPopulares(String myurl) throws IOException {
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
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
