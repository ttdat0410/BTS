package vn.vnpt.ansv.bts.common.injection.component;

import dagger.Component;
import vn.vnpt.ansv.bts.monitor.Monitor;

/**
 * Created by ANSV on 11/7/2017.
 */

@Component(dependencies = BTSComponent.class)
public interface ActivityComponent {
    void inject(Monitor o);
}
