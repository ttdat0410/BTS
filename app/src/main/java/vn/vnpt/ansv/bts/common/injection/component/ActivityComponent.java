package vn.vnpt.ansv.bts.common.injection.component;

import dagger.Component;
import vn.vnpt.ansv.bts.common.injection.scope.ActivityScope;
import vn.vnpt.ansv.bts.monitor.MonitorContainer;

/**
 * Created by ANSV on 11/7/2017.
 */

@ActivityScope
@Component(dependencies = BTSComponent.class)
public interface ActivityComponent {
    void inject(MonitorContainer o);
}
