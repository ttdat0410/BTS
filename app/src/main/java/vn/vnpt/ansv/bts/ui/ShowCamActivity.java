package vn.vnpt.ansv.bts.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ANSV on 11/27/2017.
 */

public class ShowCamActivity extends AppCompatActivity {

    public static String urlCam = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openBrowser(urlCam);

        /*if (appInstalledOrNot(this, urlCam )) {
            String url = urlCam;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } else {
            String url = urlCam;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }*/
        finish();
    }

    private void openBrowser(String urlCam) {
        if (urlCam.length() > 5) {
            String url = urlCam;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }

    /*private boolean appInstalledOrNot(Context context, String paramString) {
        PackageManager localPackageManager = context.getPackageManager();
        try {
            localPackageManager.getPackageInfo(paramString, 1);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
