package android.test.seriespopularesapp;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.test.seriespopularesapp.model.Series;
import android.test.seriespopularesapp.task.ImageLoadTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by re032629 on 27/09/2016.
 */

public class DetalhesActivity extends AppCompatActivity {

    private Series serieDetalhe;
    private TextView tvFavorito;
    private TextView tvOverview;
    private TextView tvGeneros;
    private TextView tvEstreia;
    private TextView tvNota;
    private TextView tvEpisodios;
    private ImageView imgBanner;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView imgFavorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        serieDetalhe = (Series) getIntent().getSerializableExtra("SERIE_DETALHE");

        init();
    }

    private void init() {
        tvFavorito = (TextView) findViewById(R.id.txt_favorito);
        tvOverview = (TextView) findViewById(R.id.txt_overview);
        tvGeneros = (TextView) findViewById(R.id.txt_generos);
        tvEstreia = (TextView) findViewById(R.id.txt_estreia);
        tvNota = (TextView) findViewById(R.id.txt_nota);
        tvEpisodios = (TextView) findViewById(R.id.txt_episodios);
        imgBanner = (ImageView) findViewById(R.id.img_banner);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        imgFavorito = (ImageView) findViewById(R.id.img_favoritos);
        imgFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        collapsingToolbarLayout.setTitle(serieDetalhe.getTitle());
        tvFavorito.setText("não favorita");
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
}
