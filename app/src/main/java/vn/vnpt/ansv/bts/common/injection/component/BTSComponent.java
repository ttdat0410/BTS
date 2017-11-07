package vn.vnpt.ansv.bts.common.injection.component;

import dagger.Component;
import vn.vnpt.ansv.bts.common.app.BTSApplication;
import vn.vnpt.ansv.bts.common.injection.module.BTSModule;

/**
 * Created by ANSV on 11/7/2017.
 */

@Component(modules = {BTSModule.class})
public interface BTSComponent {

    void inject(BTSApplication o);
}
