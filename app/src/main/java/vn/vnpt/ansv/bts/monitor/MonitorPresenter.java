package vn.vnpt.ansv.bts.monitor;

import javax.inject.Inject;

import vn.vnpt.ansv.bts.api.APIManager;

/**
 * Created by ANSV on 11/7/2017.
 */
public class MonitorPresenter {

    private APIManager apiManager;

    @Inject
    public MonitorPresenter(APIManager apiManager) {
        this.apiManager = apiManager;
    }
}
