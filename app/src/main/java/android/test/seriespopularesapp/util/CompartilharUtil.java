package android.test.seriespopularesapp.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Santander on 28/09/16.
 */

public class CompartilharUtil {

    public static void CompartilharPrintScreen(Activity mActivity, String print) {
        try {
            Intent sharedIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);

            final String PATH = PrintScreenUtil.PATHPRINT;

            sharedIntent.setType("text/plain");

            ArrayList<Uri> uris = new ArrayList<Uri>();
            uris.add(Uri.parse("file://" + PATH + print));

            sharedIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

            sharedIntent.setType("image/png");

            mActivity.startActivityForResult(
                    Intent.createChooser(sharedIntent, "Compartilhar:"),
                    PrintScreenUtil.SEND_EMAIL);
        } catch (Exception e) {
            Log.e("Compartilhar", e.getMessage(), e);
        }
    }
}
