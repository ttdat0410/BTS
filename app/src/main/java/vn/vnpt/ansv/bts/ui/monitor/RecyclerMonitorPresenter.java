package vn.vnpt.ansv.bts.ui.monitor;

/**
 * Created by ANSV on 11/10/2017.
 */

public interface RecyclerMonitorPresenter {
    void setView(RecyclerMonitorView view);
    void getData(int stationId, RecyclerMonitorPresenterImpl.MonitorCallback callback);
}
