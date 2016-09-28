package android.test.seriespopularesapp;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.test.seriespopularesapp.model.Series;
import android.test.seriespopularesapp.task.ImageLoadTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by re032629 on 27/09/2016.
 */

public class DetalhesActivity extends AppCompatActivity {

    private Series serieDetalhe;
    private TextView tvOverview;
    private TextView tvGeneros;
    private TextView tvEstreia;
    private TextView tvNota;
    private TextView tvEpisodios;
    private ImageView imgBanner;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton floatingActionButtonFavoritos;
    private FloatingActionButton floatingActionButtonCompartilhar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        serieDetalhe = (Series) getIntent().getSerializableExtra("SERIE_DETALHE");

        init();
    }

    private void init() {
        tvOverview = (TextView) findViewById(R.id.txt_overview);
        tvGeneros = (TextView) findViewById(R.id.txt_generos);
        tvEstreia = (TextView) findViewById(R.id.txt_estreia);
        tvNota = (TextView) findViewById(R.id.txt_nota);
        tvEpisodios = (TextView) findViewById(R.id.txt_episodios);
        imgBanner = (ImageView) findViewById(R.id.img_banner);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        floatingActionButtonCompartilhar = (FloatingActionButton) findViewById(R.id.floatButton_compartilhar);
        floatingActionButtonCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        floatingActionButtonFavoritos = (FloatingActionButton) findViewById(R.id.floatButton_favoritos);
        floatingActionButtonFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Adicionado aos favoritos", Toast.LENGTH_SHORT).show();
            }
        });


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
}
