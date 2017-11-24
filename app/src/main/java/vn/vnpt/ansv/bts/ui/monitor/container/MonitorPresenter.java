package vn.vnpt.ansv.bts.ui.monitor.container;

import android.support.v4.app.Fragment;

import com.github.florent37.materialviewpager.header.HeaderDesign;

import java.util.List;

import vn.vnpt.ansv.bts.objects.MinStationFullObj;

/**
 * Created by ANSV on 11/11/2017.
 */

public interface MonitorPresenter {
    void setView(MonitorView view);
    Fragment setFragment(List<MinStationFullObj> listAllStation, int position);
    int getCount(List<MinStationFullObj> listAllStation);
    CharSequence getPageTitle(List<MinStationFullObj> listAllStation, int position);
    HeaderDesign getHeaderDesign(List<MinStationFullObj> listAllStation, int page);
    void connectMQTT();
    void subscribeToTopic(String topic, MonitorPresenterImpl.MQTTCallback callback);
    void unsubscribeToToptic(String topic);
    void publicToMQTT(String topic);
    void showNotification(String message);
}
