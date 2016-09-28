package android.test.seriespopularesapp.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by re032629 on 27/09/2016.
 */

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
                Log.d("DOWNLOAD IMG==", "The response is: " + response);
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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.pd.dismiss();
        }

    }
