package vn.vnpt.ansv.bts.ui.splash;

import java.util.List;

import vn.vnpt.ansv.bts.objects.MinStationFullObj;

/**
 * Created by ANSV on 11/9/2017.
 */

public interface SplashView {
//    void showLoading();
//    void hideLoading();
    void showUpdate(boolean isUpdate);
    void showBottomView();
    void launchMonitor(List<MinStationFullObj> listStation);
}
