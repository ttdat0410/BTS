package vn.vnpt.ansv.bts.common.injection.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import vn.vnpt.ansv.bts.ui.monitor.container.MonitorPresenter;
import vn.vnpt.ansv.bts.ui.monitor.container.MonitorPresenterImpl;
import vn.vnpt.ansv.bts.ui.monitor.RecyclerMonitorPresenter;
import vn.vnpt.ansv.bts.ui.monitor.RecyclerMonitorPresenterImpl;
import vn.vnpt.ansv.bts.ui.splash.SplashPresenter;
import vn.vnpt.ansv.bts.ui.splash.SplashPresenterImpl;

/**
 * Created by ANSV on 11/9/2017.
 */

@Module
public class PresenterModule {

    @Provides
    @Singleton
    SplashPresenter provideSplashPresenter(Context context) {
        return new SplashPresenterImpl(context);
    }

    @Provides
    @Singleton
    RecyclerMonitorPresenter provideRecyclerMonitorPresenter(Context context) {
        return new RecyclerMonitorPresenterImpl(context);
    }

    @Provides
    @Singleton
    MonitorPresenter provideMonitorPresenter(Context context) {
        return new MonitorPresenterImpl(context);
    }
}
