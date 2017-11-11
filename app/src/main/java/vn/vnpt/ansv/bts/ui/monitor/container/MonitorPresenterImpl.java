package vn.vnpt.ansv.bts.ui.monitor.container;

import android.content.Context;

import vn.vnpt.ansv.bts.common.app.BTSApplication;

/**
 * Created by ANSV on 11/11/2017.
 */

public class MonitorPresenterImpl implements MonitorPresenter {

    public MonitorPresenterImpl(Context context) {
        (((BTSApplication) context).getAppComponent()).inject(this);
    }

}
