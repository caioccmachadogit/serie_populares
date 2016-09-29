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
import android.test.seriespopularesapp.model.Favorito;
import android.test.seriespopularesapp.model.ItemComprovante;
import android.test.seriespopularesapp.model.Series;
import android.test.seriespopularesapp.repository.FavoritoRepository;
import android.test.seriespopularesapp.task.ImageLoadTask;
import android.test.seriespopularesapp.util.share.CompartilharUtil;
import android.test.seriespopularesapp.util.share.PrintScreenUtil;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton floatingActionButtonFavoritos;
    private FloatingActionButton floatingActionButtonCompartilhar;
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

        LinearLayout linearLayoutContainer = new LinearLayout(mActivity);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(120, 120);
        linearLayoutContainer.setLayoutParams(linearLayoutParams);
        linearLayoutContainer.setOrientation(LinearLayout.VERTICAL);
        linearLayoutContainer.setPadding(0, 15, 0, 0);
        linearLayoutContainer.setBackgroundColor(getColor(R.color.viewBg));

        ItemComprovante itemComprovanteOrverview = new ItemComprovante("Resumo", serieDetalhe.getOverview());
        linearLayoutContainer.addView(montarItemComprovante(itemComprovanteOrverview));

        ItemComprovante itemComprovanteTitulo = new ItemComprovante("Titulo", serieDetalhe.getTitle());
        linearLayoutContainer.addView(montarItemComprovante(itemComprovanteTitulo));


        String namePrint = this.SavePrintScreen(linearLayoutContainer);
        CompartilharUtil.CompartilharPrintScreen(this, namePrint);
    }

    private void criarViewCompartilhavel() {
        LinearLayout linearLayoutMainContainer = new LinearLayout(getApplicationContext());

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setLayoutParams(linearLayoutParams);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textViewLabel = new TextView(getApplicationContext());
        textViewLabel.setLayoutParams(params);
        textViewLabel.setText(tvOverview.getText().toString());

        linearLayout.addView(textViewLabel);



    }

    private LinearLayout montarItemComprovante(ItemComprovante itemComprovante) {

        LinearLayout linearLayout = new LinearLayout(mActivity);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(linearLayoutParams);


//        if(itemList.isAnotherLine()){
//            linearLayout.setOrientation(LinearLayout.VERTICAL);
//            linearLayout.setPadding(0, 15, 0, 0);
//        }
//        else {
//            linearLayout.setPadding(5, 15, 0, 0);
//            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        }

        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(0, 15, 0, 0);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textViewLabel = new TextView(mActivity);
        textViewLabel.setLayoutParams(params);
        textViewLabel.setText(itemComprovante.getLabel());

        TextView textViewValue = new TextView(mActivity);
        textViewValue.setPadding(10, 0, 0, 0);
        textViewValue.setLayoutParams(params);
        textViewValue.setText(itemComprovante.getValue());

        linearLayout.addView(textViewLabel);
        linearLayout.addView(textViewValue);

        return linearLayout;
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
                            Toast.makeText(getApplication(), "Removido dos favoritos", Toast.LENGTH_SHORT).show();
                            floatingActionButtonFavoritos.setImageResource(R.drawable.ic_estrela_vazio);
                        }
                    }
                    else
                        if(favoritoRepository.insert(favorito) > 0){
                            Toast.makeText(getApplication(), "Adicionado aos favoritos", Toast.LENGTH_SHORT).show();
                            floatingActionButtonFavoritos.setImageResource(R.drawable.ic_estrela);
                        }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
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
