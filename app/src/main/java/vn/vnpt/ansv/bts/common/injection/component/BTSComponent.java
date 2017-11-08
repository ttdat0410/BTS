package vn.vnpt.ansv.bts.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import vn.vnpt.ansv.bts.api.APIManager;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.common.injection.module.BTSModule;

/**
 * Created by ANSV on 11/7/2017.
 */
@Singleton
@Component(modules = {BTSModule.class})
public interface BTSComponent {
//    APIManager provideAPIManager();
    void inject(BTSApplication o);
}
