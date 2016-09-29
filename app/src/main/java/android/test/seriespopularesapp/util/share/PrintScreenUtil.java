package android.test.seriespopularesapp.util.share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Santander on 28/09/16.
 */

public class PrintScreenUtil extends AppCompatActivity {

    public final static String PATHPRINT = Environment.getExternalStorageDirectory().getAbsolutePath()+"/APP_SERIES/PRINT/";
    private String externalFilePath = "";
    public final static int SEND_EMAIL = 666;
    private final int MILIS_BEFORE_DELETE_SCREENSHOT = 5000;

    public String SavePrintScreen(View mainContainer){
        CriarDirPRINT();
        String name = "serie.png";

        if(mainContainer != null){
            try {
                Bitmap b = loadBitmapFromView(mainContainer);
                externalFilePath = PATHPRINT + name;
                b.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(externalFilePath));
            }
            catch (FileNotFoundException e) {
                return "";
            }
            mainContainer.setDrawingCacheEnabled(false);
            return name;
        }
        return "";
    }

    private Bitmap loadBitmapFromView(View v) {
        try {
            if(v.getLayoutParams().width > 0 && v.getLayoutParams().height > 0){
                Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(b);
                v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height );
                v.draw(c);
                return b;
            }
            int total_width = v.getMeasuredWidth(); //Recupera el ancho total del parent

            //Habilita la cache para recuperar las medidas totales del contenido
            v.setDrawingCacheEnabled(true);
            v.measure(View.MeasureSpec.makeMeasureSpec(total_width, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            int total_height = v.getMeasuredHeight(); //Recupera el alto total del contenido

            v.layout(0, 0, total_width, total_height);
            v.buildDrawingCache(true);
            //Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
            Bitmap b = Bitmap.createBitmap( total_width, total_height, Bitmap.Config.ARGB_8888);
            v.setDrawingCacheEnabled(false);

            Canvas c = new Canvas(b);
            v.draw(c);
            return b;

        } catch (Exception e) {
            v.invalidate();
            v.refreshDrawableState();
            v.setDrawingCacheEnabled(true);
            Bitmap b = v.getDrawingCache();
            return b;
        }
    }

    public void CriarDirPRINT(){
        try{
            String state = Environment.getExternalStorageState();
            boolean mExternalStorageAvailable = false;
            boolean mExternalStorageWriteable = false;
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // Podemos ler e escrever os meios de comunica??o
                mExternalStorageAvailable = mExternalStorageWriteable = true;
                File file = new File (PATHPRINT);
                if ( !file.exists() ){
                    file.mkdirs();
                }
            }
            else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                // S? podemos ler a m?dia
                mExternalStorageAvailable = true;
                mExternalStorageWriteable = false;
            }
            else { // Outros status
                mExternalStorageAvailable = mExternalStorageWriteable = false;
            }
        } catch(Exception ex){
            ex.getMessage();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Delete stored screenshot
        if (requestCode == SEND_EMAIL) {
            //FIXME: Si borramos la imagen inmediatamente despu�s de recibir
            //el result, no se env�a adjunta en el correo. Se fijar� un
            //retardo para que esto no ocurra.
            final Handler deleteFileHandler = new Handler();
            deleteFileHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try{
                        new File(externalFilePath).delete();
                    }catch(Exception e){
                        Log.e("PrintScreenUtil", e.getMessage());
                    }
                }
            }, MILIS_BEFORE_DELETE_SCREENSHOT);
        }
    }
}

