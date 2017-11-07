package vn.vnpt.ansv.bts.common.app;

import android.app.Application;

import vn.vnpt.ansv.bts.BuildConfig;
import vn.vnpt.ansv.bts.common.injection.component.BTSComponent;
import vn.vnpt.ansv.bts.common.injection.component.DaggerBTSComponent;
import vn.vnpt.ansv.bts.common.injection.module.BTSModule;

/**
 * Created by ANSV on 11/7/2017.
 */

public class BTSApplication extends Application {
    private static final String TAG = BTSApplication.class.getSimpleName();

    private BTSComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component().inject(this);
    }

    public BTSComponent component() {
        if (component == null) {
            component = DaggerBTSComponent.builder().bTSModule(new BTSModule(this, BuildConfig.REST_API_IP, BuildConfig.REST_API_PORT)).build();
        }
        return component;
    }
}
