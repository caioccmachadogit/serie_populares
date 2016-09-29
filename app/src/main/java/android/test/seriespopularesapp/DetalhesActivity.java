package android.test.seriespopularesapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.test.seriespopularesapp.model.Favorito;
import android.test.seriespopularesapp.model.Series;
import android.test.seriespopularesapp.repository.FavoritoRepository;
import android.test.seriespopularesapp.task.ImageLoadTask;
import android.test.seriespopularesapp.util.share.CompartilharUtil;
import android.test.seriespopularesapp.util.share.PrintScreenUtil;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by re032629 on 27/09/2016.
 */

public class DetalhesActivity extends PrintScreenUtil {

    private final int  MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Series serieDetalhe;
    private TextView tvOverview;
    private TextView tvGeneros;
    private TextView tvEstreia;
    private TextView tvNota;
    private TextView tvEpisodios;
    private ImageView imgBanner;
    private ImageView imgBannerCompartilhar;
    private TextView tvTituloCompartilhar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton floatingActionButtonFavoritos;
    private FloatingActionButton floatingActionButtonCompartilhar;
    NestedScrollView nestedScrollView;
    private Activity mActivity;
    private FavoritoRepository favoritoRepository = new FavoritoRepository(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        mActivity = this;

        serieDetalhe = (Series) getIntent().getSerializableExtra("SERIE_DETALHE");

        init();

        verificaFavorito();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvTituloCompartilhar.setVisibility(View.GONE);
        imgBannerCompartilhar.setVisibility(View.GONE);
    }

    private void init() {
        tvOverview = (TextView) findViewById(R.id.txt_overview);
        tvGeneros = (TextView) findViewById(R.id.txt_generos);
        tvEstreia = (TextView) findViewById(R.id.txt_estreia);
        tvNota = (TextView) findViewById(R.id.txt_nota);
        tvEpisodios = (TextView) findViewById(R.id.txt_episodios);
        imgBanner = (ImageView) findViewById(R.id.img_banner);
        imgBannerCompartilhar = (ImageView) findViewById(R.id.img_banner_compartilhar);
        tvTituloCompartilhar = (TextView) findViewById(R.id.txt_titulo_compartilhar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        floatingActionButtonCompartilhar = (FloatingActionButton) findViewById(R.id.floatButton_compartilhar);
        floatingActionButtonCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(mActivity,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


                    }
                    else
                        compartilhar();
                }
                else
                    compartilhar();
            }
        });
        floatingActionButtonFavoritos = (FloatingActionButton) findViewById(R.id.floatButton_favoritos);
        floatingActionButtonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favorito favorito = new Favorito();
                favorito.setIdSerie(serieDetalhe.getIds().getTrakt());
                try {
                    if(favoritoRepository.readByIdSerie(serieDetalhe.getIds().getTrakt())!= null){
                        if(favoritoRepository.deleteByIdSerie(serieDetalhe.getIds().getTrakt()) > 0){
                            Toast.makeText(getApplication(), mActivity.getApplicationContext().getString(R.string.del_favorito), Toast.LENGTH_SHORT).show();
                            floatingActionButtonFavoritos.setImageResource(R.drawable.ic_estrela_vazio);
                        }
                    }
                    else
                    if(favoritoRepository.insert(favorito) > 0){
                        Toast.makeText(getApplication(), mActivity.getApplicationContext().getString(R.string.add_favorito), Toast.LENGTH_SHORT).show();
                        floatingActionButtonFavoritos.setImageResource(R.drawable.ic_estrela);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        nestedScrollView = (NestedScrollView) findViewById(R.id.mainContainer);


        collapsingToolbarLayout.setTitle(serieDetalhe.getTitle());
        tvOverview.setText(serieDetalhe.getOverview());
        String generos = null;
        for (String gen:serieDetalhe.getGenres()) {
            if(generos == null)
                generos = gen;

            generos = generos+", "+ gen;
        }
        tvGeneros.setText(generos);
        tvEstreia.setText(serieDetalhe.getYear());
        tvNota.setText(serieDetalhe.getRating().substring(0,4));
        tvEpisodios.setText(serieDetalhe.getAired_episodes());

        new ImageLoadTask(this, serieDetalhe.getImages().getBanner().getFull(),imgBanner).execute();
    }

    private void verificaFavorito() {
        try {
            if(favoritoRepository.readByIdSerie(serieDetalhe.getIds().getTrakt()) != null)
                floatingActionButtonFavoritos.setImageResource(R.drawable.ic_estrela);
            else
                floatingActionButtonFavoritos.setImageResource(R.drawable.ic_estrela_vazio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void compartilhar() {

        tvTituloCompartilhar.setVisibility(View.VISIBLE);
        tvTituloCompartilhar.setText(serieDetalhe.getTitle());
        imgBannerCompartilhar.setVisibility(View.VISIBLE);
        imgBannerCompartilhar.setImageDrawable(imgBanner.getDrawable());

        String namePrint = this.SavePrintScreen(nestedScrollView);
        CompartilharUtil.CompartilharPrintScreen(this, namePrint);
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
                    compartilhar();
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
}
