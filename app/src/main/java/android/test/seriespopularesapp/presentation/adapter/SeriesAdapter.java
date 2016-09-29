package android.test.seriespopularesapp.presentation.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.test.seriespopularesapp.DetalhesActivity;
import android.test.seriespopularesapp.R;
import android.test.seriespopularesapp.model.Favorito;
import android.test.seriespopularesapp.model.Series;
import android.test.seriespopularesapp.repository.FavoritoRepository;
import android.test.seriespopularesapp.task.ImageLoadTask;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by re032629 on 26/09/2016.
 */

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.MyViewHolder> {

    private Activity activity;
    private List<Series> seriesList;
    private FavoritoRepository favoritoRepository;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo, nota, estreia;
        public ImageView thumbnail, overflow, favorito;
        public Series serie;

        public MyViewHolder(View view) {
            super(view);
            titulo = (TextView) view.findViewById(R.id.titulo);
            nota = (TextView) view.findViewById(R.id.nota);
            estreia = (TextView) view.findViewById(R.id.estreia);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
            favorito = (ImageView) view.findViewById(R.id.img_favorito);
        }
    }


    public SeriesAdapter(Activity activity, List<Series> seriesList) {
        this.activity = activity;
        this.seriesList = seriesList;
        favoritoRepository = new FavoritoRepository(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.serie_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Series series = seriesList.get(position);

        holder.titulo.setText(series.getTitle());
        holder.nota.setText("Nota: "+series.getRating().substring(0,4));
        holder.estreia.setText("Ano estreia: "+series.getYear());
        holder.serie = series;
        verificaFavorito(holder);

        if(series.getImages().getThumb().getFull() != null && !series.getImages().getThumb().getFull().isEmpty())
            new ImageLoadTask(activity, series.getImages().getThumb().getFull(),holder.thumbnail).execute();

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder);
            }
        });
    }

    private void verificaFavorito(MyViewHolder holder) {
        try {
            if(favoritoRepository.readByIdSerie(holder.serie.getIds().getTrakt()) != null)
                holder.favorito.setVisibility(View.VISIBLE);
            else
                holder.favorito.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(MyViewHolder holder) {
        // inflate menu
        PopupMenu popup = new PopupMenu(activity, holder.overflow);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_serie, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(activity, holder));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        MyViewHolder holderSelected;
        Activity activity;

        public MyMenuItemClickListener(Activity activity, MyViewHolder holderSelected) {
            this.holderSelected = holderSelected;
            this.activity = activity;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favotitos:
                    verificaFavorito();
                    return true;
                case R.id.action_detalhes:
                    Intent i = new Intent(activity.getApplicationContext(), DetalhesActivity.class);
                    i.putExtra("SERIE_DETALHE", holderSelected.serie);
                    activity.startActivity(i);
                    return true;
                default:
            }
            return false;
        }

        private void verificaFavorito() {
            Favorito favorito = new Favorito();
            favorito.setIdSerie(holderSelected.serie.getIds().getTrakt());
            try {
                if(favoritoRepository.readByIdSerie(holderSelected.serie.getIds().getTrakt())!= null){
                    if(favoritoRepository.deleteByIdSerie(holderSelected.serie.getIds().getTrakt()) > 0){
                        Toast.makeText(activity, activity.getApplicationContext().getString(R.string.del_favorito), Toast.LENGTH_SHORT).show();
                        holderSelected.favorito.setVisibility(View.GONE);
                    }
                }
                else
                    if(favoritoRepository.insert(favorito) > 0){
                        Toast.makeText(activity, activity.getApplicationContext().getString(R.string.add_favorito), Toast.LENGTH_SHORT).show();
                        holderSelected.favorito.setVisibility(View.VISIBLE);
                    }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }

}
