package vn.vnpt.ansv.bts.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import vn.vnpt.ansv.bts.common.injection.module.AppModule;
import vn.vnpt.ansv.bts.common.injection.module.PresenterModule;
import vn.vnpt.ansv.bts.ui.PreferenceManager;
import vn.vnpt.ansv.bts.ui.monitor.container.MonitorContainer;
import vn.vnpt.ansv.bts.ui.monitor.container.MonitorPresenterImpl;
import vn.vnpt.ansv.bts.ui.monitor.RecyclerMonitorFragment;
import vn.vnpt.ansv.bts.ui.monitor.RecyclerMonitorPresenterImpl;
import vn.vnpt.ansv.bts.ui.settings.SettingsActivity;
import vn.vnpt.ansv.bts.ui.splash.SplashActivity;
import vn.vnpt.ansv.bts.ui.splash.SplashPresenterImpl;
import vn.vnpt.ansv.bts.utils.NotificationUtils;

/**
 * Created by ANSV on 11/9/2017.
 */

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class})
public interface AppComponent {

    PreferenceManager providePreferenceManager();

    void inject(SplashActivity o);
    void inject(SplashPresenterImpl o);
    void inject(MonitorContainer o);
    void inject(MonitorPresenterImpl o);
    void inject(RecyclerMonitorFragment o);
    void inject(RecyclerMonitorPresenterImpl o);
    void inject(SettingsActivity o);
}
