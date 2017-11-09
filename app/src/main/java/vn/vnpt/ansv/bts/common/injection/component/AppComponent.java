package vn.vnpt.ansv.bts.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import vn.vnpt.ansv.bts.common.injection.module.AppModule;
import vn.vnpt.ansv.bts.common.injection.module.PresenterModule;
import vn.vnpt.ansv.bts.ui.PreferenceManager;
import vn.vnpt.ansv.bts.ui.splash.SplashActivity;
import vn.vnpt.ansv.bts.ui.splash.SplashPresenterImpl;

/**
 * Created by ANSV on 11/9/2017.
 */

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class})
public interface AppComponent {

    PreferenceManager providePreferenceManager();

    void inject(SplashActivity o);
    void inject(SplashPresenterImpl o);
}
