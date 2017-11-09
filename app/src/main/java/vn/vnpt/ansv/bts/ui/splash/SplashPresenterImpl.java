package vn.vnpt.ansv.bts.ui.splash;

import android.content.Context;

import javax.inject.Inject;

import vn.vnpt.ansv.bts.common.injection.scope.ActivityScope;
import vn.vnpt.ansv.bts.utils.Utils;

/**
 * Created by ANSV on 11/9/2017.
 */

@ActivityScope
public class SplashPresenterImpl implements SplashPresenter {

    private Context context;

    @Inject
    public SplashPresenterImpl(Context context) {

    }

    @Override
    public boolean isNetwork(Context context) {
        return Utils.isNetworkAvailable(context);
    }

    @Override
    public void login() {

    }
}
