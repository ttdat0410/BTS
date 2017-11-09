package vn.vnpt.ansv.bts.ui.splash;

import android.content.Context;

/**
 * Created by ANSV on 11/9/2017.
 */

public interface SplashPresenter {
    void setView(SplashView splashView);
    boolean checkNetwork(Context context);
    void getUser(String user, String pass, SplashPresenterImpl.Callback callback);
}