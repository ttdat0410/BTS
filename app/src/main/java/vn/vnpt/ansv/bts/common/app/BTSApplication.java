package vn.vnpt.ansv.bts.common.app;

import android.app.Application;

import vn.vnpt.ansv.bts.common.injection.component.AppComponent;
import vn.vnpt.ansv.bts.common.injection.component.DaggerAppComponent;
import vn.vnpt.ansv.bts.common.injection.module.AppModule;

/**
 * Created by ANSV on 11/7/2017.
 */

public class BTSApplication extends Application {

    /*private AppComponent appComponent;
    public  AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initDagger(this);
    }

    protected AppComponent initDagger(BTSApplication application) {
        return DaggerAppComponent.builder().appModule(new AppModule(application)).build();
    }*/
    private AppComponent appComponent;
    public  AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initDagger(this);
    }

    protected AppComponent initDagger(BTSApplication application) {
        return DaggerAppComponent.builder().appModule(new AppModule(application)).build();
    }
}
