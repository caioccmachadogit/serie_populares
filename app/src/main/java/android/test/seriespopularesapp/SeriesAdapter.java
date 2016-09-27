package android.test.seriespopularesapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.test.seriespopularesapp.model.Series;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by re032629 on 26/09/2016.
 */

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.MyViewHolder> {

    private Activity activity;
    private List<Series> seriesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo, nota, estreia;
        public ImageView thumbnail, overflow;
        public Series serie;

        public MyViewHolder(View view) {
            super(view);
            titulo = (TextView) view.findViewById(R.id.titulo);
            nota = (TextView) view.findViewById(R.id.nota);
            estreia = (TextView) view.findViewById(R.id.estreia);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public SeriesAdapter(Activity activity, List<Series> seriesList) {
        this.activity = activity;
        this.seriesList = seriesList;
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

            if(series.getImages().getThumb().getFull() != null && !series.getImages().getThumb().getFull().isEmpty())
                new ImageLoadTask(activity, series.getImages().getThumb().getFull(),holder.thumbnail).execute();

            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(holder);
                }
            });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(MyViewHolder holder) {
        // inflate menu
        PopupMenu popup = new PopupMenu(activity, holder.overflow);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_serie, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(holder));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        MyViewHolder holderSelected;

        public MyMenuItemClickListener(MyViewHolder holderSelected) {
            this.holderSelected = holderSelected;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favotitos:
                    Toast.makeText(activity, "Adicionado aos favoritos", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_detalhes:
                    Toast.makeText(activity, holderSelected.serie.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }



    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;
        private ProgressDialog pd;
        private Activity activity;

        public ImageLoadTask(Activity activity, String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            //---Inicia a espera do progresso---
            pd = ProgressDialog.show(activity, "Buscando Series", "Carregando...");
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                int response = connection.getResponseCode();
                Log.d("DOWNLOAD IMG=====", "The response is: " + response);
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if(result != null)
                imageView.setImageBitmap(result);

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.pd.dismiss();
        }

    }
}
