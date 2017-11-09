package vn.vnpt.ansv.bts.ui.splash;

import android.content.Context;

/**
 * Created by ANSV on 11/9/2017.
 */

public interface SplashPresenter {
    boolean isNetwork(Context context);
    void login();
}